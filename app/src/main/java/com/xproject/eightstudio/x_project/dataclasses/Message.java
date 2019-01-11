package com.xproject.eightstudio.x_project.dataclasses;

/*
 *  This class is written by
 *  (C) Dmitrii Lykov, 2019
 */

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