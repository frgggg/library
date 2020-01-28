package com.cft.focusstart.library.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "books")
public class Book {

    public static final String BOOK_INFO_ID_PARAM_NAME = "id";

    public static final String BOOK_INFO_NAME_PARAM_NAME = "name";
    public static final int BOOK_INFO_NAME_LEN_MIN = 1;
    public static final int BOOK_INFO_NAME_LEN_MAX = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = BOOK_INFO_NAME_LEN_MAX, nullable = false)
    @Size(min = BOOK_INFO_NAME_LEN_MIN, max = BOOK_INFO_NAME_LEN_MAX)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "book_writers",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id")
    )
    private List<Writer> writers;

    protected Book() {}
}
