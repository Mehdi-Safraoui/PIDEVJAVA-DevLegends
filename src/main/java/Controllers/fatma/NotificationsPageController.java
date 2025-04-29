package Controllers.fatma;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NotificationsPageController {
    @FXML
    private VBox notificationList;

    public void addNotification(String notification) {
        Text notificationText = new Text(notification);
        notificationList.getChildren().add(notificationText);
    }

    @FXML
    private void closeNotifications() {
        Stage stage = (Stage) notificationList.getScene().getWindow();
        stage.close();
    }
}
