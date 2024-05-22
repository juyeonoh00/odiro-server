package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
