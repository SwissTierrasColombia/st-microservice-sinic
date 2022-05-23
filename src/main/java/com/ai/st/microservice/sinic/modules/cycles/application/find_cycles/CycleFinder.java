package com.ai.st.microservice.sinic.modules.cycles.application.find_cycles;

import com.ai.st.microservice.sinic.modules.cycles.application.CycleResponse;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleRepository;
import com.ai.st.microservice.sinic.modules.shared.application.ListResponse;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class CycleFinder implements QueryUseCase<CycleFinderQuery, ListResponse<CycleResponse>> {

    private final CycleRepository cycleRepository;

    public CycleFinder(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public ListResponse<CycleResponse> handle(CycleFinderQuery query) {
        return new ListResponse<>(
                cycleRepository.find().stream().map(CycleResponse::from).collect(Collectors.toList()));
    }

}
