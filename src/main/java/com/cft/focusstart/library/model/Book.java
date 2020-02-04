package com.cft.focusstart.library.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cft.focusstart.library.model.util.ValidationMessages.LIST_FIELD_NOTNULL_NOT_EMPTY;
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


    public static final String BOOK_WRITERS_FIELD_NAME = "writers";
    public static final String BOOK_WRITERS_VALIDATION_MESSAGE = BOOK_WRITERS_FIELD_NAME + LIST_FIELD_NOTNULL_NOT_EMPTY;

    public static final String BOOK_READER_FIELD_NAME = "reader";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = BOOK_NAME_LEN_MAX, nullable = false)
    @NotNull(message = BOOK_NAME_VALIDATION_MESSAGE)
    @Size(min = BOOK_NAME_LEN_MIN, max = BOOK_NAME_LEN_MAX, message = BOOK_NAME_VALIDATION_MESSAGE)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "books_writers",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "id")
    )
    @NotNull(message = BOOK_WRITERS_VALIDATION_MESSAGE)
    @NotEmpty(message = BOOK_WRITERS_VALIDATION_MESSAGE)
    private List<Writer> writers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "books_reader",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "reader_id", referencedColumnName = "id")
    )
    private Reader reader;

    @Column(name = "back_time")
    private LocalDateTime backTime;

    protected Book() {}

    public Book(String name, List<Writer> writers) {
        this.name = name;
        this.writers = writers;
    }
}
