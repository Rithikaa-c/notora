package com.rithikaa.notora.controller;

import com.rithikaa.notora.model.Note;
import com.rithikaa.notora.model.User;
import com.rithikaa.notora.repository.NoteRepository;
import com.rithikaa.notora.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class NotesController {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public NotesController(UserRepository userRepository,
                           NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    /* ===================== UPLOAD PAGE ===================== */

    @GetMapping("/upload-note")
    public String uploadPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // ✅ One-time confetti flag
        Boolean confetti = (Boolean) session.getAttribute("CONFETTI");
        if (confetti != null && confetti) {
            model.addAttribute("success", true);
            session.removeAttribute("CONFETTI"); // VERY IMPORTANT
        }

        return "upload-notes";
    }

    /* ===================== HANDLE UPLOAD ===================== */

    @PostMapping("/upload-note")
    public String uploadNote(
            @RequestParam("file") MultipartFile file,
            @RequestParam String subject,
            @RequestParam String department,
            @RequestParam String semester,
            @RequestParam String year,
            @RequestParam String college,
            HttpSession session
    ) throws IOException {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        if (file.isEmpty()) {
            return "redirect:/upload-note";
        }

        // 📁 Upload directory
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);

        // 🔐 Unique & safe filename
        String storedFileName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadDir.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 📝 Save note
        Note note = new Note();
        note.setSubject(subject);
        note.setDepartment(department);
        note.setSemester(semester);
        note.setYear(year);
        note.setCollege(college);
        note.setFileName(file.getOriginalFilename());
        note.setFilePath(filePath.toString());
        note.setFileType(file.getContentType());
        note.setUploadedAt(LocalDateTime.now());
        note.setUser(user);

        noteRepository.save(note);

        // ⭐ Reward points
        user.setPoints(user.getPoints() + 10);
        userRepository.save(user);

        // ✅ SESSION FLAG (not flash)
        session.setAttribute("CONFETTI", true);

        // ✅ PRG pattern (NO resubmit, NO replay)
        return "redirect:/upload-note";
    }

    /* ===================== DOWNLOAD ===================== */

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {

        Note note = noteRepository.findById(id).orElseThrow();
        Path path = Paths.get(note.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + note.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE,
                        note.getFileType() != null
                                ? note.getFileType()
                                : "application/octet-stream")
                .body(resource);
    }

    /* ===================== PREVIEW (INLINE) ===================== */

    @GetMapping("/preview-file/{id}")
    public ResponseEntity<Resource> preview(@PathVariable Long id) throws IOException {

        Note note = noteRepository.findById(id).orElseThrow();
        Path path = Paths.get(note.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + note.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE,
                        note.getFileType() != null
                                ? note.getFileType()
                                : "application/octet-stream")
                .body(resource);
    }
}
