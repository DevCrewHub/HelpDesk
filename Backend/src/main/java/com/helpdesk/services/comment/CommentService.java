package com.helpdesk.services.comment;



import com.helpdesk.dto.CommentDto;
import java.util.List;

public interface CommentService {

    CommentDto createComment(Long ticketId, String body);

    List<CommentDto> getCommentsByTicketId(Long ticketId);

}