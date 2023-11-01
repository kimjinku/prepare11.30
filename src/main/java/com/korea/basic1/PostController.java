package com.korea.basic1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    NoteRepository noteRepository;
    @RequestMapping("/")
    public String main(Model model){
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        model.addAttribute("postList",postList);
        model.addAttribute("targetPost",postList.get(0));
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote",noteList.get(0));
        return "main";
    }

    @PostMapping("/write")
    public String write(@RequestParam Long noteId){
        Post post = new Post();
        Note note = noteRepository.findById(noteId).get();
        post.setTitle("newTitle");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);
        postRepository.save(post);
        return "redirect:/";
    }
    @GetMapping("/detail/{noteId}/{postId}")
    public String detail(Model model, @PathVariable Long postId,@PathVariable Long noteId){
        Post post = postRepository.findById(postId).get();
        Note note = noteRepository.findById(noteId).get();
        model.addAttribute("targetPost",post);
        model.addAttribute("postList",postRepository.findAll());
        model.addAttribute("noteList", noteRepository.findAll());
        model.addAttribute("targetNote",note);
        return "main";
    }
    @PostMapping("/update")
    public String update(@RequestParam Long postId, String title, String content){
        Post post = postRepository.findById(postId).get();
        post.setTitle(title);
        post.setContent(content);
        if(post.getTitle().equals("")){
            post.setTitle("제목없음");
        }
        postRepository.save(post);
        return "redirect:/";
    }
    @GetMapping("/delete")
    public String delete(Long postId){
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "redirect:/";
    }
    @PostMapping("/noteWrite")
    public String noteWrite() {
        Note note = new Note();
        List<Post> postList = new ArrayList<>();
        note.setPosts(postList);
        note.setTitle("새 노트");
        noteRepository.save(note);
        return "redirect:/";
    }

    @GetMapping("/noteDetail/{noteId}/{postId}")
    public String noteDetail(Model model,@PathVariable Long noteId,@PathVariable Long postId) {
        List<Note> noteList = noteRepository.findAll();
        Post post = postRepository.findById(postId).get();
        Note note = noteRepository.findById(noteId).get();
        model.addAttribute("targetPost",post);
        model.addAttribute("postList",postRepository.findAll());
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote",note);
        return "main";
    }

}
