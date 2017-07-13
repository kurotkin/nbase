package com.kurotkin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sdecima.rssreader.RSSFeedReader;
import org.sdecima.rssreader.RSSItem;
import org.sdecima.rssreader.stores.ArrayListRSSFeedStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vitaly Kurotkin on 12.07.2017.
 */
public class Parser {
    public static void main(String[] args) {

        for (int i = 101087; i < 201087; i++){
            String url = "https://www.tinkoff.ru/invest/news/" + Integer.toString(i) +"/";

            getDoc(url);
        }
        // https://www.mkyong.com/java/jsoup-html-parser-hello-world-examples/



//        Elements metaElements = doc.select("meta");
//
//        // get page title
//        String title = doc.title();
//        System.out.println("title : " + title);
//
//        // get all links
//        Elements links = doc.select("a[href]");
//        for (Element link : links) {
//
//            // get the value from href attribute
//            System.out.println("\nlink : " + link.attr("href"));
//            System.out.println("text : " + link.text());
//        }
    }

    public static org.bson.Document getDoc(String url) {
        Document doc  = null;
        try {
            doc  = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dat = doc.getElementsByClass("_3WKsv _3KH0m _3uytC").text();
        String br = doc.getElementsByClass("_3WKsv _3zKqJ _3uytC").text();
        String name = doc.getElementsByClass("_3WKsv _2yX9u").text();
        String sd = doc.getElementsByClass("_3WKsv _3zKqJ iaS10").text();
        String s = doc.getElementsByClass("_1KGyb").text();

        System.out.println(dat);
        System.out.println(br);
        System.out.println(name);
        System.out.println(sd);
        System.out.println(s);

            org.bson.Document docBson = new org.bson.Document("guid", "")
                    .append("title", "")
                    .append("description", "")
                    .append("content", "")
                    .append("pubDate", "");

        return docBson;
    }
}
