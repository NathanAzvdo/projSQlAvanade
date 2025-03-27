package com.sqlproject.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Block {
    private Long id;
    private Card card;
    private BoardColumn boardColumn;
    private String blockCause;
    private OffsetDateTime blockIn;
    private String unblockCause;
    private OffsetDateTime unblockIn;
}
