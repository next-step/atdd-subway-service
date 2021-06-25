package nextstep.subway.member;

import nextstep.subway.auth.dto.TokenResponse;

public class BearerAuthToken {

    private String token;

    public String getToken() {
        return token;
    }

    public void changeTo(TokenResponse tokenResponse) {
        changeTo(tokenResponse.getAccessToken());
    }

    public void changeTo(String token) {
        this.token = token;
    }
}
