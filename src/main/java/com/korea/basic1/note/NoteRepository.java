package com.korea.basic1.note;

import com.korea.basic1.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoteRepository extends JpaRepository<Note,Long> {
    @Query("SELECT MAX(n.noteId) FROM Note n")
    Long findMaxNoteId();
}
