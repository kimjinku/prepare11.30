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
        return "main";
    }
    @PostMapping("/write")
    public String write(){
        Post post = new Post();
        post.setTitle("newTitle");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(post.getNote());
        postRepository.save(post);
        return "redirect:/";
    }
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id){
        Post post = postRepository.findById(id).get();
        model.addAttribute("targetPost",post);
        model.addAttribute("postList",postRepository.findAll());
        model.addAttribute("noteList", noteRepository.findAll());
        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content){
        Post post = postRepository.findById(id).get();
        post.setTitle(title);
        post.setContent(content);
        if(post.getTitle().equals("")){
            post.setTitle("제목없음");
        }
        postRepository.save(post);
        return "redirect:/detail/"+id;
    }
    @GetMapping("/delete")
    public String delete(Long id){
        Post post = postRepository.findById(id).get();
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
        return "redirect:/note";
    }

    @GetMapping("/noteDetail/{id}")
    public String noteDetail(Model model,@PathVariable Long id) {
        List<Note> noteList = noteRepository.findAll();
        Note note = noteRepository.findById(id).get();
        Post post = postRepository.findById(id).get();
        model.addAttribute("targetPost",post);
        model.addAttribute("postList",postRepository.findAll());
        model.addAttribute("noteList", noteList);
        return "main";
    }

}
