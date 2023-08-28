package jm.task.core.service;

import jm.task.core.model.Note;

import java.util.List;
import java.util.Scanner;

public interface NoteService {
    void getListOfCommands();

    Note createNewNote();

    List<Note> getNoteList();

    void removeNote();

    void exportNote();

    void exit(Scanner scanner);
}
