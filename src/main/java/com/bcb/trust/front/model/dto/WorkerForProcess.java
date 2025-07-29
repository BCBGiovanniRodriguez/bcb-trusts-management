package com.bcb.trust.front.model.dto;

import java.time.LocalDateTime;

public class WorkerForProcess {
    
    private String subaccount;

    private int status;

    private String message;

    private LocalDateTime created;

    public WorkerForProcess() {
    }

    public String getSubaccount() {
        return subaccount;
    }

    public void setSubaccount(String subaccount) {
        this.subaccount = subaccount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "WorkerForProcess [subaccount=" + subaccount + ", status=" + status + ", message=" + message
                + ", created=" + created + "]";
    }
}
