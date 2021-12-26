package com.ai.st.microservice.sinic.modules.deliveries.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.infrastructure.persistence.jpa.DeliveryJPARepository;
import com.ai.st.microservice.sinic.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.sinic.modules.shared.domain.criteria.*;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public final class PostgresDeliveryRepository implements DeliveryRepository {

    private final DeliveryJPARepository deliveryJPARepository;

    public static final Map<String, String> MAPPING_FIELDS = new HashMap<>(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>("deliveryDate", "createdAt"),
                    new AbstractMap.SimpleEntry<>("deliveryStatus", "status"),
                    new AbstractMap.SimpleEntry<>("manager", "managerCode"),
                    new AbstractMap.SimpleEntry<>("code", "code"),
                    new AbstractMap.SimpleEntry<>("municipality", "municipalityCode")
            ));

    public PostgresDeliveryRepository(DeliveryJPARepository deliveryJPARepository) {
        this.deliveryJPARepository = deliveryJPARepository;
    }

    @Override
    public Delivery search(DeliveryId deliveryId) {
        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(deliveryId.value()).orElse(null);
        return (deliveryEntity == null) ? null : mapping(deliveryEntity);
    }

    @Override
    public void save(Delivery delivery) {

        DeliveryEntity deliveryEntity = new DeliveryEntity();

        deliveryEntity.setCode(delivery.code().value());
        deliveryEntity.setCreatedAt(delivery.date().value());
        deliveryEntity.setDateStatusAt(delivery.dateStatus().value());
        deliveryEntity.setDepartmentName(delivery.locality().department().value());
        deliveryEntity.setManagerCode(delivery.manager().code().value());
        deliveryEntity.setManagerName(delivery.manager().name().value());
        deliveryEntity.setMunicipalityCode(delivery.locality().code().value());
        deliveryEntity.setMunicipalityName(delivery.locality().municipality().value());
        deliveryEntity.setObservations(delivery.observations().value());
        deliveryEntity.setStatus(mappingEnum(delivery.status()));
        deliveryEntity.setUserCode(delivery.user().value());

        deliveryJPARepository.save(deliveryEntity);
    }

    private DeliveryStatusEnum mappingEnum(DeliveryStatus status) {
        switch (status.value()) {
            case SENT_CADASTRAL_AUTHORITY:
                return DeliveryStatusEnum.SENT_CADASTRAL_AUTHORITY;
            case IN_QUEUE_TO_IMPORT:
                return DeliveryStatusEnum.IN_QUEUE_TO_IMPORT;
            case IMPORTING:
                return DeliveryStatusEnum.IMPORTING;
            case SUCCESS_IMPORT:
                return DeliveryStatusEnum.SUCCESS_IMPORT;
            case FAILED_IMPORT:
                return DeliveryStatusEnum.FAILED_IMPORT;
            case DRAFT:
            default:
                return DeliveryStatusEnum.DRAFT;
        }
    }

    @Override
    public PageableDomain<Delivery> matching(Criteria criteria) {
        List<DeliveryEntity> deliveryEntities;
        Page<DeliveryEntity> page = null;

        if (criteria.hasFilters()) {

            Specification<DeliveryEntity> specification = addFilters(criteria.filters());

            if (criteria.hasPagination()) {
                Pageable pageable = addPageable(criteria.page().get(), criteria.limit().get(), criteria.order());
                page = deliveryJPARepository.findAll(specification, pageable);
                deliveryEntities = page.getContent();
            } else {
                if (criteria.order().hasOrder()) {
                    Sort sort = addSorting(criteria.order().orderBy(), criteria.order().orderType());
                    deliveryEntities = deliveryJPARepository.findAll(specification, sort);
                } else {
                    deliveryEntities = deliveryJPARepository.findAll(specification);
                }
            }

        } else {
            if (criteria.hasPagination()) {
                Pageable pageable = addPageable(criteria.page().get(), criteria.limit().get(), criteria.order());
                page = deliveryJPARepository.findAll(pageable);
                deliveryEntities = page.getContent();
            } else {
                if (criteria.order().hasOrder()) {
                    Sort sort = addSorting(criteria.order().orderBy(), criteria.order().orderType());
                    deliveryEntities = deliveryJPARepository.findAll(sort);
                } else {
                    deliveryEntities = deliveryJPARepository.findAll();
                }
            }
        }

        List<Delivery> deliveries = deliveryEntities.stream().map(this::mapping).collect(Collectors.toList());

        return new PageableDomain<>(
                deliveries,
                page != null ? Optional.of(page.getNumber() + 1) : Optional.empty(),
                page != null ? Optional.of(page.getNumberOfElements()) : Optional.empty(),
                page != null ? Optional.of(page.getTotalElements()) : Optional.empty(),
                page != null ? Optional.of(page.getTotalPages()) : Optional.empty(),
                page != null ? Optional.of(page.getSize()) : Optional.empty()
        );
    }

    @Override
    public void remove(DeliveryId deliveryId) {
        deliveryJPARepository.deleteById(deliveryId.value());
    }

    @Override
    public void update(Delivery delivery) {
        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(delivery.id().value()).orElse(null);
        if (deliveryEntity != null) {
            deliveryEntity.setObservations(delivery.observations().value());
            deliveryJPARepository.save(deliveryEntity);
        }
    }

    @Override
    public void changeStatus(DeliveryId deliveryId, DeliveryStatus status) {

        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(deliveryId.value()).orElse(null);
        if (deliveryEntity != null) {

            deliveryEntity.setStatus(mappingEnum(status));
            deliveryEntity.setDateStatusAt(new Date());

            deliveryJPARepository.save(deliveryEntity);
        }

    }

    private Specification<DeliveryEntity> addFilters(List<Filter> filters) {
        Specification<DeliveryEntity> specification = where(createSpecification(filters.remove(0)));
        for (Filter filter : filters) {
            specification = specification != null ? specification.and(createSpecification(filter)) : null;
        }
        return specification;
    }

    private Sort addSorting(OrderBy orderBy, OrderType orderType) {
        Sort sort = Sort.by(mappingField(orderBy.value()));
        sort = (orderType.isAsc()) ? sort.ascending() : sort.descending();
        return sort;
    }

    private Pageable addPageable(int page, int limit, Order order) {
        Pageable pageable;
        int numberPage = page - 1;
        if (order.hasOrder()) {
            Sort sort = addSorting(order.orderBy(), order.orderType());
            pageable = PageRequest.of(numberPage, limit, sort);
        } else {
            pageable = PageRequest.of(numberPage, limit);
        }
        return pageable;
    }

    private Specification<DeliveryEntity> createSpecification(Filter filter) {
        try {
            switch (filter.operator()) {
                case EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(buildPath(root, filter.field().value()), filter.value().value());
                case NOT_EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.notEqual(buildPath(root, filter.field().value()), filter.value().value());
                case CONTAINS:
                    return (root, query, criteriaBuilder) -> {
                        List<String> list = filter.values().stream().map(FilterValue::value).collect(Collectors.toList());
                        return buildPath(root, filter.field().value()).as(String.class).in(list);
                    };
                default:
                    throw new OperatorUnsupported();
            }
        } catch (Exception e) {
            throw new FieldUnsupported();
        }
    }

    private Path<?> buildPath(Root<DeliveryEntity> root, String fieldDomain) {
        String field = mappingField(fieldDomain);
        return root.get(field);
    }

    private String mappingField(String fieldDomain) {
        return MAPPING_FIELDS.get(fieldDomain);
    }

    private Delivery mapping(DeliveryEntity deliveryEntity) {
        return Delivery.fromPrimitives(
                deliveryEntity.getId(),
                deliveryEntity.getCode(),
                deliveryEntity.getCreatedAt(),
                deliveryEntity.getDateStatusAt(),
                deliveryEntity.getManagerCode(),
                deliveryEntity.getManagerName(),
                deliveryEntity.getDepartmentName(),
                deliveryEntity.getMunicipalityName(),
                deliveryEntity.getMunicipalityCode(),
                deliveryEntity.getObservations(),
                deliveryEntity.getStatus().name(),
                deliveryEntity.getUserCode()
        );
    }

}
