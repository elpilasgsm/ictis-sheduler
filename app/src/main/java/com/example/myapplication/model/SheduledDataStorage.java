package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheduledDataStorage implements Serializable {
   private List<SheduleData> storage =  new ArrayList<>();

    public List<SheduleData> getStorage() {
        return storage;
    }

    public void setStorage(List<SheduleData> storage) {
        this.storage = storage;
    }

    public SheduleData getByGroup (String group){
        return storage.stream().filter(it->it.getTable().getGroup().equals(group)).findAny().orElse(null);
    }
}
