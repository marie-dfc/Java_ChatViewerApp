package com.example.chatviewer;

/**
 * Represents a single chat message with a timestamp, user nickname, and content.
 *
 * This class is used to encapsulate the details of a chat message and provides
 * methods to retrieve its attributes.
 */
public class Message {
    private String timeStamp; // Contient la date et l'heure
    private String nickName;
    private String content;

    /**
     * Constructs a new `Message` object with the specified timestamp, nickname, and content.
     *
     * @param timeStamp the timestamp of the message in "yyyy-MM-dd HH:mm:ss" format.
     * @param nickName the nickname of the user who sent the message.
     * @param content the content of the message.
     */
    public Message(String timeStamp, String nickName, String content) {
        this.timeStamp = timeStamp;
        this.nickName = nickName;
        this.content = content;
    }

    /**
     * Retrieves the timestamp of the message.
     *
     * @return the timestamp in "yyyy-MM-dd HH:mm:ss" format.
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Retrieves the nickname of the user who sent the message.
     *
     * @return the user's nickname.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Retrieves the content of the message, replacing certain emoticon patterns with emojis.
     *
     * <ul>
     *   <li><code>:)</code> is replaced with 😊</li>
     *   <li><code>:(</code> is replaced with 😞</li>
     * </ul>
     *
     * @return the message content with any emoticon replacements applied.
     */
    public String getContent() {
        return content.replace(":)", "😊").replace(":(", "😞");
    }
}
