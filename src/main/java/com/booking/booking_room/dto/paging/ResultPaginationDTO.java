package com.booking.booking_room.dto.paging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    @Builder
    public static class Meta {
        //        private int current;
        private int page;
        private int pageSize;
        private int pages;
        private long total;
    }
}
