package com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "AddPeriodsToCycleRequest")
public final class AddPeriodsToCycleRequest {

    @ApiModelProperty(required = true, notes = "List of periods")
    private List<PeriodRequest> periods;

    public List<PeriodRequest> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodRequest> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "AddPeriodsToCycleRequest{" + "periods=" + periods + '}';
    }
}
