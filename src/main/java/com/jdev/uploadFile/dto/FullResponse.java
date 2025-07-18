package com.jdev.uploadFile.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record FullResponse(
        Long amount,
        int status,
        String response,
        int page,
        int size,
        List<ResponseFiles> responseFiles
        ) {
}
