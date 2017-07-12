package com.kurotkin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Vitaly Kurotkin on 12.07.2017.
 */
public class Parser {
    public static void main(String[] args) {

        for (int i = 101087; i < 201087; i++){
            Document doc  = null;
            try {
                doc  = Jsoup.connect("https://www.tinkoff.ru/invest/news/" + Integer.toString(i) +"/").userAgent("Mozilla").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String S = doc.getElementsByClass("_3WKsv _3KH0m _3uytC").text();
            System.out.println(S);
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
}
