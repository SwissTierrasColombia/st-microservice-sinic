package com.ai.st.microservice.sinic.modules.cycles.application.delete_cycle;

import com.ai.st.microservice.sinic.modules.cycles.domain.Cycle;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleRepository;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleDoesNotExists;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.UnauthorizedRemoveCycle;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class CycleRemover implements CommandUseCase<CycleRemoverCommand> {

    private final CycleRepository cycleRepository;

    public CycleRemover(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public void handle(CycleRemoverCommand command) {

        final var cycleId = CycleId.of(command.cycleId());

        final var cycle = validateIfCycleExists(cycleId);

        if (cycle.isActive()) {
            throw new UnauthorizedRemoveCycle(cycle.year().value());
        }

        cycleRepository.deleteBy(cycleId);
    }

    private Cycle validateIfCycleExists(CycleId cycleId) {
        return cycleRepository.searchBy(cycleId).orElseThrow(() -> new CycleDoesNotExists(cycleId.value()));
    }

}
