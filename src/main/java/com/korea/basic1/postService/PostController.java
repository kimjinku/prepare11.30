package com.korea.basic1.postService;

import com.korea.basic1.note.Note;
import com.korea.basic1.note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    NoteRepository noteRepository;

    @RequestMapping("/")
    public String main(Model model) {
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        List<Post> postListForNote = noteList.get(0).getPosts();
        model.addAttribute("postList",postListForNote);
        model.addAttribute("targetPost", postList.get(0));
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));
        return "main";
    }

    @PostMapping("/write")
    public String write( Long noteId, Long postId) {
        Post post = new Post();
        Note note = noteRepository.findById(noteId).get();
        post.setTitle("newTitle");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);
        postRepository.save(post);
        return "redirect:detail/"+noteId+"/"+postId;
    }

    @GetMapping("/detail/{noteId}/{postId}")
    public String detail(Model model, @PathVariable Long postId, @PathVariable Long noteId) {
        Post post = postRepository.findById(postId).get();
        Note note = noteRepository.findById(noteId).get();
        List<Post> postListForNote = note.getPosts();
        model.addAttribute("targetPost", post);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("noteList", noteRepository.findAll());
        model.addAttribute("targetNote", note);

        return "main";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long postId, String title, String content) {
        Post post = postRepository.findById(postId).get();
        post.setTitle(title);
        post.setContent(content);
        if (post.getTitle().equals("")) {
            post.setTitle("제목없음");
        }
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "redirect:/";
    }
    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model) {
        List<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword,keyword);
        model.addAttribute("searchResults", searchResults);
        return "redirect:/"; // Thymeleaf 템플릿 이름
    }
}



