package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa.CycleJPARepository;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.CycleEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostgresCycleRepository implements CycleRepository {

    private final CycleJPARepository cycleJPARepository;

    public PostgresCycleRepository(CycleJPARepository cycleJPARepository) {
        this.cycleJPARepository = cycleJPARepository;
    }

    @Override
    public Optional<Cycle> searchBy(CycleId cycleId) {
        CycleEntity cycleEntity = cycleJPARepository.findByUuid(cycleId.value());
        return Optional.ofNullable(cycleEntity).map(CycleMapper::from);
    }

    @Override
    public Optional<Cycle> searchBy(CycleYear cycleYear) {
        CycleEntity cycleEntity = cycleJPARepository.findByYear(cycleYear.value());
        return Optional.ofNullable(cycleEntity).map(CycleMapper::from);
    }

    @Override
    public List<Cycle> find() {
        return cycleJPARepository.findAll().stream().map(CycleMapper::from).collect(Collectors.toList());
    }

    @Override
    public void save(Cycle cycle) {
        CycleEntity cycleEntity = new CycleEntity();
        cycleEntity.setUuid(cycle.id().value());
        cycleEntity.setAmountPeriods(cycle.amountPeriods().value());
        cycleEntity.setObservations(cycle.observations().value());
        cycleEntity.setCreatedAt(new Date());
        cycleEntity.setYear(cycle.year().value());
        cycleJPARepository.save(cycleEntity);
    }

}
