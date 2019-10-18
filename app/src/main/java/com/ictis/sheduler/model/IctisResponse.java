package com.ictis.sheduler.model;

import java.io.Serializable;

public class IctisResponse implements Serializable {
    private IctisSelectorResponse ictisSelectorResponse;
    private SheduleData scheduledData;

    public IctisSelectorResponse getIctisSelectorResponse() {
        return ictisSelectorResponse;
    }

    public void setIctisSelectorResponse(IctisSelectorResponse ictisSelectorResponse) {
        this.ictisSelectorResponse = ictisSelectorResponse;
    }

    public SheduleData getScheduledData() {
        return scheduledData;
    }

    public void setScheduledData(SheduleData scheduledData) {
        this.scheduledData = scheduledData;
    }
}
