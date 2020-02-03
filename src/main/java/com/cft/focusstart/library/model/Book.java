package com.cft.focusstart.library.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.cft.focusstart.library.model.util.ValidationMessages.STRING_FIELD_NOTNULL_MIN_MAX;

@Data
@Entity
@Table(name = "books")
public class Book {

    public static final String BOOK_NAME_FIELD_NAME = "name";
    public static final int BOOK_NAME_LEN_MIN = 1;
    public static final int BOOK_NAME_LEN_MAX = 200;
    public static final String BOOK_NAME_VALIDATION_MESSAGE =
            BOOK_NAME_FIELD_NAME + STRING_FIELD_NOTNULL_MIN_MAX + BOOK_NAME_LEN_MIN + ":" + BOOK_NAME_LEN_MAX;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = BOOK_NAME_LEN_MAX, nullable = false)
    @NotNull(message = "BOOK_NAME_VALIDATION_MESSAGE")
    @Size(min = BOOK_NAME_LEN_MIN, max = BOOK_NAME_LEN_MAX, message = BOOK_NAME_VALIDATION_MESSAGE)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_writers",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id")
    )
    private List<Writer> writers;

    protected Book() {}

    public Book(String name, List<Writer> writers) {
        this.name = name;
        this.writers = writers;
    }
}
