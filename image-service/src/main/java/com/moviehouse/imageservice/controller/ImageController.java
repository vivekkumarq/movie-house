package com.moviehouse.imageservice.controller;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import com.moviehouse.imageservice.service.ImageService;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/image-info-management/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<Image> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.upload(file));
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImageData(){
        return new ResponseEntity<>(imageService.getAllImageData(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id) throws IOException {
        byte[] image = imageService.getImage(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/png")).body(image);
    }
}
