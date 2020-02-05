package com.cft.focusstart.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.cft.focusstart.library.model.Reader.*;

@Data
@Builder
@AllArgsConstructor
public class ReaderDto {

    private Long id;

    @NotNull(message = READER_NAME_VALIDATION_MESSAGE)
    @Length(
            min = READER_NAME_LEN_MIN,
            max = READER_NAME_LEN_MAX,
            message = READER_NAME_VALIDATION_MESSAGE
    )
    private String name;

    private Boolean isDebtor;

    private List<Long> books;

    public ReaderDto() {}

    public Boolean getDebtor() {
        return isDebtor;
    }

    public void setDebtor(Boolean debtor) {
        isDebtor = debtor;
    }
}
