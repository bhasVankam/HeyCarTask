package com.heycar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Data
@Builder
@AllArgsConstructor
public class PageDTO<T> {

    private static final PageDTO<?> EMPTY = new PageDTO<>(emptyList());

    private int page = 0;
    private int pageSize = 10;
    private int totalPageCount;
    private Long totalEntityCount;
    private List<T> entities;

    public PageDTO() {
        this(new ArrayList<>()); // For backward compatibility
    }

    private PageDTO(final List<T> entities) {
        this.entities = entities;
    }

    public PageDTO(int page, int pageSize, int totalPageCount, List<T> entities) {
        // For backward compatibility
        this.page = page;
        this.pageSize = pageSize;
        this.totalPageCount = totalPageCount;
        this.entities = entities;
    }

    public <R> PageDTO<R> map(Function<T, R> mapper) {
        return new PageDTO<>(
                page, pageSize, totalPageCount, totalEntityCount,
                entities.stream().map(mapper).collect(Collectors.toList())
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> PageDTO<T> empty() {
        return (PageDTO<T>) EMPTY;
    }

}