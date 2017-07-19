package com.kurotkin;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Created by Vitaly on 14.07.17.
 */
public class Mongo {
    private String host;
    private String db;
    private String collection;
    private UpdateOptions options;

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


    public Document req(String fuild, String value) {
        MongoClient myMongo = new MongoClient( host , 27017 );
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
}
