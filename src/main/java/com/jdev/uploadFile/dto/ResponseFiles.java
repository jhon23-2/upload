package com.jdev.uploadFile.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ResponseFiles(UUID id,
         String name,
         String type,
         long size,
         String downloadUri,
         String previewUri,
         String dateTime
) {

}
