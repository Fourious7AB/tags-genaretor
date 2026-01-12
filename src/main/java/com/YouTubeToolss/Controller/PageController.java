package com.YouTubeToolss.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
    @GetMapping("/video/details")
   public String videoDetails(){
       return "video-details";
   }

}
