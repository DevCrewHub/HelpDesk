package com.helpdesk.controller.comment;

import com.helpdesk.dto.CommentDto;
import com.helpdesk.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments") // A general endpoint for comments
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        try {
            CommentDto createdComment = commentService.createComment(commentDto.getTicketId(), commentDto.getBody());
            if (createdComment == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create comment.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getCommentsByTicket(@PathVariable Long ticketId) {
        try {
            List<CommentDto> comments = commentService.getCommentsByTicketId(ticketId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}