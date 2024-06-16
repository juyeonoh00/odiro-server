package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}