package com.tsd.memoriapolitica.domain;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 10/07/2015.
 */
public class QueryMatcher implements Serializable {

    private List<String> entityTerms;

    public QueryMatcher(final String... theEntityTerms) {
        this.entityTerms = processEntityTerms(theEntityTerms);
    }

    private List<String> processEntityTerms(String[] terms) {
        List<String> generatedTerms = new ArrayList<>();

        if (terms.length == 1) {
            String term = terms[0];
            if (term != null) {
                term = getNonAccentTerm(term);
                generatedTerms.add(term.toUpperCase());
            }
        }
        else {
            for (String term : terms) {
                if (term == null) {
                    continue;
                }

                String[] splitTerms = term.split(" ");
                generatedTerms.addAll(processEntityTerms(splitTerms));
            }
        }

        return generatedTerms;
    }

    private String getNonAccentTerm(String term) {
        String newTerm = Normalizer.normalize(term, Normalizer.Form.NFD);
        newTerm = newTerm.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return newTerm;
    }

    public boolean matches(String... queryTerms) {
        for (String queryTerm : queryTerms) {
            for (String entityTerm : entityTerms) {
                if (entityTerm.contains(queryTerm.toUpperCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}
