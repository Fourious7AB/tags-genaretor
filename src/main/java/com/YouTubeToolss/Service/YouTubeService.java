package com.YouTubeToolss.Service;

import com.YouTubeToolss.Model.SearchVideo;
import com.YouTubeToolss.Model.Video;
import com.YouTubeToolss.Model.VideoDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private Integer maxRelatedVideo;

    public SearchVideo searchVideo(String videoTitle) {
        List<String> videoIds = searchForVideoIds(videoTitle);

        if (videoIds.isEmpty()) {
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideos(Collections.emptyList())
                    .build();
        }

        String primaryVideoId = videoIds.get(0);
        List<String> relatedVideosId = videoIds.subList(1, Math.min(videoIds.size(), maxRelatedVideo));

        Video primaryVideo = getVideoById(primaryVideoId);

        List<Video> relatedVideos = new ArrayList<>();
        for (String id : relatedVideosId) {
            Video video = getVideoById(id);
            if (video != null) {
                relatedVideos.add(video);
            }
        }

        return SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideos(relatedVideos)
                .build();
    }

    private List<String> searchForVideoIds(String videoTitle) {
        String url = String.format("%s/search?part=snippet&q=%s&type=video&maxResults=%d&key=%s",
                baseUrl, videoTitle, maxRelatedVideo, apiKey);

        SearchApiResponse response = restTemplate.getForObject(url, SearchApiResponse.class);

        if (response == null || response.items == null) {
            return Collections.emptyList();
        }

        List<String> videoIds = new ArrayList<>();
        for (SearchItem item : response.items) {
            if (item.id != null && item.id.videoId != null) {
                videoIds.add(item.id.videoId);
            }
        }

        return videoIds;
    }

    private Video getVideoById(String videoId) {
        String url = String.format("%s/videos?part=snippet&id=%s&key=%s", baseUrl, videoId, apiKey);

        VideoApiResponse response = restTemplate.getForObject(url, VideoApiResponse.class);

        if (response == null || response.items == null || response.items.isEmpty()) {
            return null;
        }

        Snippet snippet = response.items.get(0).snippet;

        return Video.builder()
                .id(videoId)
                .title(snippet.title)
                .channelTitle(snippet.channelTitle)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .build();
    }
    public VideoDetails  getVideoDetails(String videoId) {
        String url = String.format("%s/videos?part=snippet&id=%s&key=%s", baseUrl, videoId, apiKey);

        VideoApiResponse response = restTemplate.getForObject(url, VideoApiResponse.class);

        if (response == null || response.items == null || response.items.isEmpty()) {
            return null;
        }

        Snippet snippet = response.items.get(0).snippet;

        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.title)
                .channelTitle(snippet.channelTitle)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .build();


    }


    @Data
    static class SearchApiResponse { List<SearchItem> items; }

    @Data
    static class SearchItem { Id id; }

    @Data
    static class Id { String videoId; }

    @Data
    static class VideoApiResponse { List<VideoItems> items; }

    @Data
    static class VideoItems { Snippet snippet; }

    @Data
    static class Snippet {
        String title;
        String description;
        String channelTitle;
        String publishedAt;
        List<String> tags;
        Thumbnails thumbnails;
    }

    @Data
    static class Thumbnails {
        Thumbnail maxres;
        Thumbnail high;
        Thumbnail medium;
        Thumbnail _default;

        String getBestThumbnailUrl() {
            if (maxres != null) return maxres.url;
            if (high != null) return high.url;
            if (medium != null) return medium.url;
            return _default != null ? _default.url : "";
        }
    }

    @Data
    static class Thumbnail { String url; }
}
