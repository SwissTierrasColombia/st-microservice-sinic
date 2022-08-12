package com.ai.st.microservice.sinic.modules.cycles.application.update_cycle;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleDoesNotExists;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public final class CycleUpdater implements CommandUseCase<CycleUpdaterCommand> {

    private final CycleRepository cycleRepository;

    public CycleUpdater(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public void handle(CycleUpdaterCommand command) {

        final var cycleId = CycleId.of(command.cycleId());
        final var amountPeriods = CycleAmountPeriods.of(command.periodsAmount());
        final var observations = CycleObservations.of(command.observations());
        final var status = CycleStatus.of(command.status());

        final var cycle = validateIfCycleExists(cycleId);

        List<Period> periods = List.of();
        if (!hasPeriodsChanged(cycle, amountPeriods)) {
            periods = cycle.periods();
        }

        if (hasStatusChanged(cycle, status) && status.value()) {
            findAllCyclesExcept(cycleId).forEach(cycle1 -> {
                final var c = cycle1.withStatus(CycleStatus.of(Boolean.FALSE));
                cycleRepository.update(c);
            });
        }

        final var cycleToUpdate = cycle.withStatus(status).withPeriods(periods).withObservations(observations)
                .withAmountPeriods(amountPeriods);

        cycleRepository.update(cycleToUpdate);
    }

    private Cycle validateIfCycleExists(CycleId cycleId) {
        return cycleRepository.searchBy(cycleId).orElseThrow(() -> new CycleDoesNotExists(cycleId.value()));
    }

    private boolean hasPeriodsChanged(Cycle cycle, CycleAmountPeriods amountPeriods) {
        return !Objects.equals(cycle.amountPeriods().value(), amountPeriods.value());
    }

    private boolean hasStatusChanged(Cycle cycle, CycleStatus status) {
        return !cycle.status().value().equals(status.value());
    }

    private List<Cycle> findAllCyclesExcept(CycleId cycleId) {
        return this.cycleRepository.find().stream().filter(cycle -> !cycle.id().equals(cycleId))
                .collect(Collectors.toList());
    }

}
