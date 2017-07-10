package com.kurotkin;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.sdecima.rssreader.RSSFeedReader;
import org.sdecima.rssreader.RSSItem;
import org.sdecima.rssreader.stores.ArrayListRSSFeedStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vitaly on 06.07.17.
 */
public class Worker implements Runnable {

    private String urlRSS;
    private String collectionName;
    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");



    public Worker(String urlRSS, String collectionName) {
        this.urlRSS = urlRSS;
        this.collectionName = collectionName;
    }

    public List<Document> getRSS() {
        ArrayListRSSFeedStore feedStore = new ArrayListRSSFeedStore();
        RSSFeedReader.read(urlRSS, feedStore);
        ArrayList<RSSItem> list = feedStore.getList();
        List<Document> documents = new ArrayList<>();
        int i = 0;
        for(RSSItem l : list) {
            Document doc = new Document("guid", l.getGuid())
                    .append("title", l.getTitle())
                    .append("description", l.getDescription())
                    .append("content", l.getContent())
                    .append("pubDate", l.getPubDate());
            documents.add(doc);
            i++;
        }
        System.out.println(dateFormat.format(new Date()) + " get " + Integer.toString(i) + " from " + urlRSS);
        return documents;
    }

    @Override
    public void run() {
        final CountDownLatch insertLatch = new CountDownLatch(1);

        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        mongo.getDatabase("news").getCollection(collectionName).insertMany(getRSS());
        mongo.close();

        boolean inserted = false;
        try {
            inserted = insertLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert(inserted);
    }
}
