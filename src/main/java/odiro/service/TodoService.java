package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Member;
import odiro.domain.Todo;
import odiro.repository.MemberRepository;
import odiro.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Long makeTodo(Todo todo) {
        todoRepository.save(todo);
        return todo.getId();
    }

    public Optional<Todo> findById(Long todoId) {
        return todoRepository.findById(todoId);
    }

}
