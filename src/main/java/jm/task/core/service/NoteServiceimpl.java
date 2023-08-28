package jm.task.core.service;

import jm.task.core.dao.NoteDAO;
import jm.task.core.dao.NoteDAOlmpl;
import jm.task.core.model.Note;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Scanner;


@Log
public class NoteServiceimpl implements NoteService {
    private NoteDAO noteDao = new NoteDAOlmpl();

    public void startApp() {
        System.out.println("Это Ваша записная книжка. Вот список доступных команд: help,note-new,note-list,note-remove,note-export, exit.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            switch (line) {
                case "help" -> getListOfCommands();
                case "note-new" -> createNewNote();
                case "note-list" -> getNoteList();
                case "note-remove" -> removeNote();
                case "note-export" -> exportNote();
                case "exit" -> exit(scanner);
                default -> {
                    log.warning("введена неверная команда");
                    log.info("команда не найдена");
                }   //+
            }
        }
    }

    @Override
    public void getListOfCommands() {
        noteDao.getListOfCommands();
    }

    @Override
    public Note createNewNote() {
        return noteDao.createNewNote();
    }

    @Override
    public List<Note> getNoteList() {
        return noteDao.getNoteList();
    }

    @Override
    public void removeNote() {
        noteDao.removeNote();
    }

    @Override
    public void exportNote() {
        noteDao.exportNote();
    }

    @Override
    public void exit(Scanner scanner) {
        noteDao.exit(scanner);
    }
}
