package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.Note;
import com.rithikaa.notora.model.User;
import com.rithikaa.notora.repository.NoteRepository;
import com.rithikaa.notora.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SharedNotesController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public SharedNotesController(NoteRepository noteRepository,
                                 UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    /* ===================== SHARED NOTES PAGE ===================== */

    @GetMapping("/shared-notes")
    public String sharedNotes(
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Note> notes;

        if (search != null && !search.trim().isEmpty()) {
            notes = noteRepository.findBySubjectContainingIgnoreCaseOrFileNameContainingIgnoreCase(
                    search, search
            );
        } else {
            notes = noteRepository.findAllByOrderByUploadedAtDesc();
        }

        model.addAttribute("notes", notes);
        model.addAttribute("user", user);
        model.addAttribute("search", search);

        return "shared-notes";
    }

    /* ===================== FAVORITE TOGGLE ===================== */

    @PostMapping("/favorite")
    @ResponseBody
    public void toggleFavorite(@RequestParam Long noteId, HttpSession session) {

        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) return;

        User user = userRepository.findById(sessionUser.getId()).orElseThrow();
        Note note = noteRepository.findById(noteId).orElseThrow();

        if (user.getFavoriteNotes().contains(note)) {
            user.getFavoriteNotes().remove(note);
        } else {
            user.getFavoriteNotes().add(note);
        }

        userRepository.save(user);
    }

    /* ===================== PREVIEW PAGE (HTML) ===================== */

    @GetMapping("/preview/{id}")
    public String previewNote(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Note note = noteRepository.findById(id).orElseThrow();
        model.addAttribute("note", note);

        return "preview-note";
    }

    /* ===================== LIVE SEARCH API ===================== */

    @GetMapping("/api/notes/search")
    @ResponseBody
    public List<Note> liveSearch(@RequestParam(required = false) String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return noteRepository.findAllByOrderByUploadedAtDesc();
        }

        return noteRepository
                .findBySubjectContainingIgnoreCaseOrFileNameContainingIgnoreCase(
                        keyword.trim(), keyword.trim()
                );
    }
}
