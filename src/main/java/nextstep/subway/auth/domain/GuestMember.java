package nextstep.subway.auth.domain;

public class GuestMember {
    private static final LoginMember instance = new LoginMember();

    private GuestMember() {
    }

    public static LoginMember getInstance() {
        return instance;
    }
}
