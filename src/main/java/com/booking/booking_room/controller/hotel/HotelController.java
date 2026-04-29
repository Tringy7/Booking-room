package com.booking.booking_room.controller.hotel;

import com.booking.booking_room.dto.hotel.RoomResponse;
import com.booking.booking_room.dto.paging.ResultPaginationDTO;
import com.booking.booking_room.entity.room.Room;
import com.booking.booking_room.service.hotel.RoomService;
import com.booking.booking_room.util.ConvertUtil;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.booking_room.annotation.ApiMessage;
import com.booking.booking_room.dto.hotel.HotelInforResponse;
import com.booking.booking_room.service.hotel.InformationService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel")
public class HotelController {

    private final InformationService informationService;
    private final RoomService roomService;
    private final ConvertUtil convertUtil;

    @GetMapping("/hotel")
    @ApiMessage("Get hotel information successful")
    public ResponseEntity<HotelInforResponse> getHotelInformation() {
        return ResponseEntity.ok(informationService.getHotelInformation());
    }

    @GetMapping("/hotel/rooms")
    @ApiMessage("Get all room")
    public ResponseEntity<ResultPaginationDTO> getAllRooms(
            Pageable pageable,
            @Filter Specification<Room> specification
            ) {
        Page<RoomResponse> roomPage = this.roomService.getAllRoom(pageable, specification);
        ResultPaginationDTO res = this.convertUtil.convertToPagingDto(roomPage, pageable);
        return ResponseEntity.ok(res);
    }
}
