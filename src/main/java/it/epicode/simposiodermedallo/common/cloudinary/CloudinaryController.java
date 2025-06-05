package it.epicode.simposiodermedallo.common.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class CloudinaryController {
    private final Cloudinary cloudinary;
    @PostMapping(path="/uploadme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String upload(@RequestPart("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String baseName = getFileNameWithoutExtension(originalName)
                    .replaceAll("[^a-zA-Z0-9\\-_.]", "_");

            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Cloudinary.asMap(
                            "folder", "FS1024",
                            "public_id", baseName,
                            "resource_type", "image"
                    )
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Errore upload Cloudinary: " + e.getMessage(), e);
        }
    }

    private String getFileNameWithoutExtension(String filename) {
        if (filename == null) return "file";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex != -1) ? filename.substring(0, dotIndex) : filename;
    }

    }