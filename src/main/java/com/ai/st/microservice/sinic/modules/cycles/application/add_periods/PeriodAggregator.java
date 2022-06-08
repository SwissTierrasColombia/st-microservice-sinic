package com.ai.st.microservice.sinic.modules.cycles.application.add_periods;

import com.ai.st.microservice.sinic.modules.cycles.domain.Cycle;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleRepository;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.AmountOfPeriodsInvalid;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleDoesNotExists;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.PeriodGroupDurationInvalid;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.PeriodId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.Duration;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.FinishDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.StartDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroup;
import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.contracts.GroupRepository;
import com.ai.st.microservice.sinic.modules.groups.domain.exceptions.GroupDoesNotExists;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PeriodAggregator implements CommandUseCase<PeriodAggregatorCommand> {

    private final CycleRepository cycleRepository;
    private final GroupRepository groupRepository;

    public PeriodAggregator(CycleRepository cycleRepository, GroupRepository groupRepository) {
        this.cycleRepository = cycleRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void handle(PeriodAggregatorCommand command) {

        final var cycleId = CycleId.of(command.cycleId());

        final var cycle = searchCycle(cycleId);

        validateNumberOfPeriods(cycle, command.periods().size());

        final var periods = mapperPeriods(cycleId, command.periods());
        cycle.setPeriods(periods);

        cycleRepository.update(cycle);
    }

    private void validateNumberOfPeriods(Cycle cycle, int periodsReceived) {
        final var amountPeriodsAllowed = cycle.amountPeriods().value();
        if (periodsReceived != amountPeriodsAllowed) {
            throw new AmountOfPeriodsInvalid(amountPeriodsAllowed);
        }
    }

    private Cycle searchCycle(CycleId cycleId) {
        return cycleRepository.searchBy(cycleId).orElseThrow(() -> {
            throw new CycleDoesNotExists(cycleId.value());
        });
    }

    private List<Period> mapperPeriods(CycleId cycleId, List<PeriodAggregatorCommand.Period> periods) {
        return periods.stream().map(periodDTO -> {
            final var periodId = PeriodId.generate();

            final var duration = Duration.create(StartDate.of(periodDTO.startDate()),
                    FinishDate.of(periodDTO.finishDate()));

            final var periodsGroup = mapperPeriodGroups(periodId, periodDTO.getGroups(), duration);

            return Period.create(periodId, cycleId, duration, periodsGroup);
        }).collect(Collectors.toList());
    }

    private List<PeriodGroup> mapperPeriodGroups(PeriodId periodId, List<PeriodAggregatorCommand.Group> groups,
            Duration periodDuration) {
        return groups.stream().map(group -> {
            final var duration = Duration.create(StartDate.of(group.startDate()), FinishDate.of(group.finishDate()));

            final var groupFound = searchGroup(GroupId.of(group.groupId()));

            final var periodGroup = PeriodGroup.create(groupFound.id(), periodId, duration);

            if (!periodGroup.isBetweenPeriodDuration(periodDuration)) {
                throw new PeriodGroupDurationInvalid();
            }

            return periodGroup;
        }).collect(Collectors.toList());
    }

    private Group searchGroup(GroupId groupId) {
        return groupRepository.search(groupId).orElseThrow(() -> {
            throw new GroupDoesNotExists(groupId.value());
        });
    }

}
