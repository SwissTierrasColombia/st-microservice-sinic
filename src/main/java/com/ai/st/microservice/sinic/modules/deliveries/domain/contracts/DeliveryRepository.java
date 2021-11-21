package com.ai.st.microservice.sinic.modules.deliveries.domain.contracts;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;

public interface DeliveryRepository {

    void save(Delivery delivery);

}
