package com.example.top.dto.order;

import com.example.top.entity.order.Dimensions;
import com.example.top.entity.order.ServiceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDto {

    private ServiceType serviceType;

    private Dimensions dimensions;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity cannot be zero")
    private int quantity;

    @NotNull(message = "Printing charges cannot be null")
    @Min(value = 1, message = "Printing charges cannot be zero")
    private int printingCharges;

    @NotNull(message = "Service charges cannot be null")
    @Min(value = 1, message = "Service charges cannot be zero")
    private int serviceCharges;
}
