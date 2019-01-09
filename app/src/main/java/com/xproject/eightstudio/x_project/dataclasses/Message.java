package com.xproject.eightstudio.x_project.dataclasses;

public class Message {
    String message;
    String worker;
    short isLocal;

    public Message(String _worker, String _message, short _isLocal) {
        message = _message;
        worker = _worker;
        isLocal = _isLocal;
    }
}