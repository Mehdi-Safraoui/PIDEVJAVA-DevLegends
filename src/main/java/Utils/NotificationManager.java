package Utils;

import Controllers.fatma.NotificationsPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Queue;

public class NotificationManager {
    private static NotificationsPageController notificationsPageController;
    private static Queue<String> notificationQueue = new LinkedList<>();

    public static void setNotificationsPageController(NotificationsPageController controller) {
        notificationsPageController = controller;
        // Ajouter les notifications en attente dès que le contrôleur est disponible
        while (!notificationQueue.isEmpty()) {
            notificationsPageController.addNotification(notificationQueue.poll());
        }
    }

    public static void addNotification(String message) {
        if (notificationsPageController != null) {
            notificationsPageController.addNotification(message);
        } else {
            // Ajouter à la queue si la fenêtre n'est pas encore ouverte
            notificationQueue.add(message);
        }
    }
    public static void openNotificationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(NotificationManager.class.getResource("/fatma/NotificationPage.fxml"));
            Parent root = loader.load();

            NotificationsPageController controller = loader.getController();
            setNotificationsPageController(controller); // définit le contrôleur ET injecte les anciennes notifications

            // ⬇️ Affiche aussi les notifications déjà ajoutées
            while (!notificationQueue.isEmpty()) {
                controller.addNotification(notificationQueue.poll());
            }

            Stage stage = new Stage();
            stage.setTitle("Notifications");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
