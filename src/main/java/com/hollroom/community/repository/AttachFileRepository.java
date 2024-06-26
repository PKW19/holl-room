package com.hollroom.community.repository;

import com.hollroom.community.domain.entity.AttachFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachFileRepository extends JpaRepository<AttachFileEntity,Long> {
    List<AttachFileEntity> findByPostId(Long postId);
}
