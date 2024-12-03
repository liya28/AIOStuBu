package io.serateam.stewboo.core.services.notes;


import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotesService implements IService
{

    private static NotesService instance;

    public static NotesService getInstance()
    {
        if (instance == null)
        {
            instance = new NotesService();
        }
        return instance;
    }

    @Override
    public void initializeService()
    {
        if (!new File(SharedVariables.Path.notesDirectory).exists())
        {
            try
            {
                Files.createDirectories(Paths.get(SharedVariables.Path.notesDirectory));
                System.out.println("Successfully created " + SharedVariables.Path.notesDirectory);
            }
            catch (IOException e)
            {
                System.err.println("Failed to create directory: " + SharedVariables.Path.notesDirectory);
            }
        }
    }

    public List<String> getAllTitles()
    {
        NotesList notesList = Note.loadAll();
        return notesList != null
                ? notesList.getNotes().stream().map(Note::getTitle).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public String getContentByTitle(String title)
    {
        Note note = Note.findByTitle(title);
        return note != null ? note.getContent() : null;
    }

    public void saveNote(String title, String content) {
        Note note = new Note(title, content);
        note.save();
    }

    public void deleteNoteByTitle(String title)
    {
        Note.deleteByTitle(title);
    }
}
