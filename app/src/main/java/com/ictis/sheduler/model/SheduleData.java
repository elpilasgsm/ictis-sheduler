
package com.ictis.sheduler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheduleData implements Serializable
{

    private Table table;
    private List<Integer> weeks = new ArrayList<Integer>();
    private final static long serialVersionUID = 8485095960758442173L;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }


    @Override
    public String toString() {
        return "SheduleData{" +
                "table=" + table +
                ", weeks=" + weeks +
                '}';
    }
}
