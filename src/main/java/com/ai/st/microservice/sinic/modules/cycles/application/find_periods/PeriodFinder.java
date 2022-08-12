package com.ai.st.microservice.sinic.modules.cycles.application.find_periods;

import com.ai.st.microservice.sinic.modules.cycles.application.PeriodResponse;
import com.ai.st.microservice.sinic.modules.cycles.domain.Cycle;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleRepository;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleDoesNotExists;
import com.ai.st.microservice.sinic.modules.shared.application.ListResponse;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class PeriodFinder implements QueryUseCase<PeriodFinderQuery, ListResponse<PeriodResponse>> {

    private final CycleRepository cycleRepository;

    public PeriodFinder(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public ListResponse<PeriodResponse> handle(PeriodFinderQuery query) {

        final var cycleId = CycleId.of(query.cycleId());

        final var cycle = searchCycle(cycleId);

        return new ListResponse<>(cycle.periods().stream().map(PeriodResponse::from).collect(Collectors.toList()));
    }

    private Cycle searchCycle(CycleId cycleId) {
        return cycleRepository.searchBy(cycleId).orElseThrow(() -> {
            throw new CycleDoesNotExists(cycleId.value());
        });
    }

}
