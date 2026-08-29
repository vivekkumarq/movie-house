package com.moviehouse.imageservice.service;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import com.moviehouse.imageservice.exception.ImageNotFoundException;
import com.moviehouse.imageservice.exception.InvalidImageException;
import com.moviehouse.imageservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_NOT_FOUND = "Image not found";

    private final Path root;

    @Autowired
    private ImageRepository imageRepository;

    public ImageServiceImpl(@Value("${image.storage-dir:images}") String storageDir) {
        this.root = Paths.get(storageDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    void createStorageDirectory() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    @Transactional
    public Image upload(MultipartFile image) throws IOException {
        String fileName = StringUtils.cleanPath(
                StringUtils.getFilename(image.getOriginalFilename() == null ? "" : image.getOriginalFilename()));
        if (fileName.isEmpty() || fileName.contains("..")) {
            throw new InvalidImageException("Invalid file name");
        }
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageException("Only image uploads are accepted");
        }

        Path target = root.resolve(fileName).normalize();
        if (!target.startsWith(root)) {
            throw new InvalidImageException("Invalid file name");
        }
        Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Image imageData = new Image();
        imageData.setPath(target.toString());
        imageData.setContentType(contentType);
        return imageRepository.save(imageData);
    }

    @Override
    public Page<Image> getAllImageData(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image getImageData(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    @Override
    public byte[] getImage(UUID id) throws IOException {
        Path path = Paths.get(getImageData(id).getPath());
        if (!Files.exists(path)) {
            throw new ImageNotFoundException(IMAGE_NOT_FOUND);
        }
        return Files.readAllBytes(path);
    }
}
