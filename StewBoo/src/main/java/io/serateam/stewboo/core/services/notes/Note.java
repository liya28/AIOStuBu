package io.serateam.stewboo.core.services.notes;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Note {
    private String title;
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void saveNoteToFile(File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Note loadNoteFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String content = String.join("\n", lines);
            return new Note(file.getName(), content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

