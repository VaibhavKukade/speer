package com.speer.notes.repository;

import com.speer.notes.model.Notes;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Notes, String> {
    List<Notes> findByUsername(String username);
    Optional<Notes> findByIdAndUsername(String id, String username);
}

