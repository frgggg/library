package com.cft.focusstart.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.cft.focusstart.library.model.Book.*;

@Data
@Builder
@AllArgsConstructor
public class BookDto {

    private Long id;

    @NotNull(message = BOOK_NAME_VALIDATION_MESSAGE)
    @Length(
            min = BOOK_NAME_LEN_MIN,
            max = BOOK_NAME_LEN_MAX,
            message = BOOK_NAME_VALIDATION_MESSAGE
    )
    private String name;

    private Long reader;
    private LocalDateTime backTime;
    private List<Long> writers;

    public BookDto() {}
}
