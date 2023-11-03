package com.korea.basic1.note;

import com.korea.basic1.postService.Post;
import com.korea.basic1.postService.PostController;
import com.korea.basic1.postService.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class NoteController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    PostController postController;

    @RequestMapping("/note")
    public String main(Model model,@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        List<Post> postListForNote = noteList.get(0).getPosts();
        if (keyword != null && !keyword.isEmpty()) {
            List<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword,keyword);
            model.addAttribute("searchResults", searchResults);
        } else {
            model.addAttribute("searchResults", Collections.emptyList()); // 빈 결과를 전달
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("postList",postListForNote);
        model.addAttribute("targetPost", postList.get(0));
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));
        return "main";
    }
    @PostMapping("/noteWrite")
    public String noteWrite(Long noteId,Long postId) {
        Note note = new Note();
        List<Post> postList = new ArrayList<>();
        note.setPosts(postList);
        note.setTitle("새 노트");
        noteRepository.save(note);
        postController.write(noteRepository.findMaxNoteId(), postId);
        return "redirect:/noteDetail/"+noteId+"/"+postId;
    }

    @GetMapping("/noteDetail/{noteId}/{postId}")
    public String noteDetail(Model model, @PathVariable Long postId, @PathVariable Long noteId,@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Post post = postRepository.findById(postId).get();
        Note note = noteRepository.findById(noteId).get();
        List<Post> postListForNote = note.getPosts();
        if (keyword != null && !keyword.isEmpty()) {
            List<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword,keyword);
            model.addAttribute("searchResults", searchResults);
        } else {
            model.addAttribute("searchResults", Collections.emptyList()); // 빈 결과를 전달
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("targetPost", post);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("noteList", noteRepository.findAll());
        model.addAttribute("targetNote", note);

        return "main";
    }

    @GetMapping("/noteDelete")
    public String noteDelete(Long noteId) {
        Note note = noteRepository.findById(noteId).get();
        noteRepository.delete(note);
        return "redirect:/note";
    }


}
