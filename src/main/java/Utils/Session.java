package Utils;

import Entities.salsabil.User;
import Entities.fatma.Produit;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private static User currentUser;



    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }


    public static void clear() {
        currentUser = null;
    }
}
