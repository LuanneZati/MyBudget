package com.example.minhacarteira;

import java.util.UUID;

public class Budget {
    private UUID mUUID;
    private String mData;
    private String mValue;
    private String mDescription;

    public Budget(String data, String value, String description) {
        mData = data;
        mValue = value;
        mDescription = description;
        mUUID = UUID.randomUUID();
    }

    public Budget(UUID uuid, String data, String value, String description) {
        mData = data;
        mValue = value;
        mDescription = description;
        mUUID = uuid;
    }

    public String getData() {
        return mData;
    }

    public String getValue() {
        return mValue;
    }

    public String getDescription() {
        return mDescription;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID uuid) {
        mUUID = uuid;
    }

    public void setData(String data) {
        mData = data;
    }

    public void setTime(String value) {
        mValue = value;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
