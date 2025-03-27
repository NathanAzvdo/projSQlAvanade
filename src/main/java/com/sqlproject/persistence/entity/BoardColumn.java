package com.sqlproject.persistence.entity;

import lombok.Data;

@Data
public class BoardColumn {
    private Long id;
    private String name;
    private int order;
    private String kind;
    private Board board;
}