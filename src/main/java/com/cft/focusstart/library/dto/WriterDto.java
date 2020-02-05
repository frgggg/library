package com.cft.focusstart.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.cft.focusstart.library.model.Writer.*;

@Data
@Builder
@AllArgsConstructor
public class WriterDto {

    private Long id;

    @NotNull(message = WRITER_FIRST_NAME_VALIDATION_MESSAGE)
    @Size(min = WRITER_FIRST_NAME_LEN_MIN, max = WRITER_FIRST_NAME_LEN_MAX, message = WRITER_FIRST_NAME_VALIDATION_MESSAGE)
    private String firstName;

    @NotNull(message = WRITER_SURNAME_VALIDATION_MESSAGE)
    @Size(min = WRITER_SURNAME_LEN_MIN, max = WRITER_SURNAME_LEN_MAX, message = WRITER_SURNAME_VALIDATION_MESSAGE)
    private String surname;

    @Size(max = WRITER_MIDDLE_NAME_LEN_MAX, message = WRITER_MIDDLE_NAME_VALIDATION_MESSAGE)
    private String middleName;

    @Size(max = WRITER_COMMENT_LEN_MAX, message = WRITER_COMMENT_VALIDATION_MESSAGE)
    private String comment;

    public WriterDto() {}
}
