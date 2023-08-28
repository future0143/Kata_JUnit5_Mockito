import jm.task.core.dao.NoteDAOlmpl;
import jm.task.core.model.Note;
import jm.task.core.service.NoteService;
import jm.task.core.service.NoteServiceimpl;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {
    private final NoteService noteService = new NoteServiceimpl();

    @Test
    public void uniqueId() {      //проверка id на уникальность    +++
        String inputFirst = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(inputFirst.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        Note firstNote = noteService.createNewNote();

        String inputSecond = "Another Text\nOne two";
        InputStream inputStream = new ByteArrayInputStream(inputSecond.getBytes());
        System.setIn(inputStream);

        Note secondNote = noteService.createNewNote();
        assertNotEquals(firstNote.getId(), secondNote.getId());
    }

    @Test
    public void createNewNoteWithValidInput() {     //Создание заметки из введенных корректных данных   +++
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Note createdNote = noteService.createNewNote();

        assertEquals("New Text", createdNote.getText());
        assertTrue(createdNote.getLabels().get(0).equalsIgnoreCase("first"));
        assertTrue(createdNote.getLabels().get(1).equalsIgnoreCase("second"));
    }

    @Test
    public void addingNoteToList() {            //Проверка добавления заметки в список    +++
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        List<Note> mockListOfNotes = new ArrayList<>();
        Whitebox.setInternalState(Note.class, "listOfNotes", mockListOfNotes);

        noteService.createNewNote();

        assertEquals(1, mockListOfNotes.size());
    }

    @Test
    public void deletingNoteWithValidId() {      //Удаление заметки с корректным id     +++
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        List<Note> mockListOfNotes = new ArrayList<>();
        Whitebox.setInternalState(Note.class, "listOfNotes", mockListOfNotes);

        Note newNote = noteService.createNewNote();

        int id = newNote.getId();
        InputStream inputStream = new ByteArrayInputStream(String.valueOf(id).getBytes());
        System.setIn(inputStream);

        noteService.removeNote();
        assertEquals(0, mockListOfNotes.size());
    }

    @Test
    public void deletingNoteWithInvalidId() {      //Удаление заметки с некорректным id    +++
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        List<Note> mockListOfNotes = new ArrayList<>();
        Whitebox.setInternalState(Note.class, "listOfNotes", mockListOfNotes);

        noteService.createNewNote();

        String id = "ab";
        InputStream inputStream = new ByteArrayInputStream(id.getBytes());
        System.setIn(inputStream);

        noteService.removeNote();
        assertEquals(1, mockListOfNotes.size());
    }

    @Test
    public void createNewNoteWithInvalidText() {     //Невозможность создания заметки с некорректным текстом   +++
        String input = "Ne";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        List<Note> mockListOfNotes = new ArrayList<>();
        Whitebox.setInternalState(Note.class, "listOfNotes", mockListOfNotes);

        noteService.createNewNote();

        assertEquals(0, mockListOfNotes.size());
    }

    @Test
    public void createNewNoteWithInvalidLabels() {      //Невозможность создания заметки с некорректными лэйблами
        String input = "New Text\n1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        List<Note> mockListOfNotes = new ArrayList<>();
        Whitebox.setInternalState(Note.class, "listOfNotes", mockListOfNotes);

        noteService.createNewNote();

        assertEquals(0, mockListOfNotes.size());
    }

    @Test
    public void searchNoteById() {                     //поиск заметки по лэйблу существующей заметки    +++
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        Note newNote = noteService.createNewNote();

        String lab = newNote.getLabels().get(1);
        InputStream inputStream = new ByteArrayInputStream(lab.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        List<Note> list = noteService.getNoteList();

        assertTrue(list.stream().allMatch(note -> note.getLabels().contains(lab)));    //проверка что все из найденных заметок содержат введенный лэйбл
    }

    @Test
    public void searchNoteByNonExistentId() {                     //поиск заметки по несуществующему лэйблу
        String input = "New Text\nfirst second";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        Note newNote = noteService.createNewNote();

        String lab = "abc";
        InputStream inputStream = new ByteArrayInputStream(lab.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        List<Note> list = noteService.getNoteList();

        assertTrue(list.isEmpty());    //проверка что все из найденных заметок содержат введенный лэйбл
    }
}
