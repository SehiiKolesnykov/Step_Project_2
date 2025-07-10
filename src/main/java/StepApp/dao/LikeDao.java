package StepApp.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface LikeDao {
    void createLikedTable();
    void createRatedTable();
    void addToLikedDb(String userEmail, String likedUser);
    void addToRatedDb(String userEmail, String ratedUser);
    List<String> getLikedUsersFromDb(String userEmail);
    List<String> getRatedUsersFromDb(String userEmail);
    void removeFromRatedDb(String userEmail);
    void addEndLikedToCookie(HttpServletResponse response);
    Optional<String> getLikedEnd(HttpServletRequest request);
    void removeEndLikedFromCookie(HttpServletResponse response);
}
