package StepApp.controller;

import StepApp.service.LikeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class LikeController {

    LikeService likeService;

    public LikeController() {
        this.likeService = new LikeService();
    }

    public void startApp() {
        likeService.startApp();
    }

    public void removeEndLikedFromCookie (HttpServletResponse response) {
        likeService.removeEndLikedFromCookie(response);
    }

    public Map<String, Object> likePageData(HttpServletRequest request, HttpServletResponse response) {
        return likeService.likePageData(request, response);
    }

    public List<Map<String, Object>> getLikedUsersData(HttpServletRequest request) {
        return likeService.getLickedUsersData(request);
    }
}
