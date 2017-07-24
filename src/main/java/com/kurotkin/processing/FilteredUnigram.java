package com.kurotkin.processing;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaly Kurotkin on 21.07.2017.
 */
public class FilteredUnigram extends Unigram  {
    @Override
    public Set<String> getNGram(String text) {
        String[] words = clean(text).split("[ \n\t\r$+<>№=]");

        for (int i = 0; i < words.length; i++) {
            words[i] = PorterStemmer.doStem(words[i]);
        }

        Set<String> uniqueValues = new LinkedHashSet<>(Arrays.asList(words));
        uniqueValues.removeIf(s -> s.equals(""));

        return uniqueValues;
    }

    private String clean(String text) {
        if (text != null) {
            return text.toLowerCase().replaceAll("[\\pP\\d]", " ");
        } else {
            return "";
        }
    }
}
