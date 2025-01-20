package com.example.chatviewer;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessageRenderer {

    /**
     * Renders a message in a specified `TextFlow` component.
     * The message is displayed with formatted elements:
     * - Timestamp in normal font weight.
     * - Nickname in blue color.
     * - Message content in bold font.
     *
     * @param textFlow the `TextFlow` component where the message will be displayed.
     * @param message  the `Message` object containing the timestamp, nickname, and content.
     */
    public void renderMessage(TextFlow textFlow, Message message) {
        Text timeText = new Text("[" + message.getTimeStamp() + "] ");
        Text nameText = new Text(message.getNickName() + ": ");
        Text contentText = new Text(message.getContent());

        timeText.setStyle("-fx-font-weight: normal;");
        nameText.setStyle("-fx-fill: blue;");
        contentText.setStyle("-fx-font-weight: bold;");

        textFlow.getChildren().addAll(timeText, nameText, contentText, new Text("\n"));
    }
}

