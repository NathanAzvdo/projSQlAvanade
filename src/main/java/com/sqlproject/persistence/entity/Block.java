package com.sqlproject.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private Long id;
    private Card card;
    private BoardColumn boardColumn;
    private String blockCause;
    private OffsetDateTime blockIn;
    private String unblockCause;
    private OffsetDateTime unblockIn;
}
