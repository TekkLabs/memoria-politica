package com.tekklabs.memoriapolitica.domain;

import android.test.suitebuilder.annotation.SmallTest;

import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByParty;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by taciosd on 4/22/16.
 */
public class ComparePoliticiansByPartyTest {

    @Test
    public void testCompareSamePartyAndSameNamePoliticians() {
        ComparePoliticianByParty comparator = new ComparePoliticianByParty();

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(22, "PT", "ZZ"));
        pol1.setPoliticianName("Abel");
        pol1.setCivilName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PT", "AA"));
        pol2.setPoliticianName("Abel");
        pol2.setCivilName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);

        Assert.assertEquals(0, comparator.compare(polClassification1, polClassification2));
        Assert.assertEquals(0, comparator.compare(polClassification2, polClassification1));
    }

    @Test
    public void testCompareDifferentPartyPoliticians() {
        ComparePoliticianByParty comparator = new ComparePoliticianByParty();

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(22, "PP", "ZZ"));
        pol1.setPoliticianName("Abel");
        pol1.setCivilName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PT", "AA"));
        pol2.setPoliticianName("Abel");
        pol2.setCivilName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);

        Assert.assertTrue(comparator.compare(polClassification1, polClassification2) < 0);
        Assert.assertTrue(comparator.compare(polClassification2, polClassification1) > 0);
    }

    @Test
    public void testCompareSamePartyDifferentNamePoliticians() {
        ComparePoliticianByParty comparator = new ComparePoliticianByParty();

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(22, "PP", "ZZ"));
        pol1.setPoliticianName("Abel");
        pol1.setCivilName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PP", "AA"));
        pol2.setPoliticianName("Rodrigo");
        pol2.setCivilName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);

        Assert.assertTrue(comparator.compare(polClassification1, polClassification2) < 0);
        Assert.assertTrue(comparator.compare(polClassification2, polClassification1) > 0);
    }
}
