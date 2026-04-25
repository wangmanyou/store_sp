package org.example.store_sp_backend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.config.UploadProperties;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.vo.UploadResultVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
public class AdminUploadController {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final UploadProperties uploadProperties;

    @PostMapping("/image")
    public ApiResponse<UploadResultVO> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Please select an image file");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Only image files are allowed");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalName);
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase(Locale.ROOT))) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Unsupported image format");
        }

        LocalDate now = LocalDate.now();
        String relativeDir = now.getYear() + "/" + String.format("%02d", now.getMonthValue());
        String fileName = UUID.randomUUID() + "." + extension.toLowerCase(Locale.ROOT);
        Path uploadDir = getUploadRoot().resolve(relativeDir).normalize();
        Path target = uploadDir.resolve(fileName).normalize();

        try {
            Files.createDirectories(uploadDir);
            file.transferTo(target);
        } catch (IOException ex) {
            throw new BusinessException(ResultCode.ERROR.getCode(), "Image upload failed");
        }

        String url = uploadProperties.getUrlPrefix() + "/" + relativeDir + "/" + fileName;
        return ApiResponse.success(new UploadResultVO(fileName, url.replace("\\", "/")));
    }

    private Path getUploadRoot() {
        Path root = Paths.get(uploadProperties.getDir());
        if (!root.isAbsolute()) {
            root = Paths.get(System.getProperty("user.dir")).resolve(root);
        }
        return root.normalize();
    }
}
