package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.Note;
import com.rithikaa.notora.model.User;
import com.rithikaa.notora.repository.NoteRepository;
import com.rithikaa.notora.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    private final NoteRepository noteRepository;

    public DashboardController(
                           NoteRepository noteRepository) {

        this.noteRepository = noteRepository;
    }
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("points", user.getPoints());

        // 🔹 Total notes
        long totalNotes = noteRepository.countByUser(user);
        model.addAttribute("totalNotes", totalNotes);

        // 🔹 Recent 2 activities
        List<Note> recentNotes =
                noteRepository.findTop2ByUserOrderByUploadedAtDesc(user);
        model.addAttribute("recentNotes", recentNotes);

        // 🔹 My Notes (latest uploads)
        List<Note> myNotes =
                noteRepository.findTop5ByUserOrderByUploadedAtDesc(user);
        model.addAttribute("myNotes", myNotes);

        return "dashboard";
    }

}
