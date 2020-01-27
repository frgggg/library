package com.cft.focusstart.library.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(
        name = "writers",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name", "surname", "middle_name", "comment"})}
        )
public class Writer {
    public static final String WRITER_ID_PARAM_NAME = "id";

    public static final String WRITER_FIRST_NAME_PARAM_NAME = "firstName";
    public static final int WRITER_FIRST_NAME_LEN_MIN = 1;
    public static final int WRITER_FIRST_NAME_LEN_MAX = 100;

    public static final String WRITER_SURNAME_PARAM_NAME = "surname";
    public static final int WRITER_SURNAME_LEN_MIN = 1;
    public static final int WRITER_SURNAME_LEN_MAX = 100;

    public static final String WRITER_MIDDLE_NAME_PARAM_NAME = "middleName";
    public static final int WRITER_MIDDLE_NAME_LEN_MIN = 1;
    public static final int WRITER_MIDDLE_NAME_LEN_MAX = 100;
    public static final String WRITER_MIDDLE_NAME_NULL_VALUE = "\n\n\n~~~\n***\n@@@Null\n&&&\n^^^\n";

    public static final String WRITER_COMMENT_PARAM_NAME = "comment";
    public static final int WRITER_COMMENT_LEN_MAX = 100;
    public static final String WRITER_COMMENT_NULL_VALUE = "\n\n\n~~~\n***\n@@@Null@@@\n&&&\n^^^\n";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", length = WRITER_FIRST_NAME_LEN_MAX, nullable = false)
    @Size(min = WRITER_FIRST_NAME_LEN_MIN, max = WRITER_FIRST_NAME_LEN_MAX)
    private String firstName;

    @Column(name = "surname", length = WRITER_SURNAME_LEN_MAX, nullable = false)
    @Size(min = WRITER_SURNAME_LEN_MIN, max = WRITER_SURNAME_LEN_MAX)
    private String surname;

    @Column(name = "middle_name", length = WRITER_MIDDLE_NAME_LEN_MAX)
    @Size(min  = WRITER_MIDDLE_NAME_LEN_MIN, max = WRITER_MIDDLE_NAME_LEN_MAX)
    private String middleName;

    @Column(name = "comment", length = WRITER_COMMENT_LEN_MAX)
    @Size(max = WRITER_COMMENT_LEN_MAX)
    private String comment;

    protected Writer() {}

    public Writer(String firstName, String surname, String middleName, String comment) {
        this.firstName = firstName;
        this.surname = surname;
        this.middleName = middleName;
        this.comment = comment;
    }
}
