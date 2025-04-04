package com.sqlproject.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardHistory {
    private Long id;
    private Card card;
    private BoardColumn boardColumn;
    private OffsetDateTime enteredAt;
    private OffsetDateTime exitedAt;
}
