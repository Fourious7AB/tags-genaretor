package com.YouTubeToolss.Controller;

import com.YouTubeToolss.Model.VideoDetails;
import com.YouTubeToolss.Service.ThumbnailService;
import com.YouTubeToolss.Service.YouTubeService;
import com.YouTubeToolss.dto.ErrorResponce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/youtube")

public class YouTubeVideoControler {

    private final YouTubeService youTubeService;
    private final ThumbnailService thumbnailService;

    @GetMapping("/video-details")
    public ResponseEntity<?> fetchVideoDetails(
            @RequestParam String videoUrlOrId) {

        String videoId = thumbnailService.extractVideoId(videoUrlOrId);

        if (videoId == null || videoId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponce("Invalid or missing video ID"));
        }

        VideoDetails details = youTubeService.getVideoDetails(videoId);

        if (details == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponce("Video not found"));
        }

        return ResponseEntity.ok(details);
    }
}
