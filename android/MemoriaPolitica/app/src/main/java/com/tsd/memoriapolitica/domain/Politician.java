package com.tsd.memoriapolitica.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tsd.memoriapolitica.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by PC on 29/06/2015.
 */
public class Politician implements Serializable, Comparable {

    private static final Logger LOGGER = Logger.getLogger(Politician.class.getName());

    private String cpf = Constants.NULL_CPF;
    private String party;
    private String civilName;
    private String politicianName;
    private String uf;
    private String email;
    private String status;
    private String photoPath;

    private QueryMatcher matcher;
    private String politicianNameWithNoAccents;


    public Politician(String theCpf) {
        this.cpf = theCpf;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPoliticianName() {
        return politicianName;
    }

    public void setPoliticianName(String thePoliticianName) {
        this.politicianName = thePoliticianName;
        this.politicianNameWithNoAccents = StringUtil.stripAccents(thePoliticianName);
        this.matcher = new QueryMatcher(politicianName, party, civilName, uf);
    }

    public String getPoliticianNameWithNoAccents() {
        return politicianNameWithNoAccents;
    }

    public String getPartyName() {
        return party;
    }

    public void setPartyName(String aParty) {
        this.party = aParty;
        this.matcher = new QueryMatcher(politicianName, party, civilName, uf);
    }

    public Bitmap getPhoto(Context context) throws IOException {
        // Lazy load to avoid memory problems.
        InputStream bitmapStream = context.getAssets().open(photoPath);
        Bitmap photo = BitmapFactory.decodeStream(bitmapStream);
        return photo;
    }

    public String toString() {
        return "CPF: " + cpf + "; Politician name: " + politicianName;
    }

    public void setPhotoPath(String aPhotoPath) {
        this.photoPath = aPhotoPath;
    }

    public boolean matchesQueryTerm(String... queryTerms) {
        return matcher.matches(queryTerms);
    }

    public void fillWithCandidateInfo(Politician candidate) {
        if (!this.cpf.equals(candidate.cpf)) {
            throw new IllegalArgumentException("O objeto candidate passado não tem o mesmo cpf.");
        }

        if (this.party == null) this.party = candidate.party;
        if (this.civilName == null) this.civilName = candidate.civilName;
        if (this.politicianName == null) this.politicianName = candidate.politicianName;
        if (this.uf == null) this.uf = candidate.uf;
        if (this.status == null) this.status = candidate.status;
        if (this.email == null) this.email = candidate.email;

        this.politicianNameWithNoAccents = StringUtil.stripAccents(this.politicianName);
        this.matcher = new QueryMatcher(politicianName, party, civilName, uf);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Politician that = (Politician) o;

        return cpf.equals(that.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }


    @Override
    public int compareTo(Object another) {
        if (!(another instanceof Politician)) {
            return 1;
        }
        Politician otherPol = (Politician) another;

        String thisName = this.getPoliticianNameWithNoAccents();
        String otherName = otherPol.getPoliticianNameWithNoAccents();

        if (thisName == null || otherName == null) {
            return 1;
        }

        return thisName.compareToIgnoreCase(otherName);
    }

    public String getCivilName() {
        return civilName;
    }

    public void setCivilName(String theCivilName) {
        this.civilName = theCivilName;
    }

    public String getPoliticianNameFirstLetter() {
        String polName = getPoliticianNameWithNoAccents();
        if (polName == null) {
            return "";
        }

        char letter = polName.charAt(0);
        return String.valueOf(letter);
    }
}