package com.booking.booking_room.dto.hotel;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private BigDecimal price;
    private String roomTypeName;
    private Integer maxOccupancy;
    private Integer totalReviews;
}
