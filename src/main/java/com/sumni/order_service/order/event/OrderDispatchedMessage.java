package com.sumni.order_service.order.event;

import java.util.UUID;

public record OrderDispatchedMessage(
        UUID orderId
) {
}
