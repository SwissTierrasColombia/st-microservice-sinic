package com.ai.st.microservice.sinic.modules.cycles.application.add_periods;

import com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto.AddPeriodsToCycleRequest;
import com.ai.st.microservice.sinic.modules.shared.application.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodAggregatorCommand implements Command {

    private final String cycleId;
    private final List<Period> periods;

    public PeriodAggregatorCommand(String cycleId, List<Period> periods) {
        this.cycleId = cycleId;
        this.periods = periods;
    }

    public String cycleId() {
        return cycleId;
    }

    public List<Period> periods() {
        return periods;
    }

    public static PeriodAggregatorCommand from(String cycleId, AddPeriodsToCycleRequest dto) {

        final var periods = dto.getPeriods().stream().map(periodRequest -> {

            final var groups = periodRequest.getGroups().stream()
                    .map(periodGroupRequest -> new Group(periodGroupRequest.getGroupId(),
                            periodGroupRequest.getStartDate(), periodGroupRequest.getFinishDate()))
                    .collect(Collectors.toList());

            return new Period(periodRequest.getStartDate(), periodRequest.getFinishDate(), groups);
        }).collect(Collectors.toList());

        return new PeriodAggregatorCommand(cycleId, periods);
    }

    static class Period {

        private final long startDate;
        private final long finishDate;
        private final List<PeriodAggregatorCommand.Group> groups;

        public Period(long startDate, long finishDate, List<Group> groups) {
            this.startDate = startDate;
            this.finishDate = finishDate;
            this.groups = groups;
        }

        public long startDate() {
            return startDate;
        }

        public long finishDate() {
            return finishDate;
        }

        public List<Group> getGroups() {
            return groups;
        }
    }

    static class Group {

        private final String groupId;
        private final long startDate;
        private final long finishDate;

        public Group(String groupId, long startDate, long finishDate) {
            this.groupId = groupId;
            this.startDate = startDate;
            this.finishDate = finishDate;
        }

        public String groupId() {
            return groupId;
        }

        public long startDate() {
            return startDate;
        }

        public long finishDate() {
            return finishDate;
        }
    }

}