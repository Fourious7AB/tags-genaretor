package com.YouTubeToolss.Controller;

import com.YouTubeToolss.Service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class ThumbnailController {

    @Autowired
    ThumbnailService service;

    @GetMapping("/thumbnail")
    public String getThumbnails() {
        return "Here are your thumbnail!";
    }

    @PostMapping("/get-thumbnail")
    public String showThumbnail(@RequestBody  String videoUrlOrId, Model model ){
        String videoId=service.extractVideoId( videoUrlOrId);
        //set the error
        if(videoId==null){
            model.addAttribute("error","Invalid Youtube URL");
        }
        String thumbnailUrl="https://img.youtube.com/vi/"+videoId+"/maxresdefault.jpg";
        model.addAttribute("response","thumbnailUrl");
       return thumbnailUrl;
    }




}
