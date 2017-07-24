package com.kurotkin;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.lang.System.exit;

/**
 * Created by Vitaly Kurotkin on 10.07.2017.
 */
public class Deleter {
    public static void main(String[] args) {
        if (args[0].length() == 0)
            exit(1);
        String collName = args[0];
        MongoClient mongo = new MongoClient( "10.0.0.1" , 27017 );
        MongoCollection coll = mongo.getDatabase("news").getCollection(collName);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        MongoCursor<Document> cur1 = coll.find().iterator();
        try {
            while (cur1.hasNext()) {
                Document one = cur1.next();
                String guid1 = one.getString("guid");
                ObjectId id1 = one.getObjectId("_id");
                System.out.println(">   " + one.getObjectId("_id").toString() + " " + one.getString("guid") + " " + dateFormat.format(one.getDate("pubDate")));
                MongoCursor<Document> cur2 = coll.find().iterator();
                while (cur2.hasNext()){
                    Document two = cur2.next();
                    String guid2 = two.getString("guid");
                    ObjectId id2 = two.getObjectId("_id");
                    if(guid1.equals(guid2) && !(id1.equals(id2))){
                        System.out.println("Del " + two.getObjectId("_id").toString() + " " + two.getString("guid") + " " + dateFormat.format(two.getDate("pubDate")));
                        DeleteResult deleteResult = coll.deleteOne(new Document("_id", id2));
                    }
                }
                cur2.close();
            }
        } finally {
            cur1.close();
        }
    }
}
