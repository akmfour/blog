package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "error")
public class Error {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String status;

}
