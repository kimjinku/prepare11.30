package com.korea.basic1.postService;

import com.korea.basic1.postService.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
List<Post> findByTitleContainingOrContentContaining(String title,String content);

}
