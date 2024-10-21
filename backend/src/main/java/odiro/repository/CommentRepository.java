package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByDayPlanIdOrderByWriteTimeDesc(Long dayPlanId, Pageable pageable);
}