package com.tekklabs.memoriapolitica.domain;

import com.tekklabs.memoriapolitica.gui.notebook.Approval;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by PC on 16/07/2015.
 */
public class CitizenNotebook implements Serializable {

    private Map<PoliticianClass, Set<PoliticianClassification>> approvedPolMap = new HashMap<>();
    private Map<PoliticianClass, Set<PoliticianClassification>> reprovedPolMap = new HashMap<>();
    private Map<PoliticianClass, Set<PoliticianClassification>> neutralPolMap = new HashMap<>();


    public CitizenNotebook() {
        approvedPolMap.put(PoliticianClass.FED_DEP, new TreeSet<PoliticianClassification>());
        //approvedPolMap.put(PoliticianClass.SENATORS, new TreeSet<PoliticianClassification>());
        reprovedPolMap.put(PoliticianClass.FED_DEP, new TreeSet<PoliticianClassification>());
        //reprovedPolMap.put(PoliticianClass.SENATORS, new TreeSet<PoliticianClassification>());
        neutralPolMap.put(PoliticianClass.FED_DEP, new TreeSet<PoliticianClassification>());
        //neutralPolMap.put(PoliticianClass.SENATORS, new TreeSet<PoliticianClassification>());
    }

    public Set<PoliticianClassification> getApprovedPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(approvedPolMap.get(polClass));
    }

    public Set<PoliticianClassification> getReprovedPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(reprovedPolMap.get(polClass));
    }

    public Set<PoliticianClassification> getNeutralPoliticians(PoliticianClass polClass) {
        return Collections.unmodifiableSet(neutralPolMap.get(polClass));
    }

    public void addApprovedPoliticians(PoliticianClass polClass, Collection<PoliticianClassification> polCollection) {
        for (PoliticianClassification pol : polCollection) {
            addApprovedPolitician(polClass, pol);
        }
    }

    public void addApprovedPolitician(PoliticianClass polClass, PoliticianClassification pol) {
        reprovedPolMap.get(polClass).remove(pol);
        neutralPolMap.get(polClass).remove(pol);

        pol.setApproval(Approval.APPROVED);
        approvedPolMap.get(polClass).add(pol);
    }

    public void addReprovedPoliticians(PoliticianClass polClass, Collection<PoliticianClassification> polCollection) {
        for (PoliticianClassification pol : polCollection) {
            addReprovedPolitician(polClass, pol);
        }
    }

    public void addReprovedPolitician(PoliticianClass polClass, PoliticianClassification pol) {
        approvedPolMap.get(polClass).remove(pol);
        neutralPolMap.get(polClass).remove(pol);

        pol.setApproval(Approval.REPROVED);
        reprovedPolMap.get(polClass).add(pol);
    }

    public void addNeutralPoliticians(PoliticianClass polClass, Collection<PoliticianClassification> polCollection) {
        for (PoliticianClassification pol : polCollection) {
            addNeutralPolitician(polClass, pol);
        }
    }

    public void addNeutralPolitician(PoliticianClass polClass, PoliticianClassification pol) {
        approvedPolMap.get(polClass).remove(pol);
        reprovedPolMap.get(polClass).remove(pol);

        pol.setApproval(Approval.NEUTRAL);
        neutralPolMap.get(polClass).add(pol);
    }

    public void setPoliticianClassification(PoliticianClassification polClassification) {
        for (PoliticianClassification aPolClass : approvedPolMap.get(PoliticianClass.FED_DEP)) {
            if (aPolClass.getPolitician().equals(polClassification.getPolitician())) {
                aPolClass.setApproval(polClassification.getApproval());
                aPolClass.setApprovalReason(polClassification.getReason());
                return;
            }
        }

        for (PoliticianClassification aPolClass : neutralPolMap.get(PoliticianClass.FED_DEP)) {
            if (aPolClass.getPolitician().equals(polClassification.getPolitician())) {
                aPolClass.setApproval(polClassification.getApproval());
                aPolClass.setApprovalReason(polClassification.getReason());
                return;
            }
        }

        for (PoliticianClassification aPolClass : reprovedPolMap.get(PoliticianClass.FED_DEP)) {
            if (aPolClass.getPolitician().equals(polClassification.getPolitician())) {
                aPolClass.setApproval(polClassification.getApproval());
                aPolClass.setApprovalReason(polClassification.getReason());
                return;
            }
        }
    }

    public Set<PoliticianClassification> getPoliticians(Approval theApproval, PoliticianClass politicianClass) {
        if (theApproval.equals(Approval.NEUTRAL)) {
            return getNeutralPoliticians(politicianClass);
        }
        else if (theApproval.equals(Approval.APPROVED)) {
            return getApprovedPoliticians(politicianClass);
        }
        else {
            return getReprovedPoliticians(politicianClass);
        }
    }
}