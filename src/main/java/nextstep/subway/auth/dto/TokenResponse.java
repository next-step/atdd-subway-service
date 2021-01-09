package nextstep.subway.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
