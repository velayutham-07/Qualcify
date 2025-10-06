package com.qualcify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/upload-page")
    public String showUploadPage() {
        return "upload"; // corresponds to upload.html in templates
    }
}
