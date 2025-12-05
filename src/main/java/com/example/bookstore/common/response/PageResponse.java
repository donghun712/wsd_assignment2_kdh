package com.example.bookstore.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;     // 현재 페이지 데이터
    private int page;            // 현재 페이지 번호 (0부터 시작)
    private int size;            // 페이지 크기
    private long totalElements;  // 전체 데이터 개수
    private int totalPages;      // 전체 페이지 수
    private boolean first;       // 첫 페이지 여부
    private boolean last;        // 마지막 페이지 여부
}
