package com.kurotkin;

import com.kurotkin.processing.FilteredUnigram;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.text;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Created by Vitaly on 14.07.17.
 */
public class Mongo {
    private String host;
    private String db;
    private String collection;
    private UpdateOptions options;
    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Mongo(String host, String db, String collection) {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        options = new UpdateOptions();
        options.upsert(true);
        this.host = host;
        this.db = db;
        this.collection = collection;
    }

    public void update(Document doc) {
        final CountDownLatch insertLatch = new CountDownLatch(1);
        MongoClient mongo = new MongoClient( host , 27017 );
        MongoCollection coll = mongo.getDatabase(db).getCollection(collection);

        coll.updateOne(doc, new org.bson.Document("$set", doc), options);
        mongo.close();

        boolean inserted = false;
        try {
            inserted = insertLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert(inserted);
    }

    public void insert(Document doc) {
        final CountDownLatch insertLatch = new CountDownLatch(1);
        MongoClient mongo = new MongoClient(host, 27017 );
        MongoCollection coll = mongo.getDatabase(db).getCollection(collection);

        coll.insertOne(doc);
        mongo.close();

        boolean inserted = false;
        try {
            inserted = insertLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert(inserted);
    }

    public Document req(String fuild, String value) {
        MongoClient myMongo = new MongoClient(host, 27017 );
        MongoCollection coll = myMongo.getDatabase(db).getCollection(collection);
        Document doc = (Document) coll.find(eq(fuild, value)).first();
        myMongo.close();
        return doc;
    }

    public Document reqMax(String fuild) {
        MongoClient myMongo = new MongoClient( host , 27017 );
        MongoCollection coll = myMongo.getDatabase(db).getCollection(collection);
        Document doc = (Document) coll.find().sort(descending(fuild)).first();
        myMongo.close();
        return doc;
    }

    public void reAddedNumber() {
        MongoClient myMongo = new MongoClient( host , 27017 );
        MongoCollection coll = myMongo.getDatabase(db).getCollection(collection);
        MongoCursor<Document> cursor = coll.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String[] str = doc.getString("guid").split("/");
                int number = Integer.parseInt(str[5]);
                System.out.println(number);
                Bson newValue = new Document("number", number);
                coll.updateOne(doc, new Document("$set", newValue), options);
            }
        } finally {
            cursor.close();
        }
        myMongo.close();
    }

    public void correctYear() {
        MongoClient myMongo = new MongoClient( host , 27017 );
        MongoCollection coll = myMongo.getDatabase(db).getCollection(collection);
        MongoCursor<Document> cursor = coll.find().iterator();
        int i = 0;
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Date dateFromBase = doc.getDate("Date");
                int year = dateFromBase.getYear();
                System.out.print(i + " ");
                i++;
                System.out.println(year);
                if (year == 117) {
                    year = year + 1900;
                    dateFromBase.setYear(year);
                    System.out.println(dateFormat.format(dateFromBase));
                    Bson newValue = new Document("Date", dateFromBase);
                    coll.updateOne(doc, new Document("$set", newValue), options);
                }

            }
        } finally {
            cursor.close();
        }

        myMongo.close();
    }


    public void create(String comp, String search) {
        MongoClient myMongo = new MongoClient(host , 27017 );
        MongoCollection coll = myMongo.getDatabase(db).getCollection(collection);
        MongoCursor<Document> cursor = coll.find(text(search)).iterator(); // {$text:{$search:"Роснефт"}}

        Mongo mongo = new Mongo(host, db, comp);
        FilteredUnigram fltr = new FilteredUnigram();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int number = doc.getInteger("number");

                if (!mongo.exist("number", number)) {
                    Set<String> strs = fltr.getNGram(doc.getString("description"));
                    List<String> words = strs.stream().collect(Collectors.toList());
                    Document toNew = new Document(comp, comp)
                            .append("number", number)
                            .append("date", doc.getDate("Date"))
                            .append("words", words);
                    mongo.insert(toNew);
                    System.out.print(dateFormat.format(doc.getDate("Date")));
                    System.out.println(" " + number + " " + comp);
                }
            }
        } finally {
            cursor.close();
        }
        myMongo.close();
    }

    public boolean exist (String fuild, int value) {
        Document doc;
        try {
            doc = new MongoClient( host , 27017 )
                    .getDatabase(db)
                    .getCollection(collection)
                    .find(eq(fuild, value))
                    .first();
            if (doc.equals(null))
                return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

}
