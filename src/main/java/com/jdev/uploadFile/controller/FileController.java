package com.jdev.uploadFile.controller;

import com.jdev.uploadFile.dto.FullResponse;
import com.jdev.uploadFile.dto.ResponseFiles;
import com.jdev.uploadFile.dto.ResponseMessage;
import com.jdev.uploadFile.entity.FileEntity;
import com.jdev.uploadFile.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;

    @PostMapping("upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        FileEntity fileEntity = this.fileService.saveFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .response("File uploaded successfully: " + fileEntity.getName())
                        .build());
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


    @GetMapping("preview/{id}")
    public ResponseEntity<byte[]> previewFile(@PathVariable("id") UUID id) throws FileNotFoundException {
        FileEntity fileEntity = this.fileService.getFileById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(fileEntity.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getName() + "\"")
                .body(fileEntity.getData());
    }


    @GetMapping("all")
    public ResponseEntity<FullResponse> getAllFiles(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.fileService.getAllFiles(page, size));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("id") UUID id) throws FileNotFoundException {
        this.fileService.deleteFile(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .response("File deleted successfully with id: " + id)
                        .build());
    }


}
