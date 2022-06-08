package com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PeriodGroupRequest")
public final class PeriodGroupRequest {

    @ApiModelProperty(required = true, notes = "Group ID")
    private String groupId;

    @ApiModelProperty(required = true, notes = "Start date of the group (timestamp)")
    private long startDate;

    @ApiModelProperty(required = true, notes = "Finish date of the group(timestamp)")
    private long finishDate;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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

    @Override
    public String toString() {
        return "PeriodGroupRequest{" + "groupId='" + groupId + '\'' + ", startDate=" + startDate + ", finishDate="
                + finishDate + '}';
    }
}