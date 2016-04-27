package com.tekklabs.memoriapolitica.domain;

import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByApproval;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByParty;
import com.tekklabs.memoriapolitica.gui.SortMode;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by taciosd on 4/22/16.
 */
public class ComparePoliticiansByApprovalTest {

    @Test
    public void testCompareAskingApprovedFirst() {
        ComparePoliticianByApproval comparator = new ComparePoliticianByApproval(SortMode.BY_APPROVED_FIRST);

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol1.setPoliticianName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);
        polClassification1.setApproval(Approval.NEUTRAL);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol2.setPoliticianName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);
        polClassification2.setApproval(Approval.APPROVED);

        Politician pol3 = new Politician("11220470609");
        pol3.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol3.setPoliticianName("Abel");
        PoliticianClassification polClassification3 = new PoliticianClassification(pol3);
        polClassification3.setApproval(Approval.REPROVED);

        Assert.assertEquals(1, comparator.compare(polClassification1, polClassification2));
        Assert.assertEquals(-1, comparator.compare(polClassification2, polClassification1));
        Assert.assertEquals(-1, comparator.compare(polClassification1, polClassification3));
        Assert.assertEquals(1, comparator.compare(polClassification3, polClassification1));
        Assert.assertEquals(-2, comparator.compare(polClassification2, polClassification3));
        Assert.assertEquals(2, comparator.compare(polClassification3, polClassification2));
        Assert.assertEquals(0, comparator.compare(polClassification2, polClassification2));
    }

    @Test
    public void testCompareAskingNeutralFirst() {
        ComparePoliticianByApproval comparator = new ComparePoliticianByApproval(SortMode.BY_NEUTRAL_FIRST);

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol1.setPoliticianName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);
        polClassification1.setApproval(Approval.APPROVED);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol2.setPoliticianName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);
        polClassification2.setApproval(Approval.NEUTRAL);

        Politician pol3 = new Politician("11220470609");
        pol3.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol3.setPoliticianName("Abel");
        PoliticianClassification polClassification3 = new PoliticianClassification(pol3);
        polClassification3.setApproval(Approval.REPROVED);

        Assert.assertEquals(1, comparator.compare(polClassification1, polClassification2));
        Assert.assertEquals(-1, comparator.compare(polClassification2, polClassification1));
        Assert.assertEquals(-1, comparator.compare(polClassification1, polClassification3));
        Assert.assertEquals(1, comparator.compare(polClassification3, polClassification1));
        Assert.assertEquals(-2, comparator.compare(polClassification2, polClassification3));
        Assert.assertEquals(2, comparator.compare(polClassification3, polClassification2));
        Assert.assertEquals(0, comparator.compare(polClassification2, polClassification2));
    }

    @Test
    public void testCompareAskingReprovedFirst() {
        ComparePoliticianByApproval comparator = new ComparePoliticianByApproval(SortMode.BY_REPROVED_FIRST);

        Politician pol1 = new Politician("10230450609");
        pol1.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol1.setPoliticianName("Abel");
        PoliticianClassification polClassification1 = new PoliticianClassification(pol1);
        polClassification1.setApproval(Approval.APPROVED);

        Politician pol2 = new Politician("50230450610");
        pol2.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol2.setPoliticianName("Abel");
        PoliticianClassification polClassification2 = new PoliticianClassification(pol2);
        polClassification2.setApproval(Approval.REPROVED);

        Politician pol3 = new Politician("11220470609");
        pol3.setParty(new Party(13, "PT", "PARTIDO DO T"));
        pol3.setPoliticianName("Abel");
        PoliticianClassification polClassification3 = new PoliticianClassification(pol3);
        polClassification3.setApproval(Approval.NEUTRAL);

        Assert.assertEquals(1, comparator.compare(polClassification1, polClassification2));
        Assert.assertEquals(-1, comparator.compare(polClassification2, polClassification1));
        Assert.assertEquals(-1, comparator.compare(polClassification1, polClassification3));
        Assert.assertEquals(1, comparator.compare(polClassification3, polClassification1));
        Assert.assertEquals(-2, comparator.compare(polClassification2, polClassification3));
        Assert.assertEquals(2, comparator.compare(polClassification3, polClassification2));
        Assert.assertEquals(0, comparator.compare(polClassification2, polClassification2));
    }
}
