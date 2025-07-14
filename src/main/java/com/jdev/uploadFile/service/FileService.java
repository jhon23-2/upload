package com.jdev.uploadFile.service;

import com.jdev.uploadFile.dto.ResponseFiles;
import com.jdev.uploadFile.entity.FileEntity;
import com.jdev.uploadFile.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements FileInterface{

    private final FileRepository fileRepository;

    @Override
    public FileEntity saveFile(MultipartFile file) throws IOException {

        String name = StringUtils.cleanPath(file.getOriginalFilename());

        FileEntity fileEntity = FileEntity.builder()
                .name(name)
                .type(file.getContentType())
                .data(file.getBytes())
                .build();

        log.info("Saving file with name: {}", name);
        return this.fileRepository.save(fileEntity);

    }

    @Override
    public Optional<FileEntity> getFileById(UUID id) throws FileNotFoundException {
        log.info("Fetching file with id: {}", id);
        return Optional.of(
                this.fileRepository.findById(id)
                        .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id))
        );
    }

    @Override
    public List<ResponseFiles> getAllFiles() {

        List<ResponseFiles> responseFiles = this.fileRepository.findAll()
                .stream()
                .map(files -> {
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/file/download/")
                            .path(files.getId().toString())
                            .toUriString();

                    return ResponseFiles.builder()
                            .name(files.getName())
                            .type(files.getType())
                            .size(files.getData().length)
                            .url(fileDownloadUri)
                            .build();

                }).toList();

        log.info("Fetching all files, total count: {}", responseFiles.size());
        return responseFiles;
    }
}
