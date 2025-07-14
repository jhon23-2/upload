package com.jdev.uploadFile.repository;

import com.jdev.uploadFile.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

}
