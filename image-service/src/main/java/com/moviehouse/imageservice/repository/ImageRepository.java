package com.moviehouse.imageservice.repository;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
}
