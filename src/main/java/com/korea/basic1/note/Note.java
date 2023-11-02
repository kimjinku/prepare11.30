package com.korea.basic1.note;

import com.korea.basic1.postService.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    private String title;
    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE)
    private List<Post> posts;


}
