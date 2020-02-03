package com.cft.focusstart.library.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.cft.focusstart.library.model.util.ValidationMessages.*;

@Data
@Entity
@Table(
        name = "writers",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name", "surname", "middle_name", "comment"})}
        )
public class Writer {

    public static final String WRITER_FIRST_NAME_FIELD_NAME = "firstName";
    public static final int WRITER_FIRST_NAME_LEN_MIN = 2;
    public static final int WRITER_FIRST_NAME_LEN_MAX = 100;
    public static final String WRITER_FIRST_NAME_VALIDATION_MESSAGE =
            WRITER_FIRST_NAME_FIELD_NAME + STRING_FIELD_NOTNULL_MIN_MAX + WRITER_FIRST_NAME_LEN_MIN + ":" + WRITER_FIRST_NAME_LEN_MAX;

    public static final String WRITER_SURNAME_FIELD_NAME = "surname";
    public static final int WRITER_SURNAME_LEN_MIN = 2;
    public static final int WRITER_SURNAME_LEN_MAX = 100;
    public static final String WRITER_SURNAME_VALIDATION_MESSAGE =
            WRITER_SURNAME_FIELD_NAME + STRING_FIELD_NOTNULL_MIN_MAX + WRITER_SURNAME_LEN_MIN + ":" + WRITER_SURNAME_LEN_MAX;

    public static final String WRITER_MIDDLE_NAME_FIELD_NAME = "middleName";
    public static final int WRITER_MIDDLE_NAME_LEN_MAX = 100;
    public static final String WRITER_MIDDLE_NAME_VALIDATION_MESSAGE =
            WRITER_MIDDLE_NAME_FIELD_NAME + STRING_FIELD_MAX + WRITER_MIDDLE_NAME_LEN_MAX;
    public static final String WRITER_MIDDLE_NAME_NULL_VALUE = "\n";

    public static final String WRITER_COMMENT_FIELD_NAME = "comment";
    public static final int WRITER_COMMENT_LEN_MAX = 100;
    public static final String WRITER_COMMENT_VALIDATION_MESSAGE =
            WRITER_COMMENT_FIELD_NAME + STRING_FIELD_MAX + WRITER_COMMENT_LEN_MAX;
    public static final String WRITER_COMMENT_NULL_VALUE = "\n";

    public static final String WRITER_BOOKS_FIELD_NAME = "books";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", length = WRITER_FIRST_NAME_LEN_MAX, nullable = false)
    @NotNull(message = WRITER_FIRST_NAME_VALIDATION_MESSAGE)
    @Size(min = WRITER_FIRST_NAME_LEN_MIN, max = WRITER_FIRST_NAME_LEN_MAX, message = WRITER_FIRST_NAME_VALIDATION_MESSAGE)
    private String firstName;

    @Column(name = "surname", length = WRITER_SURNAME_LEN_MAX, nullable = false)
    @NotNull(message = WRITER_SURNAME_VALIDATION_MESSAGE)
    @Size(min = WRITER_SURNAME_LEN_MIN, max = WRITER_SURNAME_LEN_MAX, message = WRITER_SURNAME_VALIDATION_MESSAGE)
    private String surname;

    @Column(name = "middle_name", length = WRITER_MIDDLE_NAME_LEN_MAX)
    @Size(max = WRITER_MIDDLE_NAME_LEN_MAX, message = WRITER_MIDDLE_NAME_VALIDATION_MESSAGE)
    private String middleName;

    @Column(name = "comment", length = WRITER_COMMENT_LEN_MAX)
    @Size(max = WRITER_COMMENT_LEN_MAX, message = WRITER_COMMENT_VALIDATION_MESSAGE)
    private String comment;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "writers", cascade = CascadeType.ALL)
    private List<Book> books;

    protected Writer() {}

    public Writer(String firstName, String surname, String middleName, String comment) {
        this.firstName = firstName;
        this.surname = surname;
        this.middleName = ((middleName == null)? WRITER_MIDDLE_NAME_NULL_VALUE: middleName);
        this.comment = ((comment == null)? WRITER_COMMENT_NULL_VALUE: comment);
    }

    public String getMiddleName() {
        return (middleName.equals(WRITER_MIDDLE_NAME_NULL_VALUE)? null: middleName);
    }

    public void setMiddleName(String middleName) {
        this.middleName = ((middleName == null)? WRITER_MIDDLE_NAME_NULL_VALUE: middleName);
    }

    public String getComment() {
        return (comment.equals(WRITER_COMMENT_NULL_VALUE)? null: comment);
    }

    public void setComment(String comment) {
        this.comment = ((comment == null)? WRITER_COMMENT_NULL_VALUE: comment);
    }
}
