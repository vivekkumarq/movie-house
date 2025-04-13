package com.moviehouse.imageservice.service;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    Image upload(MultipartFile image) throws IOException;
    List<Image> getAllImageData();
    byte[] getImage(UUID id) throws IOException;
}
