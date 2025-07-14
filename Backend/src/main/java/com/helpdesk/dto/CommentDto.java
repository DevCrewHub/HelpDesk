package com.helpdesk.dto;


import lombok.Data;
import java.util.Date;

@Data
public class CommentDto {

    private Long id;
    private String body;
    private Date createdAt;
    private Long userId;
    private String postedBy;
    private Long ticketId;
}