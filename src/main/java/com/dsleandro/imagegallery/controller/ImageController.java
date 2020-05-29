package com.dsleandro.imagegallery.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.dsleandro.imagegallery.storage.StorageService;

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

@Controller
public class ImageController {

    @Autowired
    private StorageService storageService;


    @GetMapping("/")
	public String displayImages(Model model) throws IOException {

		model.addAttribute("images", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(ImageController.class,
						"serveImage", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "index";
	}

    @GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveImage(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
  
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