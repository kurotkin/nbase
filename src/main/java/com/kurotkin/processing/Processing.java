package com.kurotkin.processing;

import com.kurotkin.Mongo;

/**
 * Created by Vitaly Kurotkin on 24.07.2017.
 */
public class Processing {
    public static void main(String[] args) {
        Mongo mongo = new Mongo("10.0.0.1", "news", "ru_tinkoff_invest_news");
        mongo.create("ROSN", "Роснефт");
    }
}
