package jm.task.core.dao;

import java.util.ArrayList;
import java.util.List;

public enum Commands {
    LISTOFCOMMANDS("help", "выводит на экран список доступных команд с их описанием"),
    NOTENEW("note-new", "создать новую заметку"),
    NOTELIST("note-list", "выводит все заметки на экран"),
    NOTEREMOVE("note-remove", "удаляет заметку"),
    NOTEEXPORT("note-export", "сохраняет все заметки в текстовый файл и выводит имя сохраненного файла"),
    EXIT("exit", "выход из приложения");
    private String command;
    private String description;

    public String getDescription() {
        return description;
    }

    Commands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public static List<String> getAllCommands() {
        Commands[] values = Commands.values();
        List<String> commands = new ArrayList<>();
        for (Commands value : values) {
            commands.add(value.getCommand() + ": " + value.getDescription());
        }
        return commands;
    }
}
