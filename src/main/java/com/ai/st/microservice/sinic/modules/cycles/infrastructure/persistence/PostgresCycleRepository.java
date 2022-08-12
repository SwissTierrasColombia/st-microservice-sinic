package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa.CycleJPARepository;
import com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa.PeriodGroupJPARepository;
import com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa.PeriodJPARepository;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.CycleEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodGroupEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostgresCycleRepository implements CycleRepository {

    private final CycleJPARepository cycleJPARepository;
    private final PeriodJPARepository periodJPARepository;
    private final PeriodGroupJPARepository periodGroupJPARepository;

    public PostgresCycleRepository(CycleJPARepository cycleJPARepository, PeriodJPARepository periodJPARepository,
            PeriodGroupJPARepository periodGroupJPARepository) {
        this.cycleJPARepository = cycleJPARepository;
        this.periodJPARepository = periodJPARepository;
        this.periodGroupJPARepository = periodGroupJPARepository;
    }

    @Override
    public Optional<Cycle> searchBy(CycleId cycleId) {
        CycleEntity cycleEntity = cycleJPARepository.findByUuid(cycleId.value());
        return Optional.ofNullable(cycleEntity).map(cycle -> {
            cycle.setPeriods(findPeriodsByCycle(cycle.getUuid()));
            return CycleMapper.from(cycle);
        });
    }

    @Override
    public Optional<Cycle> searchBy(CycleYear cycleYear) {
        CycleEntity cycleEntity = cycleJPARepository.findByYear(cycleYear.value());
        return Optional.ofNullable(cycleEntity).map(cycle -> {
            cycle.setPeriods(findPeriodsByCycle(cycle.getUuid()));
            return CycleMapper.from(cycle);
        });
    }

    @Override
    public List<Cycle> find() {
        return cycleJPARepository.findAll(Sort.by(Sort.Direction.ASC, "year")).stream().map(cycle -> {
            cycle.setPeriods(findPeriodsByCycle(cycle.getUuid()));
            return CycleMapper.from(cycle);
        }).collect(Collectors.toList());
    }

    @Override
    public void save(Cycle cycle) {
        CycleEntity cycleEntity = new CycleEntity();
        cycleEntity.setUuid(cycle.id().value());
        cycleEntity.setAmountPeriods(cycle.amountPeriods().value());
        cycleEntity.setObservations(cycle.observations().value());
        cycleEntity.setCreatedAt(new Date());
        cycleEntity.setYear(cycle.year().value());
        cycleEntity.setStatus(cycle.status().value());
        cycleJPARepository.save(cycleEntity);
    }

    @Transactional
    @Override
    public void update(Cycle cycle) {
        CycleEntity cycleEntity = cycleJPARepository.findByUuid(cycle.id().value());
        if (cycleEntity != null) {
            cycleEntity.setObservations(cycle.observations().value());
            cycleEntity.setAmountPeriods(cycle.amountPeriods().value());
            cycleEntity.setStatus(cycle.status().value());
            deletePeriods(cycle.id().value());
            if (!cycle.periods().isEmpty()) {
                cycle.periods().forEach(this::createPeriod);
            }
        }
    }

    @Transactional
    @Override
    public void deleteBy(CycleId cycleId) {
        CycleEntity cycleEntity = cycleJPARepository.findByUuid(cycleId.value());
        if (cycleEntity != null) {
            deletePeriods(cycleId.value());
            cycleJPARepository.deleteById(cycleEntity.getId());
        }
    }

    private void deletePeriods(String cycleId) {
        final var periodsEntity = findPeriodsByCycle(cycleId);
        periodsEntity.forEach(periodEntity -> deletePeriodsGrupoByPeriod(periodEntity.getUuid()));
        deletePeriodsByCycle(cycleId);
    }

    private List<PeriodEntity> findPeriodsByCycle(String cycleId) {
        return periodJPARepository.findByCycleId(cycleId).stream().peek(periodEntity -> {
            final var groups = periodGroupJPARepository.findByPeriodId(periodEntity.getUuid());
            periodEntity.setPeriodGroups(groups);
        }).collect(Collectors.toList());
    }

    private void deletePeriodsGrupoByPeriod(String periodId) {
        long affected = periodGroupJPARepository.deleteByPeriodId(periodId);
        System.out.println(String.format("PERIODS GROUPS DELETED: %d PERIOD --> %s", affected, periodId));
    }

    private void deletePeriodsByCycle(String cycleId) {
        long affected = periodJPARepository.deleteByCycleId(cycleId);
        System.out.println("PERIODS DELETED: " + affected);
    }

    private void createPeriod(Period period) {
        PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setUuid(period.id().value());
        periodEntity.setCycleId(period.cycleId().value());
        periodEntity.setDateStart(period.duration().start().value());
        periodEntity.setDateFinish(period.duration().finish().value());
        periodEntity.setCreatedAt(new Date());
        periodJPARepository.save(periodEntity);

        period.periodGroups().forEach(periodGroup -> {
            PeriodGroupEntity periodGroupEntity = new PeriodGroupEntity();
            periodGroupEntity.setUuid(periodGroup.id().value());
            periodGroupEntity.setPeriodId(periodGroup.periodId().value());
            periodGroupEntity.setDateStart(periodGroup.duration().start().value());
            periodGroupEntity.setDateFinish(periodGroup.duration().finish().value());
            periodGroupEntity.setGroupId(periodGroup.groupId().value());
            periodGroupJPARepository.save(periodGroupEntity);
        });
    }

}
