package com.ai.st.microservice.sinic.modules.cycles.application.create_cycle;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleAlreadyExists;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class CycleCreator implements CommandUseCase<CycleCreatorCommand> {

    private final CycleRepository cycleRepository;

    public CycleCreator(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public void handle(CycleCreatorCommand command) {

        System.out.println("HELLOOOO");

        CycleYear year = CycleYear.of(command.year());
        CycleAmountPeriods amountPeriods = CycleAmountPeriods.of(command.amountPeriods());
        CycleObservations observations = CycleObservations.of(command.observations());

        validateIfCycleAlreadyExistsForYear(year);

        Cycle cycle = Cycle.create(year, observations, amountPeriods);

        cycleRepository.save(cycle);
    }

    private void validateIfCycleAlreadyExistsForYear(CycleYear year) {
        cycleRepository.searchBy(year).ifPresent(s -> {
            throw new CycleAlreadyExists(year.value());
        });
    }

}
