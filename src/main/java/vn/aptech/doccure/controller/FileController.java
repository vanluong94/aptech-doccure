package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.aptech.doccure.storage.StorageService;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class FileController {

    @Autowired
    StorageService storageService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {

        Resource file = storageService.loadAsResource(filename);
        String mimeType = Files.probeContentType(file.getFile().toPath());
        String extension = filename.substring(filename.lastIndexOf("."));
        MediaType contentType;

        switch (extension) {
            case "png":
                contentType = MediaType.IMAGE_PNG;
                break;
            case "gif":
                contentType = MediaType.IMAGE_GIF;
                break;
            default:
                contentType = MediaType.IMAGE_JPEG;
                break;
        }

        return ResponseEntity.ok().contentType(contentType).body(file);
    }

}
