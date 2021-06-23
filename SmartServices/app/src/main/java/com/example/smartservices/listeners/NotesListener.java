package com.example.smartservices.listeners;


import com.example.smartservices.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
