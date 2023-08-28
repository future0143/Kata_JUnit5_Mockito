package jm.task.core.dao;

import jm.task.core.model.Note;
import lombok.extern.java.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
public class NoteDAOlmpl implements NoteDAO {

    @Override
    public void getListOfCommands() {
        log.fine("Вызвана команда help");
        for (String commands : Commands.getAllCommands()) {
            System.out.println(commands);
        }
    }

    @Override
    public Note createNewNote() {
        log.fine("Вызвана команда note-new");
        Note newNote = null;

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Введите текст заметки: ");
            String text = scanner.nextLine();
            if (text.length() < 3) {
                log.info("Длина заметки должна быть не менее 3 символов, введено - " + text);
                throw new IllegalArgumentException("Длина заметки должна быть не менее 3 символов.");
            }

            System.out.println("Введите лэйблы: ");
            String label = scanner.nextLine();
            List<String> labels = null;
            if (!label.isEmpty()) {
                labels = List.of(label.toUpperCase().split(" "));
            }
            if (checkLabels(labels) == true) {
                newNote = new Note(text, labels);
                Note.getListOfNotes().add(newNote);
                log.info("Заметка добавлена");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return newNote;
    }

    @Override
    public List<Note> getNoteList() {
        log.fine("Вызвана команда note-list");

        List<String> listLabels;
        List<Note> listNote = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String label;
        System.out.println("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок");

        try {
            label = scanner.nextLine();
            if (!label.isEmpty()) {
                listLabels = List.of(label.toUpperCase().split(" "));
                if (checkLabels(listLabels) == true) {
                    for (Note note : Note.getListOfNotes()) {
                        for (String l : listLabels) {
                            if (note.getLabels().contains(l)) {
                                listNote.add(note);
                            } else {
                                log.info("Метка \"" + l + "\" отсутствует в заметках");
                                throw new IllegalArgumentException("Введенная метка отсутствует в заметках");
                            }
                        }
                    }
                }
            } else {
                listNote = Note.getListOfNotes();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        for (Note note : listNote) {
            System.out.println(note + "\n" + "===================");
        }
        return listNote;
    }


    @Override
    public void removeNote() {
        log.fine("Вызвана команда note-remove");

        Scanner scanner = new Scanner(System.in);
        int id;
        System.out.println("Введите id удаляемой заметки");
        try {
            String idLine = scanner.nextLine();
            id = Integer.parseInt(idLine);
        } catch (NumberFormatException e) {
            log.info("Введен некорректный id");
            return;
        }
        Iterator<Note> iterator = Note.getListOfNotes().iterator();
        while (iterator.hasNext()) {
            Note note = iterator.next();
            if (note.getId() == id) {
                iterator.remove();
                log.info("Заметка удалена");
            } else {
                log.info("Заметка не найдена");
            }
        }
    }

    @Override
    public void exportNote() {
        log.fine("Вызвана команда note-export");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = "notes_" + dateFormat.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Note note : Note.getListOfNotes()) {
                writer.write(note + "\n" + "===================" + "\n");
            }
            log.info("Заметки успешно сохранены в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи заметок: " + e.getMessage());
        }
    }

    @Override
    public void exit(Scanner scanner) {
        log.fine("Вызвана команда exit");
        scanner.close();
    }

    public boolean checkLabels(List<String> listLabels) {    //проверка лэйблов на соответствие регулярному выражению
        boolean checkLab = false;

        String regular = "^[a-zA-Z]+$";
        for (String lab : listLabels) {
            if (!lab.matches(regular)) {
                checkLab = false;
                log.info("Метка \"" + lab + "\" некорректна. Метки должны состоять только из букв.");
                throw new IllegalArgumentException("Метка \"" + lab + "\" некорректна. Метки должны состоять только из букв.");
            } else {
                checkLab = true;
            }
        }
        return checkLab;
    }

}