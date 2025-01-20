package com.example.chatviewer;

/**
 * Custom exception to handle invalid message format errors during
 * conversation parsing.
 *
 * This exception is thrown when a message in the conversation file
 * does not adhere to the expected format. The format requires the following:
 *
 * <ul>
 *   <li>A valid timestamp in the format "yyyy-MM-dd HH:mm:ss".</li>
 *   <li>A non-empty user nickname.</li>
 *   <li>A non-empty message content.</li>
 * </ul>
 */
public class InvalidMessageFormatException extends Exception {

    /**
     * Constructs a new `InvalidMessageFormatException` with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception.
     */
    public InvalidMessageFormatException(String message) {
        super(message);
    }
}
