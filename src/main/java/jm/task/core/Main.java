package jm.task.core;

import jm.task.core.service.NoteServiceimpl;

public class Main {
    public static void main(String[] args) {
        NoteServiceimpl noteServiceimpl = new NoteServiceimpl();
        noteServiceimpl.startApp();
    }
}
