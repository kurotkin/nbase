package com.kurotkin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) {

        ScheduledExecutorService [] service = new ScheduledExecutorService[6];

        for(int i = 0; i < service.length; i++) {
            service[i] = Executors.newSingleThreadScheduledExecutor();
        }

        service[0].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/head/",
                "ru_rambler_news_head"), 0, 900, TimeUnit.SECONDS);

        service[1].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/world/",
                "ru_rambler_news_world"), 100, 900, TimeUnit.SECONDS);

        service[2].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/politics/",
                "ru_rambler_news_politics"), 200, 900, TimeUnit.SECONDS);

        service[3].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/business/",
                "ru_rambler_news_business"), 300, 900, TimeUnit.SECONDS);

        service[4].scheduleAtFixedRate(new Worker("https://news.rambler.ru/rss/incidents/",
                "ru_rambler_news_incidents"), 400, 900, TimeUnit.SECONDS);

        service[5].scheduleAtFixedRate(new Worker("http://news.rambler.ru/rss/USA/",
                "ru_rambler_news_USA"), 500, 900, TimeUnit.SECONDS);

    }
}
