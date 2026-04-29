package com.booking.booking_room.service.hotel;

import org.springframework.stereotype.Service;

import com.booking.booking_room.dto.hotel.HotelInforResponse;
import com.booking.booking_room.entity.information.Contact;
import com.booking.booking_room.entity.information.Information;
import com.booking.booking_room.entity.information.Policy;
import com.booking.booking_room.repository.InformationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    public HotelInforResponse getHotelInformation() {
        Information information = informationRepository.findFirstByIsDeletedFalseOrderByIdAsc()
                .orElseThrow(() -> new EntityNotFoundException("Hotel information not found"));

        return HotelInforResponse.builder()
                .id(information.getId())
                .name(information.getName())
                .description(information.getDescription())
                .address(information.getAddress())
                .latitude(information.getLatitude())
                .longitude(information.getLongitude())
                .starRating(information.getStarRating())
                .totalRoom(information.getTotalRoom())
                .contact(toContactInfor(information.getContact()))
                .policy(toPolicyInfor(information.getPolicy()))
                .build();
    }

    private HotelInforResponse.ContactInfor toContactInfor(Contact contact) {
        if (contact == null) {
            return null;
        }

        return HotelInforResponse.ContactInfor.builder()
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .website(contact.getWebsite())
                .urlFacebook(contact.getUrlFacebook())
                .urlInstagram(contact.getUrlInstagram())
                .urlTwitter(contact.getUrlTwitter())
                .urlYoutube(contact.getUrlYoutube())
                .build();
    }

    private HotelInforResponse.PolicyInfor toPolicyInfor(Policy policy) {
        if (policy == null) {
            return null;
        }

        return HotelInforResponse.PolicyInfor.builder()
                .checkInFrom(policy.getCheckInFrom())
                .checkInTo(policy.getCheckInTo())
                .checkOutBefore(policy.getCheckOutBefore())
                .minAgeCheckin(policy.getMinAgeCheckin())
                .petPolicy(policy.getPetPolicy())
                .smokingPolicy(policy.getSmokingPolicy())
                .build();
    }

}
