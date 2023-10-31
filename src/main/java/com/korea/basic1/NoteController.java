package com.korea.basic1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NoteController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    NoteRepository noteRepository;

    @RequestMapping("/note")
    public String main(Model model) {
        List<Note> noteList = noteRepository.findAll();
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);
        model.addAttribute("targetPost",postList);
        model.addAttribute("noteList", noteList);
        return "main";
    }


}
