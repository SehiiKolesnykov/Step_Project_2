package StepApp.util;

import StepApp.controller.LikeController;
import StepApp.controller.MessageController;
import StepApp.controller.TokenController;
import StepApp.controller.UserController;

public class StartApp {

    UserController userController;
    TokenController tokenController;
    MessageController messageController;
    LikeController likeController;

    public StartApp() {
        userController = new UserController();
        tokenController = new TokenController();
        messageController = new MessageController();
        likeController = new LikeController();
    }

    public void startApp() {
        if (userController.startApp()){
            tokenController.startAppToken();
            messageController.startApp();
            likeController.startApp();
        }
    }
}
