package com.booking.booking_room.dto.hotel;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelInforResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private Float latitude;
    private Float longitude;
    private Integer starRating;
    private Long totalRoom;
    private ContactInfor contact;
    private PolicyInfor policy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ContactInfor {

        private String phone;
        private String email;
        private String website;
        private String urlFacebook;
        private String urlInstagram;
        private String urlTwitter;
        private String urlYoutube;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PolicyInfor {

        private LocalDateTime checkInFrom;
        private LocalDateTime checkInTo;
        private LocalTime checkOutBefore;
        private Integer minAgeCheckin;
        private String petPolicy;
        private String smokingPolicy;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomInfor {

        private LocalDateTime checkInFrom;
        private LocalDateTime checkInTo;
        private LocalTime checkOutBefore;
        private Integer minAgeCheckin;
        private String petPolicy;
        private String smokingPolicy;
    }
}
