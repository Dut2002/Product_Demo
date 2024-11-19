package com.example.demo_oracle_db.service.mySupplier.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class SaveNoteRequest {
    @NotNull
    Long id;
    @NotBlank
    String note;
}
