package StepApp.util;

import StepApp.controller.MessageController;
import StepApp.controller.TokenController;
import StepApp.controller.UserController;

public class StartApp {

    UserController userController;
    TokenController tokenController;
    MessageController messageController;

    public StartApp() {
        userController = new UserController();
        tokenController = new TokenController();
        messageController = new MessageController();
    }

    public void startApp() {
        if (userController.startApp()){
            tokenController.startAppToken();
            messageController.startApp();
        }
    }
}
