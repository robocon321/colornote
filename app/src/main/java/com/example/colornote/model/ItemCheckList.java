package com.example.colornote.model;

import java.util.Date;

public class ItemCheckList {
    private int id;
    private String content;
    private boolean completed;
    private int parentId;
    private Date modifiedDate;
    private int status;
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ItemCheckList{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", completed=" + completed +
                ", parentId=" + parentId +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
