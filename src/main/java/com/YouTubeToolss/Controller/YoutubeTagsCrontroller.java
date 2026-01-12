package com.YouTubeToolss.Controller;

import com.YouTubeToolss.Model.SearchVideo;
import com.YouTubeToolss.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/youtube")
public class YoutubeTagsCrontroller {

    @Autowired
    private YouTubeService youTubeService;

    @Value("${youtube.api.key}")
    private String apiKey;//store the api key in apiKey Variable

    private Boolean isApiKeyConfigured(){
       return apiKey!=null && !apiKey.isEmpty();
    }


    @GetMapping("/search")
    public SearchVideo videoTag(@RequestParam String videoTitle) {

        if (!isApiKeyConfigured()) {
            throw new IllegalStateException("YouTube API key is not configured!");
        }

        if (videoTitle == null || videoTitle.isEmpty()) {
            throw new IllegalArgumentException("YouTube title is required");
        }

        return youTubeService.searchVideo(videoTitle);
    }
    }





