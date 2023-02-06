package com.jesusmurguia.chat_app.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
public class LoginForm {
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    @NotBlank
    @Size(min = 4, max = 45)
    private String password;

    private String room;
}