package com.moviehouse.imageservice.service;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import com.moviehouse.imageservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService{

    private final Path root = Paths.get("images");
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image upload(MultipartFile image) throws IOException {
        String imageName = image.getOriginalFilename();
        String path = root +"/"+ imageName;
        Image imageData = new Image();
        imageData.setPath(path);
//        System.out.println(path);
        Files.copy(image.getInputStream(), this.root.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        return imageRepository.save(imageData);
    }

    @Override
    public List<Image> getAllImageData() {
        return imageRepository.findAll();
    }

    @Override
    public byte[] getImage(UUID id) throws IOException {
        String path = imageRepository.findById(id).get().getPath();
        System.out.println(path);
        return Files.readAllBytes(new File(path).toPath());
    }
}
