package com.speer.notes.controller;

import com.speer.notes.model.Notes;
import com.speer.notes.repository.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Notes> getAllNotesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the authenticated user
        return noteRepository.findByUsername(username);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getNoteById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the authenticated user
        Optional<Notes> note = noteRepository.findByIdAndUsername(id, username);
        return note.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> createNote(@RequestBody Notes note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the authenticated user
        note.setUsername(username);
        noteRepository.save(note);
        return ResponseEntity.ok("Note created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> updateNote(@PathVariable String id, @RequestBody Notes updatedNote) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the authenticated user
        Optional<Notes> existingNote = noteRepository.findByIdAndUsername(id, username);
        if (existingNote.isPresent()) {
            updatedNote.setUsername(username);
            updatedNote.setId(id);
            noteRepository.save(updatedNote);
            return ResponseEntity.ok("Note updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteNote(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the authenticated user
        Optional<Notes> existingNote = noteRepository.findByIdAndUsername(id, username);
        if (existingNote.isPresent()) {
            noteRepository.deleteById(id);
            return ResponseEntity.ok("Note deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

