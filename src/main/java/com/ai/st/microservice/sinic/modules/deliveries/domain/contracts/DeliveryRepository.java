package com.ai.st.microservice.sinic.modules.deliveries.domain.contracts;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.sinic.modules.shared.domain.criteria.Criteria;

public interface DeliveryRepository {

    Delivery search(DeliveryId deliveryId);

    void save(Delivery delivery);

    PageableDomain<Delivery> matching(Criteria criteria);

    void remove(DeliveryId deliveryId);

    void update(Delivery delivery);

}
