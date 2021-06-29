package nextstep.subway.auth.acceptance;

public class TestToken {
    private String accessToken;

    protected TestToken() {
    }

    public TestToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
