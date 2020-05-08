package util;

/**
 * Types of the messages between client and server
 */
public enum MessageType {
    /**
     * Message from Controller to View
     */
    CV,

    /**
     * Message from Model to View
     */
    MV,

    /**
     * Message from View to Controller
     */
    VC,

    /**
     * System message from Client to Server
     */
    SYS
}