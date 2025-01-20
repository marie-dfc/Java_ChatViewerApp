package com.example.chatviewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

import java.util.ArrayList;

public class ChatViewerApp extends Application {

    private static File lastOpenedDirectory = null;
    private TextFlow conversationView;
    private TextField messageInput;
    private TextField userNickInput;
    private List<Message> loadedMessages = new ArrayList<>();// Liste des messages chargés depuis le fichier

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ChatViewer");

        // Création de la vue de la conversation
        conversationView = new TextFlow();
        ScrollPane scrollPane = new ScrollPane(conversationView);
        scrollPane.setFitToWidth(true);

        // Champ de saisie du message
        messageInput = new TextField();
        messageInput.setPromptText("Type here");

        // Champ pour le pseudo de l'utilisateur
        userNickInput = new TextField("User");
        userNickInput.setPromptText("Nickname");

        // Bouton pour envoyer le message
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());

        // Bouton pour charger une conversation depuis un fichier
        Button loadButton = new Button("Open file");
        loadButton.setOnAction(event -> loadConversation(primaryStage));

        // Bouton pour sauvegarder la conversation dans un fichier
        Button saveButton = new Button("Save file");
        saveButton.setOnAction(event -> saveConversation(primaryStage));

        // Disposer les boutons "Open file" et "Save file" côte à côte dans un HBox
        HBox fileButtons = new HBox(10, loadButton, saveButton);

        // Disposer les éléments dans un layout vertical
        VBox layout = new VBox(10, scrollPane, new HBox(5, new Label("Pseudo:"), userNickInput), messageInput, sendButton, fileButtons);
        layout.setPadding(new javafx.geometry.Insets(10));

        // Appliquer le style CSS
        layout.getStyleClass().add("root"); // Ajoute une classe CSS à la racine
        Scene scene = new Scene(layout, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Charge le fichier CSS

        primaryStage.setScene(scene);
        primaryStage.show();

        conversationView.getStyleClass().add("text-flow");
        messageInput.getStyleClass().add("text-field");
        userNickInput.getStyleClass().add("text-field");
        sendButton.getStyleClass().add("button");
        loadButton.getStyleClass().add("button");
        saveButton.getStyleClass().add("button");

    }

    /**
     * Sends a message entered by the user.
     * Validates that the nickname and message fields are not empty.
     * Displays an error dialog if any field is empty.
     */
    private void sendMessage() {
        String message = messageInput.getText();
        String userNick = userNickInput.getText();

        if (!message.trim().isEmpty() && !userNick.trim().isEmpty()) {
            String timeStamp = getCurrentTimeStamp();
            Message newMessage = new Message(timeStamp, userNick, message);

            // Ajout du message à loadedMessages
            if (loadedMessages == null) {
                loadedMessages = new ArrayList<>();
            }
            loadedMessages.add(newMessage);

            // Affichage du message
            renderMessage(newMessage);
            messageInput.clear();
        } else {
            showErrorDialog("Erreur", "Le message ou le pseudo ne peut pas être vide.");
        }
    }

    /**
     * Loads a conversation from a file.
     * Opens a file dialog for the user to select a file.
     * Validates and parses the file content into messages.
     * Displays an error dialog if the file is invalid or cannot be loaded.
     *
     * @param stage the primary stage for displaying the file chooser.
     */
    private void loadConversation(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier de conversation");

        // Utiliser le dernier répertoire ouvert si disponible
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            lastOpenedDirectory = file.getParentFile(); // Mémorise le répertoire du fichier ouvert
            try {
                ConversationParser parser = new ConversationParser();
                loadedMessages = parser.parseFile(file); // Réinitialise la liste avec les messages chargés
                conversationView.getChildren().clear();
                for (Message message : loadedMessages) {
                    renderMessage(message);
                }
            } catch (Exception e) {
                showErrorDialog("Erreur de chargement", "Impossible de charger la conversation.");
            }
        }
    }

    /**
     * Saves the current conversation to a file.
     * Opens a file dialog for the user to specify the save location.
     * Displays an error dialog if no messages are loaded or the save fails.
     *
     * @param stage the primary stage for displaying the file chooser.
     */
    private void saveConversation(Stage stage) {
        if (loadedMessages == null || loadedMessages.isEmpty()) {
            showErrorDialog("Erreur", "Aucun message à sauvegarder.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la conversation");

        // Utiliser le dernier répertoire ouvert si disponible
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            lastOpenedDirectory = file.getParentFile(); // Mémorise le répertoire du fichier sauvegardé
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Message message : loadedMessages) {
                    writer.write("Time: " + message.getTimeStamp() + "\n");
                    writer.write("Name: " + message.getNickName() + "\n");
                    writer.write("Message: " + message.getContent() + "\n\n");
                }
                showSuccessDialog("Succès", "La conversation a été sauvegardée avec succès.");
            } catch (IOException e) {
                showErrorDialog("Erreur", "Erreur lors de la sauvegarde du fichier.");
            }
        }
    }

    /**
     * Renders a message in the conversation view.
     * Uses a `MessageRenderer` to format and display the message.
     *
     * @param message the message to be rendered.
     */
    private void renderMessage(Message message) {
        // Si le pseudo est le même que le précédent, remplacez-le par "..."
        if (message.getNickName().equals(lastUserNick)) {
            message = new Message(message.getTimeStamp(), "...", message.getContent());
        } else {
            lastUserNick = message.getNickName(); // Mettez à jour le pseudo précédent
        }

        MessageRenderer renderer = new MessageRenderer();
        renderer.renderMessage(conversationView, message);
    }

    /**
     * Displays an error dialog with the specified title and message.
     *
     * @param title   the title of the dialog.
     * @param message the content message of the dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success dialog with the specified title and message.
     *
     * @param title   the title of the dialog.
     * @param message the content message of the dialog.
     */
    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Pas de texte d'en-tête
        alert.setContentText(message);  // Le message de succès
        alert.showAndWait();
    }

    /**
     * Gets the current timestamp in the format `yyyy-MM-dd HH:mm:ss`.
     *
     * @return the current timestamp as a string.
     */
    private String getCurrentTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new java.util.Date());
    }

    private String lastUserNick = null;
    
}
