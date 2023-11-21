package com.korea.basic1.postService;

import com.korea.basic1.note.Note;
import com.korea.basic1.note.NoteRepository;
import com.korea.basic1.user.SiteUser;
import com.korea.basic1.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

//질문사항
//검색기능 오류
//확인메시지
// 마크다운이....?

@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String main(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword, @RequestParam(value = "page", defaultValue = "1") int page, Pageable pageable) {
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        List<Post> postListForNote = noteList.get(0).getPosts();
        int size = 10;
        pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            Page<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            model.addAttribute("searchResults", searchResults);
        } else {
            model.addAttribute("searchResults", Collections.emptyList()); // 빈 결과를 전달
        }
        Page<Note> searchNoteResults = noteRepository.findByPosts_TitleContainingOrPosts_ContentContaining(keyword, keyword, pageable);
        model.addAttribute("searchNoteResults", searchNoteResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("targetPost", postList.get(0));
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));
        return "main";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(Long noteId, Long postId, Principal principal) {
        Post post = new Post();
        Note note = noteRepository.findById(noteId).get();
        SiteUser siteUser = userService.getUser(principal.getName());
        post.setTitle("newTitle");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);
        post.setAuthor(siteUser);
        postRepository.save(post);
        return "redirect:detail/" + noteId + "/" + postId;
    }

    @GetMapping("/detail/{noteId}/{postId}")
    public String detail(Model model, @PathVariable Long postId, @PathVariable Long noteId, @RequestParam(value = "keyword", defaultValue = "") String keyword, @RequestParam(value = "page", defaultValue = "1") int page, Pageable pageable) {
        Post post = postRepository.findById(postId).get();
        Note note = noteRepository.findById(noteId).get();
        List<Post> postListForNote = note.getPosts();
        int size = 10;
        pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            Page<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            model.addAttribute("searchResults", searchResults);
        } else {
            model.addAttribute("searchResults", Collections.emptyList()); // 빈 결과를 전달
        }
        Page<Note> searchNoteResults = noteRepository.findByPosts_TitleContainingOrPosts_ContentContaining(keyword, keyword, pageable);
        model.addAttribute("searchNoteResults", searchNoteResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("targetPost", post);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("noteList", noteRepository.findAll());
        model.addAttribute("targetNote", note);

        return "main";
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String delete(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam(value = "keyword", defaultValue = "") String keyword, Model model, @RequestParam(value = "page", defaultValue = "1") int page, Pageable pageable) {
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        int size = 10;
        pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            Page<Post> searchResults = postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            model.addAttribute("searchResults", searchResults);
        } else {
            model.addAttribute("searchResults", Collections.emptyList()); // 빈 결과를 전달
        }
        Page<Note> searchNoteResults = noteRepository.findByPosts_TitleContainingOrPosts_ContentContaining(keyword, keyword, pageable);
        model.addAttribute("searchNoteResults", searchNoteResults);
        List<Post> postListForNote = noteList.get(0).getPosts();
        model.addAttribute("keyword", keyword);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("targetPost", postList.get(0));
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));

        return "main";
    }


}



