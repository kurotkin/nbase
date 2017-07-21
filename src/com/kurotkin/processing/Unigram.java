package com.kurotkin.processing;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaly Kurotkin on 21.07.2017.
 */
public class Unigram {
    public Set<String> getNGram(String text) {
        if (text == null) {
            text = "";
        }
        String[] words = text.toLowerCase().split("[ \\pP\n\t\r$+<>№=]");

        Set<String> uniqueValues = new LinkedHashSet<>(Arrays.asList(words));
        uniqueValues.removeIf(s -> s.equals(""));

        return uniqueValues;
    }
}
