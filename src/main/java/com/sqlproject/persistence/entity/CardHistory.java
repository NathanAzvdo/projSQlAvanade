package com.sqlproject.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CardHistory {
    private Long id;
    private Card card;
    private BoardColumn boardColumn;
    private OffsetDateTime enteredAt;
    private OffsetDateTime exitedAt;
}
