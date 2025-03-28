package com.sqlproject.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardColumn {
    private Long id;
    private String name;
    private int order;
    private String kind;
    private Board board;



}