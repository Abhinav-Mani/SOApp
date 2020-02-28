package com.maxpetroleum.soapp.Model;

import java.io.Serializable;

public class Dealer implements Comparable<Dealer> , Serializable {
    String uid;
    long pendingRequests;

    public Dealer() {
    }

    public Dealer(String uid, long pendingRequests) {
        this.uid = uid;
        this.pendingRequests = pendingRequests;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public int compareTo(Dealer dealer) {
        return (int)(this.pendingRequests-dealer.pendingRequests);
    }
}
