package com.booking.booking_room.dto.auth;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleUserInfo {

    private String email;
    private String name;
    private boolean emailVerified;
}
