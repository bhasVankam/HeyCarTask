package com.heycar.util;

import com.heycar.dto.PageDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PagingUtils {

    public static <T, R> PageDTO<R> createPageDTO(Page<T> page, Function<T, R> mapper) {
        PageDTO<R> pageDto = new PageDTO();
        fillPageDto(pageDto, page, mapper);
        return pageDto;
    }

    private static <T, R> void fillPageDto(PageDTO<R> pageDto, Page<T> page, Function<T, R> mapper) {
        List<R> entities = (List)page.getContent().stream().map(mapper).collect(Collectors.toList());
        pageDto.setEntities(entities);
        pageDto.setTotalEntityCount(page.getTotalElements());
        pageDto.setPage(page.getNumber());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPageCount(page.getTotalPages());
    }
}
