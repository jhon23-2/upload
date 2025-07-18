package com.jdev.uploadFile.service;

import com.jdev.uploadFile.dto.FullResponse;
import com.jdev.uploadFile.dto.ResponseFiles;
import com.jdev.uploadFile.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileInterface {
    FileEntity saveFile(MultipartFile file) throws IOException;
    Optional<FileEntity> getFileById(UUID id) throws FileNotFoundException;
    FullResponse getAllFiles(int page, int size);
    void deleteFile(UUID id) throws FileNotFoundException;
}
