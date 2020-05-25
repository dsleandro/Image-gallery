package com.dsleandro.imagegallery.controller;

import com.dsleandro.imagegallery.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    private StorageService storageService;
  
    @PostMapping("/")
    public String imageUpload(@RequestParam("file") MultipartFile file) {

        if (isImage(file)) {

            storageService.store(file);

            return "redirect:/";

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
}