package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Todo;


public interface TodoRepository extends JpaRepository<Todo, Long> {
}
