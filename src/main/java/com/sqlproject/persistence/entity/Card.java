package com.sqlproject.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Card {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime createdAt;
    private Status status;
    private BoardColumn boardColumn;
}