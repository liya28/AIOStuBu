package io.serateam.stewboo.core.services.notes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotesList implements Iterable<Note>
{
    private final List<Note> notes = new ArrayList<>();

    public void add(Note note)
    {
        notes.add(note);
    }

    public void remove(Note note)
    {
        notes.remove(note);
    }

    public List<Note> getNotes()
    {
        return notes;
    }

    @Override
    public Iterator<Note> iterator()
    {
        return notes.iterator();
    }
}
