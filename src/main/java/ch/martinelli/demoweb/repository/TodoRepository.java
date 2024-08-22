package ch.martinelli.demoweb.repository;

import ch.martinelli.demoweb.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    @Query("select max(t.id) from Todo t")
    Optional<Integer> findMaxId();
}
