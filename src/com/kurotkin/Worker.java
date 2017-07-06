package com.kurotkin;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.sdecima.rssreader.RSSFeedReader;
import org.sdecima.rssreader.RSSItem;
import org.sdecima.rssreader.stores.ArrayListRSSFeedStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaly on 06.07.17.
 */
public class Worker implements Runnable {

    private String urlRSS;
    private String collectionName;

    public Worker(String urlRSS, String collectionName) {
        this.urlRSS = urlRSS;
        this.collectionName = collectionName;
    }

    @Override
    public void run() {
        ArrayListRSSFeedStore feedStore = new ArrayListRSSFeedStore();
        RSSFeedReader.read(urlRSS, feedStore);

        ArrayList<RSSItem> list = feedStore.getList();

        MongoClientURI connectionString = new MongoClientURI("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("news");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        List<Document> documents = new ArrayList<>();
        for(RSSItem l : list) {
            Document doc = new Document("guid", l.getGuid())
                    .append("title", l.getTitle())
                    .append("description", l.getDescription())
                    .append("content", l.getContent())
                    .append("pubDate", l.getPubDate());
            documents.add(doc);
        }
        collection.insertMany(documents);
    }
}
