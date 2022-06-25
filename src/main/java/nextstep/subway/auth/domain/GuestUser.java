package nextstep.subway.auth.domain;

public class GuestUser extends LoginMember{
    private static final long GUEST_ID = -1;
    private static final String GUEST_EMAIL = "guest";
    private static final int GUEST_AGE = Integer.MAX_VALUE;

    public GuestUser() {
        super(GUEST_ID,"", GUEST_AGE);
    }
}
