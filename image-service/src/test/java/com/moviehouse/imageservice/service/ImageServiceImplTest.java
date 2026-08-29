package com.moviehouse.imageservice.service;

import com.moviehouse.imageservice.dataaccess.entity.Image;
import com.moviehouse.imageservice.exception.ImageNotFoundException;
import com.moviehouse.imageservice.exception.InvalidImageException;
import com.moviehouse.imageservice.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @TempDir
    Path storageDir;

    @Mock
    private ImageRepository imageRepository;

    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl(imageRepository, storageDir.toString());
    }

    @Test
    void uploadStoresTheFileAndRecordsItsContentType() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "poster.jpg", "image/jpeg", "bytes".getBytes());
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Image saved = imageService.upload(file);

        assertThat(saved.getContentType()).isEqualTo("image/jpeg");
        assertThat(Files.exists(storageDir.resolve("poster.jpg"))).isTrue();
    }

    @Test
    void uploadKeepsATraversingFileNameInsideTheStorageDirectory() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "../../evil.jpg", "image/jpeg", "bytes".getBytes());
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Image saved = imageService.upload(file);

        assertThat(Paths.get(saved.getPath())).hasParent(storageDir);
        assertThat(Files.exists(storageDir.getParent().resolve("evil.jpg"))).isFalse();
    }

    @Test
    void uploadRejectsANonImageContentType() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "payload.sh", "application/x-sh", "rm -rf".getBytes());

        assertThatThrownBy(() -> imageService.upload(file))
                .isInstanceOf(InvalidImageException.class);
    }

    @Test
    void getImageReportsAMissingRecord() {
        UUID id = UUID.randomUUID();
        when(imageRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.getImage(id))
                .isInstanceOf(ImageNotFoundException.class);
    }

    @Test
    void getImageReportsAFileThatIsNoLongerOnDisk() {
        UUID id = UUID.randomUUID();
        Image image = new Image();
        image.setId(id);
        image.setPath(storageDir.resolve("missing.jpg").toString());
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        assertThatThrownBy(() -> imageService.getImage(id))
                .isInstanceOf(ImageNotFoundException.class);
    }
}
