package com.ictis.sheduler.model;

import com.ictis.sheduler.storage.DataHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheduledDataStorage implements Serializable {
    private List<SheduleData> storage = new ArrayList<>();

    public List<SheduleData> getStorage() {
        return storage;
    }

    public void setStorage(List<SheduleData> storage) {
        this.storage = storage;
    }

    public SheduleData getByGroup(String group) {
        return storage.stream().filter(it -> it.getTable().getGroup().equals(group)).findAny().orElse(null);
    }

    public SheduleData update(SheduleData data) {
        SheduleData storedVal = getByGroup(data.getTable().getGroup());
        if (storedVal == null) {
            getStorage().add(data);
        } else {
            storedVal.setTable(data.getTable());
            storedVal.setWeeks(data.getWeeks());
        }


        return data;
    }
}
