
package com.rithikaa.notora.repository;

import com.rithikaa.notora.model.Note;
import com.rithikaa.notora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    long countByUser(User user);

    List<Note> findTop2ByUserOrderByUploadedAtDesc(User user);

    List<Note> findAllByOrderByUploadedAtDesc();
    List<Note> findBySubjectContainingIgnoreCaseOrFileNameContainingIgnoreCase(
            String subject, String fileName);

    List<Note> findBySubjectContainingIgnoreCase(String keyword);
    List<Note> findTop5ByUserOrderByUploadedAtDesc(User user);
}

