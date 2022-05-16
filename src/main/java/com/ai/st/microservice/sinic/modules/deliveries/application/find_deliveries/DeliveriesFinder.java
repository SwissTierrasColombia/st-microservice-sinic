package com.ai.st.microservice.sinic.modules.deliveries.application.find_deliveries;

import com.ai.st.microservice.sinic.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryCode;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.shared.application.PageableResponse;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;
import com.ai.st.microservice.sinic.modules.shared.domain.MunicipalityCode;
import com.ai.st.microservice.sinic.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.criteria.*;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.ErrorFromInfrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public final class DeliveriesFinder implements QueryUseCase<DeliveriesFinderQuery, PageableResponse<DeliveryResponse>> {

    private final DeliveryRepository repository;

    private final static int PAGE_DEFAULT = 1;
    private final static int LIMIT_DEFAULT = 10;

    public DeliveriesFinder(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageableResponse<DeliveryResponse> handle(DeliveriesFinderQuery query) {

        List<Filter> filters = new ArrayList<>();

        List<DeliveryStatus> defaultStatuses = (isCadastralAuthority(query.role()))
                ? Delivery.statusesAllowedToCadastralAuthority() : Delivery.statusesAllowedToManager();

        if (!query.states().isEmpty()) {
            filters.add(filterByStatus(query.states(), defaultStatuses));
        } else {
            filters.add(filterByStatus(
                    defaultStatuses.stream().map(status -> status.value().name()).collect(Collectors.toList()),
                    defaultStatuses));
        }

        String code = query.code();
        if (code != null && !code.isEmpty()) {
            filters.add(filterByCode(code));
        }

        String municipality = query.municipality();
        if (municipality != null && !municipality.isEmpty()) {
            filters.add(filterByMunicipality(municipality));
        }

        if (isManager(query.role())) {
            filters.add(filterByManager(query.entityCode()));
        }

        Long manager = query.manager();
        if (manager != null && isCadastralAuthority(query.role())) {
            filters.add(filterByManager(manager));
        }

        Criteria criteria = new Criteria(filters, Order.fromValues(Optional.of("deliveryDate"), Optional.of("DESC")),
                Optional.of(verifyPage(query.page())), Optional.of(verifyLimit(query.limit())));

        PageableDomain<Delivery> pageableDomain = repository.matching(criteria);

        return buildResponse(pageableDomain);
    }

    private int verifyPage(int page) {
        return (page <= 0) ? PAGE_DEFAULT : page;
    }

    private int verifyLimit(int limit) {
        return (limit <= 9) ? LIMIT_DEFAULT : limit;
    }

    private Filter filterByManager(Long managerCode) {
        FilterField filterField = new FilterField("manager");
        return new Filter(filterField, FilterOperator.EQUAL, new FilterValue(managerCode.toString()));
    }

    private Filter filterByStatus(List<String> statuses, List<DeliveryStatus> defaultStatuses) {

        List<String> statusesApproved = verifyStatus(statuses, defaultStatuses);

        return new Filter(new FilterField("deliveryStatus"), FilterOperator.CONTAINS,
                statusesApproved.stream().map(FilterValue::new).collect(Collectors.toList()));
    }

    private Filter filterByCode(String code) {
        return new Filter(new FilterField("code"), FilterOperator.EQUAL,
                new FilterValue(DeliveryCode.fromValue(code).value()));
    }

    private Filter filterByMunicipality(String municipality) {
        return new Filter(new FilterField("municipality"), FilterOperator.EQUAL,
                new FilterValue(MunicipalityCode.fromValue(municipality).value()));
    }

    private List<String> verifyStatus(List<String> statuses, List<DeliveryStatus> allows) {
        for (String state : statuses) {
            DeliveryStatus deliveryStatusString = allows.stream()
                    .filter(deliveryStatus -> deliveryStatus.value().name().equals(state)).findAny().orElse(null);
            if (deliveryStatusString == null) {
                statuses.remove(state);
            }
        }
        return statuses;
    }

    private PageableResponse<DeliveryResponse> buildResponse(PageableDomain<Delivery> pageableDomain) {

        if (!pageableDomain.numberOfElements().isPresent() || !pageableDomain.totalElements().isPresent()
                || !pageableDomain.totalPages().isPresent() || !pageableDomain.size().isPresent()
                || !pageableDomain.currentPage().isPresent()) {
            throw new ErrorFromInfrastructure();
        }

        List<DeliveryResponse> deliveriesResponse = pageableDomain.items().stream().map(DeliveryResponse::fromAggregate)
                .collect(Collectors.toList());

        return new PageableResponse<>(deliveriesResponse, pageableDomain.currentPage().get(),
                pageableDomain.numberOfElements().get(), pageableDomain.totalElements().get(),
                pageableDomain.totalPages().get(), pageableDomain.size().get());
    }

    private boolean isManager(Roles role) {
        return role.equals(Roles.MANAGER);
    }

    private boolean isCadastralAuthority(Roles role) {
        return role.equals(Roles.CADASTRAL_AUTHORITY);
    }

}
