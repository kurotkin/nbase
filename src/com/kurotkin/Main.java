package com.kurotkin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class Main {

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        ScheduledExecutorService [] service = new ScheduledExecutorService[6];

        for(int i = 0; i < service.length; i++) {
            service[i] = Executors.newSingleThreadScheduledExecutor();
        }

        service[0].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/head/",
                "ru_rambler_news_head"), 0, 1200, TimeUnit.SECONDS);

        service[1].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/world/",
                "ru_rambler_news_world"), 200, 1200, TimeUnit.SECONDS);

        service[2].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/politics/",
                "ru_rambler_news_politics"), 400, 1200, TimeUnit.SECONDS);

        service[3].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/business/",
                "ru_rambler_news_business"), 600, 1200, TimeUnit.SECONDS);

        service[4].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/incidents/",
                "ru_rambler_news_incidents"), 800, 1200, TimeUnit.SECONDS);

        service[5].scheduleAtFixedRate(new Worker("http://news.rambler.ru/rss/USA/",
                "ru_rambler_news_USA"), 1000, 1200, TimeUnit.SECONDS);

    }
}
