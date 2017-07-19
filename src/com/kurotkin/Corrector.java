package com.kurotkin;

/**
 * Created by Vitaly Kurotkin on 19.07.2017.
 *
 * db.getCollection('ru_tinkoff_invest_news').dropIndex({})
 * db.getCollection('ru_tinkoff_invest_news').createIndex({"description":"text"},{default_language:"russian"})
 * db.getCollection('ru_tinkoff_invest_news').find({$text:{$search:"Роснефт"}}).limit(20)
 */
public class Corrector {

}
