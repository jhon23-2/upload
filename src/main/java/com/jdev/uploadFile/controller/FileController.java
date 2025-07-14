package com.jdev.uploadFile.controller;

import com.jdev.uploadFile.dto.ResponseFiles;
import com.jdev.uploadFile.entity.FileEntity;
import com.jdev.uploadFile.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file/")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        FileEntity fileEntity = this.fileService.saveFile(file);
        return "File uploaded successfully: " + fileEntity.getName();
    }

    @GetMapping("download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") UUID id) throws IOException {
        FileEntity fileEntity = this.fileService.getFileById(id)
                .orElseThrow(() -> new IOException("File not found with id: " + id));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, fileEntity.getType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"")
                .body(fileEntity.getData());
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFiles> getAllFiles() {
        return this.fileService.getAllFiles();
    }


}
