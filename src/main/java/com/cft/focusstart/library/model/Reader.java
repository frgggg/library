package com.cft.focusstart.library.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.cft.focusstart.library.model.util.ValidationMessages.STRING_FIELD_NOTNULL_MIN_MAX;

@Data
@Entity
@Table(name = "readers")
public class Reader {

    public static final String READER_NAME_FIELD_NAME = "name";
    public static final int READER_NAME_LEN_MIN = 1;
    public static final int READER_NAME_LEN_MAX = 100;
    public static final String  READER_NAME_VALIDATION_MESSAGE = READER_NAME_FIELD_NAME + STRING_FIELD_NOTNULL_MIN_MAX + READER_NAME_LEN_MIN + ":" + READER_NAME_LEN_MAX;

    public static final String READER_IS_DEBTOR_FIELD_NAME = "isDebtor";
    public static final Boolean READER_IS_DEBTOR_STATUS = true;
    public static final Boolean READER_IS_NOT_DEBTOR_STATUS = false;

    public static final String READER_BOOKS_FIELD_NAME = "books";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = READER_NAME_LEN_MAX, nullable = false)
    @NotNull(message = READER_NAME_VALIDATION_MESSAGE)
    @Size(min = READER_NAME_LEN_MIN, max = READER_NAME_LEN_MAX, message = READER_NAME_VALIDATION_MESSAGE)
    private String name;

    @Column(name = "is_debtor", nullable = false)
    private Boolean isDebtor;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reader", cascade = CascadeType.REMOVE)
    private List<Book> books;

    protected Reader() {}

    public Reader(String name){
        this.name = name;
        isDebtor = READER_IS_NOT_DEBTOR_STATUS;
    }

    public Boolean getDebtor() {
        return isDebtor;
    }

    public void setDebtor(Boolean debtor) {
        isDebtor = debtor;
    }
}
