package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    Integer id;
    String name;
    String bookNumber;
    Integer price;
    String publisher;
    String description;
    String category;
    String imageUrl;
    Date createTime;
}
