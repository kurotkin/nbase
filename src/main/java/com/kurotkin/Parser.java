package com.kurotkin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.exit;

/**
 * Created by Vitaly Kurotkin on 12.07.2017.
 */
public class Parser {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static void main(String[] args) {
        Mongo mongo = new Mongo("10.0.0.1", "news", "ru_tinkoff_invest_news");
        int i = mongo.reqMax("number").getInteger("number");
        int j = 5;

        for (; i < 1000000; i++) { //37804
            String url = "https://www.tinkoff.ru/invest/news/" + Integer.toString(i) +"/";

            org.bson.Document docOld = mongo.req("guid", url);
            if (docOld != null) {
                System.out.println("ok " + i);
                continue;
            }
            org.bson.Document doc = getDoc(url);
            if (doc == null) {
                System.out.print(doc);
                System.out.println(" " + i);
                j--;
                if (j < 0)
                    exit(0);
                continue;
            }
            j = 5;
            doc.append("guid", url);
            doc.append("number", i);
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
        if (str[1].toLowerCase().equals("сегодня")) {
            d = new Date();
            String[] strTime = str[0].split(":");
            d.setHours(Integer.parseInt(strTime[0]));
            d.setMinutes(Integer.parseInt(strTime[1]));
        } else if ((str[1].toLowerCase().equals("вчера"))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -1);
            d = cal.getTime();
            String[] strTime = str[0].split(":");
            d.setHours(Integer.parseInt(strTime[0]));
            d.setMinutes(Integer.parseInt(strTime[1]));
        } else {
            date = Integer.parseInt(str[0]);
            year = Integer.parseInt(str[2]);
            month = parsMonth(str[1]);
            d = new Date(year, month, date);
        }
        return d;
    }

    public static int parsMonth(String str) {
        int month = 0;
        if (str.equals("январь"))
            month = 1;
        if (str.equals("февраль"))
            month = 2;
        if (str.equals("март"))
            month = 3;
        if (str.equals("апрель"))
            month = 4;
        if (str.equals("май"))
            month = 5;
        if (str.equals("июнь"))
            month = 6;
        if (str.equals("июль"))
            month = 7;
        if (str.equals("август"))
            month = 8;
        if (str.equals("сентябрь"))
            month = 9;
        if (str.equals("октябрь"))
            month = 10;
        if (str.equals("ноябрь"))
            month = 11;
        if (str.equals("декабрь"))
            month = 12;
        return month;
    }
}
