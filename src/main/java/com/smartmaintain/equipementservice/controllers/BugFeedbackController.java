package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.equipementservice.entities.BugFeedback;
import com.smartmaintain.equipementservice.repositories.BugFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bug-feedbacks")
public class BugFeedbackController {

    @Autowired
    private BugFeedbackRepository repository;

    @PostMapping
    public ResponseEntity<BugFeedback> create(@Valid @RequestBody BugFeedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(feedback));
    }

    @GetMapping
    public ResponseEntity<List<BugFeedback>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
