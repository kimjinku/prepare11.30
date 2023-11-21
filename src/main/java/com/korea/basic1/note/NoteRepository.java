package com.korea.basic1.note;

import com.korea.basic1.note.Note;
import com.korea.basic1.postService.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    @Query("SELECT MAX(n.noteId) FROM Note n")
    Long findMaxNoteId();
    Page<Note> findByPosts_TitleContainingOrPosts_ContentContaining(String title, String content,Pageable pageable);
}

