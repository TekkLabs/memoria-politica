package com.tsd.memoriapolitica.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by PC on 16/07/2015.
 */
public class CitizenNotebook implements Serializable {

    private Map<PoliticianClass, Set<Politician>> approvedPolMap = new HashMap<>();
    private Map<PoliticianClass, Set<Politician>> reprovedPolMap = new HashMap<>();
    private Map<PoliticianClass, Set<Politician>> neutralPolMap = new HashMap<>();


    public CitizenNotebook() {
        approvedPolMap.put(PoliticianClass.FED_DEP, new TreeSet<Politician>());
        approvedPolMap.put(PoliticianClass.SENATORS, new TreeSet<Politician>());
        reprovedPolMap.put(PoliticianClass.FED_DEP, new TreeSet<Politician>());
        reprovedPolMap.put(PoliticianClass.SENATORS, new TreeSet<Politician>());
        neutralPolMap.put(PoliticianClass.FED_DEP, new TreeSet<Politician>());
        neutralPolMap.put(PoliticianClass.SENATORS, new TreeSet<Politician>());
    }

    public Set<Politician> getApprovedPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(approvedPolMap.get(polClass));
    }

    public Set<Politician> getReprovedPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(reprovedPolMap.get(polClass));
    }

    public Set<Politician> getNeutralPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(neutralPolMap.get(polClass));
    }

    public void addApprovedPoliticians(PoliticianClass polClass, Collection<Politician> polCollection) {
        for (Politician pol : polCollection) {
            addApprovedPolitician(polClass, pol);
        }
    }

    public void addApprovedPolitician(PoliticianClass polClass, Politician pol) {
        reprovedPolMap.get(polClass).remove(pol);
        neutralPolMap.get(polClass).remove(pol);
        approvedPolMap.get(polClass).add(pol);
    }

    public void addReprovedPoliticians(PoliticianClass polClass, Collection<Politician> polCollection) {
        for (Politician pol : polCollection) {
            addReprovedPolitician(polClass, pol);
        }
    }

    public void addReprovedPolitician(PoliticianClass polClass, Politician pol) {
        approvedPolMap.get(polClass).remove(pol);
        neutralPolMap.get(polClass).remove(pol);
        reprovedPolMap.get(polClass).add(pol);
    }

    public void addNeutralPoliticians(PoliticianClass polClass, Collection<Politician> polCollection) {
        for (Politician pol : polCollection) {
            addNeutralPolitician(polClass, pol);
        }
    }

    public void addNeutralPolitician(PoliticianClass polClass, Politician pol) {
        approvedPolMap.get(polClass).remove(pol);
        reprovedPolMap.get(polClass).remove(pol);
        neutralPolMap.get(polClass).add(pol);
    }
}
