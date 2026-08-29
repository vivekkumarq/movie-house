package com.moviehouse.imageservice.service;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {
    Image upload(MultipartFile image) throws IOException;
    Page<Image> getAllImageData(Pageable pageable);
    Image getImageData(UUID id);
    byte[] getImage(UUID id) throws IOException;
}
