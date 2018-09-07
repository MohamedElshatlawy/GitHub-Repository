package com.example.mohamed.git_task;

import java.io.Serializable;

public class RepoModel implements Serializable {
    private String repName;
    private String repOwner;
    private String repDesc;
    private boolean forkFlag;
    private String rep_url;
    private String owner_url;

    public RepoModel(String repName, String repOwner, String repDesc, boolean forkFlag, String rep_url, String owner_url) {
        this.repName = repName;
        this.repOwner = repOwner;
        this.repDesc = repDesc;
        this.forkFlag = forkFlag;
        this.rep_url = rep_url;
        this.owner_url = owner_url;
    }

    public String getRep_url() {
        return rep_url;
    }

    public void setRep_url(String rep_url) {
        this.rep_url = rep_url;
    }

    public String getOwner_url() {
        return owner_url;
    }

    public void setOwner_url(String owner_url) {
        this.owner_url = owner_url;
    }

    public boolean isForkFlag() {
        return forkFlag;
    }

    public void setForkFlag(boolean forkFlag) {
        this.forkFlag = forkFlag;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getRepOwner() {
        return repOwner;
    }

    public void setRepOwner(String repOwner) {
        this.repOwner = repOwner;
    }

    public String getRepDesc() {
        return repDesc;
    }

    public void setRepDesc(String repDesc) {
        this.repDesc = repDesc;
    }
}
