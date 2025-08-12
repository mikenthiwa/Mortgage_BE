package com.example.mortgage.extension;

import com.example.mortgage.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class PaginationResponse<T> extends ApiResponse {
    public boolean last;
    public long totalElements;
    public int totalPages;
    public boolean first;
    public int size;
    public int number;
    public Sort sort;
    public int numberOfElements;
    public boolean empty;

    public PaginationResponse(Page<T> page, String message) {
        this.success = true;
        this.statusCode = 200;
        this.message = message;
        this.data = page.getContent();

        this.last = page.isLast();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.sort = page.getSort();
        this.numberOfElements = page.getNumberOfElements();
        this.empty = page.isEmpty();
    }

}
