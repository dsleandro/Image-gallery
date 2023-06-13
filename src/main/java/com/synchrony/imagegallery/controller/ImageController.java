package com.synchrony.imagegallery.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.synchrony.imagegallery.entity.Image;
import com.synchrony.imagegallery.entity.User;
import com.synchrony.imagegallery.repository.ImageRepository;
import com.synchrony.imagegallery.repository.UserRepository;
import com.synchrony.imagegallery.storage.StorageService;

@Controller
public class ImageController {

    UUID uuid = UUID.randomUUID();

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String displayImages(Model model, Principal pUser) throws IOException {
        try {
            User user = userRepository.findByUsername(pUser.getName())
                    .orElseThrow(() -> new Exception("user not found"));

            model.addAttribute("images",
                    storageService.loadAll(user)
                            .map(path -> MvcUriComponentsBuilder
                                    .fromMethodName(ImageController.class, "serveImage", path.getFileName().toString())
                                    .build().toUri().toString())
                            .collect(Collectors.toList()));

            model.addAttribute("username", pUser.getName());

            return "index";

        } catch (Exception e) {
            return "error";
        }

    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String imageUpload(@RequestParam("file") MultipartFile file, Principal user) {

        if (isImage(file)) {

            try {
                // set an ID to image with its extension
                String fileId = uuid.toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                // save image on server
                storageService.store(fileId, file.getBytes());

                // parse MultipartFile to Image entity
                Image image = parseToImage(fileId, user);

                // save path of image in database
                imageRepository.save(image);

                return "redirect:/";

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

        } else {
            return "error";
        }

    }

    public boolean isImage(MultipartFile file) {

        String type = file.getContentType().split("/")[0];

        if (type.equals("image"))
            return true;
        else
            return false;

    }

    public Image parseToImage(String fileId, Principal user) throws Exception {

        Path path = storageService.getPath(fileId);

        User username = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new Exception("user not found"));

        return new Image(path.toString(), username);

    }
}