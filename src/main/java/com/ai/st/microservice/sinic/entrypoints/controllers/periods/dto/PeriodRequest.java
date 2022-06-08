package com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "PeriodRequest")
public final class PeriodRequest {

    @ApiModelProperty(required = true, notes = "Start date of the period (timestamp)")
    private long startDate;

    @ApiModelProperty(required = true, notes = "Finish date of the period(timestamp)")
    private long finishDate;

    private List<PeriodGroupRequest> groups;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

    public List<PeriodGroupRequest> getGroups() {
        return groups;
    }

    public void setGroups(List<PeriodGroupRequest> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "PeriodRequest{" + "startDate=" + startDate + ", finishDate=" + finishDate + ", groups=" + groups + '}';
    }
}