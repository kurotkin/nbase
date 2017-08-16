package com.kurotkin;

import java.io.*;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.kurotkin.Parser.getDoc;

/**
 * Created by Vitaly Kurotkin on 15.08.2017.
 */
public class GetNews {
    private static Logger log = Logger.getLogger(GetNews.class.getName());
    public static void main(String[] args) throws IOException {
        prepareLogging();
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
                log.info("Number " + i + " NULL");
                j--;
                if (j < 0)
                    return 0;
                continue;
            }
            j = 30;
            doc.append("guid", url);
            doc.append("number", i);
            log.info("Number " + i + " -------> load");
            mongo.update(doc);
            Thread.sleep(2000);
        }
        return 1;
    }

    public static void prepareLogging() {
        File loggingConfigurationFile = new File("logg.properties");
        if(!loggingConfigurationFile.exists()) {
            Writer output = null;
            try {
                output = new BufferedWriter(new FileWriter(loggingConfigurationFile));
                Properties logConf = new Properties();
                logConf.setProperty("handlers", "java.util.logging.FileHandler");
                logConf.setProperty("java.util.logging.FileHandler.pattern", "log.log");
                logConf.setProperty("java.util.logging.FileHandler.limit","50000");
                logConf.setProperty("java.util.logging.FileHandler.count", "5");
                logConf.setProperty("java.util.logging.FileHandler.formatter","java.util.logging.SimpleFormatter");
                logConf.setProperty("java.util.logging.SimpleFormatter.format","%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
                logConf.store(output, "Generated");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    output.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        Properties prop = System.getProperties();
        prop.setProperty("java.util.logging.config.file", "logg.properties");

        try {
            LogManager.getLogManager().readConfiguration();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
