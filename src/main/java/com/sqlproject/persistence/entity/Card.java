package com.sqlproject.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime createdAt;
    private Status status;
    private BoardColumn boardColumn;
}