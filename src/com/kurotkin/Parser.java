package com.kurotkin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Vitaly Kurotkin on 12.07.2017.
 */
public class Parser {
    public static void main(String[] args) {
        Mongo mongo = new Mongo("10.0.0.1", "news", "ru_tinkoff_invest_news");
        for (int i = 45972; i < 1000000; i++) { //37804
            String url = "https://www.tinkoff.ru/invest/news/" + Integer.toString(i) +"/";

            org.bson.Document docOld = mongo.req("guid", url);
            if (docOld != null) {
                System.out.println("ok " + i);
                continue;
            }
            org.bson.Document doc = getDoc(url);
            System.out.print(doc);
            System.out.println(" " + i);
            if (doc == null)
                continue;
            doc.append("guid", url);
            System.out.println(i);
            mongo.update(doc);
        }
    }

    public static org.bson.Document getDoc(String url) {
        Document doc  = null;
        try {
            doc  = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dat = doc.getElementsByClass("_3WKsv _3KH0m _3uytC").text();
        if (dat.equals(""))
            return null;
        Date date = parseDate(dat);
        String br = doc.getElementsByClass("_3WKsv _3zKqJ _3uytC").text();
        String name = doc.getElementsByClass("_3WKsv _2yX9u").text();
        String sd = doc.getElementsByClass("_3WKsv _3zKqJ iaS10").text();
        String s = doc.getElementsByClass("_1KGyb").text();

        org.bson.Document docBson = new org.bson.Document("Date", date)
                .append("title", name)
                .append("description", sd)
                .append("content", s)
                .append("creater", br);

        return docBson;
    }

    public static Date parseDate(String dat) {
        Date d;
        String[] str = dat.split(" ");
        int year, month, date;
        if (str[1].equals("сегодня")) {
            d = new Date();
        } else {
            date = Integer.parseInt(str[0]);
            year = Integer.parseInt(str[2]);
            month = 0;
            if (str[1].equals("январь"))
                month = 1;
            if (str[1].equals("февраль"))
                month = 2;
            if (str[1].equals("март"))
                month = 3;
            if (str[1].equals("апрель"))
                month = 4;
            if (str[1].equals("май"))
                month = 5;
            if (str[1].equals("июнь"))
                month = 6;
            if (str[1].equals("июль"))
                month = 7;
            if (str[1].equals("август"))
                month = 8;
            if (str[1].equals("сентябрь"))
                month = 9;
            if (str[1].equals("октябрь"))
                month = 10;
            if (str[1].equals("ноябрь"))
                month = 11;
            if (str[1].equals("декабрь"))
                month = 12;
            d = new Date(year, month, date);
        }
        return d;
    }
}
