package com.ictis.sheduler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class IctisSelectorResponse implements Serializable
{

    @SerializedName("choices")
    @Expose
    private List<Choice> choices = null;
    private final static long serialVersionUID = -1383315497680543710L;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "IctisSelectorResponse{" +
                "choices=" + choices +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IctisSelectorResponse that = (IctisSelectorResponse) o;
        return Objects.equals(choices, that.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choices);
    }
}
