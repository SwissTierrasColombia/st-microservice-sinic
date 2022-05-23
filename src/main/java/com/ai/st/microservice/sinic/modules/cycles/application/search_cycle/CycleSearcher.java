package com.ai.st.microservice.sinic.modules.cycles.application.search_cycle;

import com.ai.st.microservice.sinic.modules.cycles.application.CycleResponse;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.CycleRepository;
import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.CycleDoesNotExists;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class CycleSearcher implements QueryUseCase<CycleSearcherQuery, CycleResponse> {

    private final CycleRepository cycleRepository;

    public CycleSearcher(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    @Override
    public CycleResponse handle(CycleSearcherQuery query) {
        return cycleRepository.searchBy(CycleId.of(query.id())).map(CycleResponse::from).orElseThrow(() -> {
            throw new CycleDoesNotExists(query.id());
        });
    }

}
