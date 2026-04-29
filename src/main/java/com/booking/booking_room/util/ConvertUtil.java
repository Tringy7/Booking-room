package com.booking.booking_room.util;

import com.booking.booking_room.dto.paging.ResultPaginationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtil {
    public <T> ResultPaginationDTO convertToPagingDto(Page<T> content, Pageable pageable) {
        ResultPaginationDTO.Meta meta = ResultPaginationDTO.Meta.builder()
//                .current(pageable.getPageNumber() + 1)
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(content.getTotalPages())
                .total(content.getTotalElements())
                .build();

        ResultPaginationDTO resultPaginationDTO = new  ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(content.getContent());
        return resultPaginationDTO;
    }
}
