package com.jdev.uploadFile.service;

import com.jdev.uploadFile.dto.FullResponse;
import com.jdev.uploadFile.dto.ResponseFiles;
import com.jdev.uploadFile.entity.FileEntity;
import com.jdev.uploadFile.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public FullResponse getAllFiles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<ResponseFiles> responseFiles = this.fileRepository.findAll(pageable)
                .stream()
                .map(files -> {
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/file/download/")
                            .path(files.getId().toString())
                            .toUriString();

                    String filePreviewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/file/preview/")
                            .path(files.getId().toString())
                            .toUriString();

                    return ResponseFiles.builder()
                            .id(files.getId())
                            .name(files.getName())
                            .type(files.getType())
                            .size(files.getData().length)
                            .downloadUri(fileDownloadUri)
                            .previewUri(filePreviewUri)
                            .dateTime(files.getDateTime().toString())
                            .build();

                }).toList();

        log.info("Fetching all files, total count: {}", responseFiles.size());
        return FullResponse.builder()
                .response("Files fetched successfully")
                .status(HttpStatus.OK.value())
                .responseFiles(responseFiles)
                .amount((long) responseFiles.size())
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public void deleteFile(UUID id) throws FileNotFoundException {
    log.info("Deleting file with id: {}", id);

    FileEntity fileEntity = this.fileRepository.findById(id)
            .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

    this.fileRepository.delete(fileEntity);

    log.info("File deleted successfully with id: {}", id);
    }
}
