package com.sem.ecommerce.domain.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Receiver(
        @NotBlank
        @Size(max = 200)
        String address,

        @NotBlank
        @Size(max = 32)
        String name,

        @NotBlank
        @Pattern(regexp = "^01[016789]([0-9]){3,4}([0-9]){3,4}")
        String phoneNumber
) {
}
