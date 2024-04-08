package com.zosh.request;

import com.zosh.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long RestaurantId;
    private Address deliveryAddress;
}
