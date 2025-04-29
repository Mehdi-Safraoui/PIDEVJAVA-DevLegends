package Controllers;

import Entities.Evenement;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.Calendar;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;

public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "JavaFX Calendar App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_595460522393-8db48ch9amebvovvahvhpjsjkj6cjb88.apps.googleusercontent.com.json";

    /**
     * Charge les autorisations OAuth2.
     */
    private static Credential getCredentials() throws Exception {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new Exception("Le fichier credentials.json est introuvable.");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Crée le service Google Calendar.
     */
    private static Calendar getCalendarService() throws Exception {
        Credential credential = getCredentials();

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Ajoute un événement Google Calendar.
     */
    public static void ajouterEvenement(Evenement evt) {
        try {
            Calendar service = getCalendarService();

            Event event = new Event()
                    .setSummary(evt.getTitreEvent())
                    .setLocation(evt.getLieuEvent())
                    .setDescription("Événement généré depuis l'application JavaFX");

            Date startDate = evt.getDateEvent(); // Assure-toi que c'est bien une java.util.Date
            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDate))
                    .setTimeZone("Europe/Paris");

            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(startDate))
                    .setTimeZone("Europe/Paris");

            event.setStart(start);
            event.setEnd(end);

            service.events().insert("primary", event).execute();

            System.out.println("Événement ajouté au calendrier Google !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
