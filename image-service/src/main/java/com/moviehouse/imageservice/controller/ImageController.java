package com.moviehouse.imageservice.controller;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import com.moviehouse.imageservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/image-info-management/image")
@CrossOrigin("*")
public class ImageController {

    private static final MediaType FALLBACK_TYPE = MediaType.IMAGE_JPEG;

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Image> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(imageService.upload(file), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Image>> getAllImageData(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(imageService.getAllImageData(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id) throws IOException {
        Image imageData = imageService.getImageData(id);
        byte[] image = imageService.getImage(id);
        return ResponseEntity.ok()
                .contentType(mediaTypeOf(imageData))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic())
                .body(image);
    }

    private MediaType mediaTypeOf(Image image) {
        if (image.getContentType() == null) {
            return FALLBACK_TYPE;
        }
        try {
            return MediaType.parseMediaType(image.getContentType());
        } catch (org.springframework.http.InvalidMediaTypeException e) {
            return FALLBACK_TYPE;
        }
    }
}
