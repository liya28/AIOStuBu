package io.serateam.stewboo.core.services.notes;

import io.serateam.stewboo.core.utility.ISerializable;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.io.File;

public class Note implements ISerializable {
    private String title;
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void save() {
        String filePath = SharedVariables.Path.notesDirectory + title + ".json";
        JSONService.serializeAndWriteToFile(filePath, this);
    }

    public static Note load(String title) {
        String filePath = SharedVariables.Path.notesDirectory + title + ".json";
        return JSONService.deserialize(filePath, Note.class);
    }

    public static NotesList loadAll() {
        NotesList notesList = new NotesList();
        File folder = new File(SharedVariables.Path.notesDirectory);
        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".json")));
        if (files != null) {
            for (File file : files) {
                Note note = JSONService.deserialize(file.getPath(), Note.class);
                if (note != null) {
                    notesList.add(note);
                }
            }
        }
        return notesList;
    }

    public static Note findByTitle(String title) {
        return load(title);
    }

    public static void deleteByTitle(String title) {
        File file = new File(SharedVariables.Path.notesDirectory + title + ".json");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted note: " + title);
            } else {
                System.out.println("Failed to delete note: " + title);
            }
        } else {
            System.out.println("Note not found: " + title);
        }
    }
}

