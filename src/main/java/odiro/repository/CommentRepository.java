package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}