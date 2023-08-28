package jm.task.core.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Note {
    @Getter
    private static List<Note> listOfNotes = new ArrayList<>();
    private static int count = 0;
    private int id;
    private String text;
    private List<String> labels;

    public Note(String text, List<String> labels) {
        this.id = generateId();
        this.text = text;
        this.labels = labels;
        System.out.println(this);
    }

    public int getId() {
        return id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return id == note.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("#");
        sb.append(text + "\n");

        if (labels != null && !labels.isEmpty()) {
            for (String label : labels) {
                sb.append(label).append("; ");
            }
            sb.setLength(sb.length() - 2); // Убираем последнюю ;
        }
        return sb.toString();
    }

    public static int generateId() {
        return ++count;
    }
}
