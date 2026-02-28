package com.Bitespeed.enity;

import com.Bitespeed.enity.type.LinkPrecedence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Contact{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String phoneNumber;
    private Long linkedId;
    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}