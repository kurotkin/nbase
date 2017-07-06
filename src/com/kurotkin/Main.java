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


public class Main {

    public static void main(String[] args) {
        ArrayListRSSFeedStore feedStore = new ArrayListRSSFeedStore();

        RSSFeedReader.read("http://news.rambler.ru/rss/USA/", feedStore);

        ArrayList<RSSItem> list = feedStore.getList();

        MongoClientURI connectionString = new MongoClientURI("mongodb://10.0.0.1:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("news");
        MongoCollection<Document> collection = database.getCollection("ru_rambler_news_USA");

        int i = 0; // http://jsman.ru/mongo-book/Glava-1-Osnovy.html
        for(RSSItem l : list) {
            Document doc = new Document("guid", l.getGuid())
                    .append("title", l.getTitle())
                    .append("description", l.getDescription())
                    .append("content", l.getContent())
                    .append("pubDate", l.getPubDate());

            collection.insertOne(doc);
            i++;
        }
        System.out.println(i);



    }
}
