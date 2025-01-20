package com.example.chatviewer;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationParser {

    private boolean errorShown = false;  // Ajout d'un indicateur pour éviter l'affichage multiple

    /**
     * Parses a conversation file and converts its contents into a list of `Message` objects.
     * Each message in the file is expected to include a timestamp, nickname, and content
     * in the following format:
     *
     * <pre>
     * Time: yyyy-MM-dd HH:mm:ss
     * Name: user_nickname
     * Message: message_content
     * </pre>
     *
     * Blank lines separate individual messages.
     *
     * @param file the file containing the conversation data to be parsed.
     * @return a list of parsed `Message` objects.
     * @throws IOException if an error occurs while reading the file.
     */
    public List<Message> parseFile(File file) throws IOException {
        List<Message> messages = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!lines.isEmpty()) {
                        try {
                            messages.add(parseMessage(lines));
                        } catch (InvalidMessageFormatException e) {
                            showErrorDialog("Format de message invalide", e.getMessage());
                            return new ArrayList<>(); // Retourne une liste vide si une erreur est détectée
                        }
                        lines.clear();
                    }
                } else {
                    lines.add(line);
                }
            }
            if (!lines.isEmpty()) {
                try {
                    messages.add(parseMessage(lines));
                } catch (InvalidMessageFormatException e) {
                    showErrorDialog("Format de message invalide", e.getMessage());
                    return new ArrayList<>(); // Retourne une liste vide si une erreur est détectée
                }
            }
        }

        return messages;
    }

    /**
     * Converts a list of lines representing a single message into a `Message` object.
     *
     * @param lines the list of lines representing a single message.
     * @return the parsed `Message` object.
     * @throws InvalidMessageFormatException if the message format is invalid.
     */
    private Message parseMessage(List<String> lines) throws InvalidMessageFormatException {
        String timeStamp = lines.get(0).substring(5);  // Extrait la partie après "Time:"
        if (!timeStamp.contains(" ")) {  // Vérifie si la date est manquante
            throw new InvalidMessageFormatException("Le message n'inclut pas de date valide. Format attendu : 'Time: yyyy-MM-dd HH:mm:ss'");
        }

        if (timeStamp.length() < 16) {  // Vérifie si l'heure est manquante
            throw new InvalidMessageFormatException("Le message n'inclut pas l'heure complète : " + timeStamp);
        }


        String nickName = lines.get(1).substring(5); // Extrait la partie après "Name:"
        if (nickName.trim().isEmpty()) {  // Si le nom est manquant
            throw new InvalidMessageFormatException("Le message n'inclut pas de pseudo utilisateur.");
        }

        String content = lines.get(2).substring(8);  // Extrait la partie après "Message:"
        if (content.trim().isEmpty()) {  // Si le message est manquant
            throw new InvalidMessageFormatException("Le message est vide.");
        }

        return new Message(timeStamp, nickName, content);
    }

    /**
     * Displays an error dialog to notify the user of parsing errors.
     *
     * @param title the title of the error dialog.
     * @param message the message describing the error.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


