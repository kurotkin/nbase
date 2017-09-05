package com.kurotkin.api;

//import yahoofinance.Stock;
//import yahoofinance.YahooFinance;
//import yahoofinance.histquotes.HistoricalQuote;
//import yahoofinance.histquotes.Interval;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vitaly Kurotkin on 15.08.2017.
 */
public class Yahoo {
    // http://financequotes-api.com/

    public static void main(String[] args) throws IOException {
//        Stock stock = YahooFinance.get("INTC");
//
//        BigDecimal price = stock.getQuote().getPrice();
//        BigDecimal change = stock.getQuote().getChangeInPercent();
//        BigDecimal peg = stock.getStats().getPeg();
//        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
//
//        stock.print();
//
//        Stock tesla = YahooFinance.get("TSLA", true);
//        System.out.println(tesla.getHistory());

//        Calendar from = Calendar.getInstance();
//        Calendar to = Calendar.getInstance();
//        from.add(Calendar.YEAR, -5); // from 5 years ago
//
//        Stock google = YahooFinance.get("GOOG", from, to, Interval.WEEKLY);




        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -5); // from 1 year ago

        Stock google = YahooFinance.get("GOOG");
        List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);

        for(HistoricalQuote q : googleHistQuotes){

            System.out.print(pDate(q.getDate().getTime()));
            System.out.print(" ");
            System.out.print(q.getOpen());
            System.out.print(" ");
            System.out.print(q.getLow());
            System.out.print(" ");
            System.out.print(q.getHigh());
            System.out.print(" ");
            System.out.print(q.getClose());
            System.out.print(" ");
            System.out.print(q.getAdjClose());
            System.out.println(" ");
        }


    }

    public static String pDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(date);
    }
}
