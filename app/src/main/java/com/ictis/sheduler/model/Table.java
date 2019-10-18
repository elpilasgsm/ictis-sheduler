
package com.ictis.sheduler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable
{

    private String type;
    private String name;
    private Integer week;
    private String group;
    private List<List<String>> table = new ArrayList<List<String>>();
    private final static long serialVersionUID = -7363018604500646766L;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<List<String>> getTable() {
        return table;
    }

    public void setTable(List<List<String>> table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "Table{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", week=" + week +
                ", group='" + group + '\'' +
                ", table=" + table +
                '}';
    }
}
