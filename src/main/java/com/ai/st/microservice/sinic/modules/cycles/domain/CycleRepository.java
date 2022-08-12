package com.ai.st.microservice.sinic.modules.cycles.domain;

import java.util.List;
import java.util.Optional;

public interface CycleRepository {

    Optional<Cycle> searchBy(CycleId id);

    Optional<Cycle> searchBy(CycleYear year);

    List<Cycle> find();

    void save(Cycle cycle);

    void update(Cycle cycle);

    void deleteBy(CycleId cycleId);

}
