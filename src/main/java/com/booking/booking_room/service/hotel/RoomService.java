package com.booking.booking_room.service.hotel;

import com.booking.booking_room.dto.hotel.RoomResponse;
import com.booking.booking_room.entity.room.Room;
import com.booking.booking_room.entity.room.RoomType;
import com.booking.booking_room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Page<RoomResponse> getAllRoom(Pageable pageable, Specification<Room> specification) {
        Page<Room> page = roomRepository.findAll(specification, pageable);
        Page<RoomResponse> responsePage = page.map(room -> {
            RoomResponse roomResponse = RoomResponse.builder()
                    .id(room.getId())
                    .roomNumber(room.getRoomNumber())
                    .price(room.getPrice())
                    .roomTypeName(room.getRoomType().getName())
                    .maxOccupancy(room.getRoomType().getMaxOccupancy())
                    .totalReviews(room.getReviews().toArray().length)
                    .build();
            return roomResponse;
        });
        return responsePage;
    }
}
