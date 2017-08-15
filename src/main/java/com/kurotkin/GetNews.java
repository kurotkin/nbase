package com.kurotkin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.kurotkin.Parser.getDoc;

/**
 * Created by Vitaly Kurotkin on 15.08.2017.
 */
public class GetNews {
    private static Logger log = Logger.getLogger(GetNews.class.getName());
    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        while (true){
            try {
                int unser = get();
                log.warning("Error! Guid is vary big!");
                Thread.sleep(14400000);
            } catch (InterruptedException e) {
                log.warning(e.toString());
            }
        }
    }

    private static int get() throws InterruptedException {
        log.info("Start program is started .... ");
        Mongo mongo = new Mongo("10.0.0.1", "news", "ru_tinkoff_invest_news");
        int i = mongo.reqMax("number").getInteger("number");
        log.info("Start from " + i + " guid");
        int j = 30;
        for (; i < 1000000; i++) {
            String url = "https://www.tinkoff.ru/invest/news/" + Integer.toString(i) +"/";
            if (mongo.req("guid", url) != null) {
                log.info("OK " + i);
                continue;
            }
            org.bson.Document doc = getDoc(url);
            if (doc == null) {
                log.info("Number " + i + "NULL");
                j--;
                if (j < 0)
                    return 0;
                continue;
            }
            j = 30;
            doc.append("guid", url);
            doc.append("number", i);
            System.out.println(i);
            mongo.update(doc);
            Thread.sleep(2000);
        }
        return 1;
    }


}
