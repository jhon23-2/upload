package com.jdev.uploadFile.dto;

import lombok.Builder;

@Builder
public record ResponseFiles(
         String name,
         String type,
         long size,
         String url
) {

}
