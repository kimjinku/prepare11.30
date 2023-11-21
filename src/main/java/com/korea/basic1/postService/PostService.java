package com.korea.basic1.postService;

import com.korea.basic1.note.Note;
import com.korea.basic1.note.NoteRepository;
import com.korea.basic1.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    public List<Post> getPostList(){
        return postRepository.findAll();
    }
    public Post getPost(Long postId){
        Optional<Post> post = this.postRepository.findById(postId);
        if(post.isPresent()){
            return post.get();
        }else{
            throw new RuntimeException("post not found"); //Todo
        }
    }
    //Note note = noteRepository.findById(noteId).get();
    //        List<Post> postListForNote = note.getPosts();
    public List<Post> getPostInNote(Long noteId){
        return noteService.getNote(noteId).getPosts();
    }
    public void deletePost(Long postId){
        postRepository.delete(getPost(postId));
    }
    public Post addPost(Long noteId){
        Post post = new Post();
        Note note = noteService.getNote(noteId);
        post.setTitle("newTitle");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);
        postRepository.save(post);
        return post;
    }
}
