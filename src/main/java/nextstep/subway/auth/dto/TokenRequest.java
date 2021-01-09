package nextstep.subway.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
