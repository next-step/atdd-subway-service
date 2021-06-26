package nextstep.subway.auth.domain;

public class Guest extends LoginMember {

    private static final Long GUEST_ID = -1L;
    private static final String GUEST_EMAIL = "guest";
    private static final int GUEST_AGE = 30;


    public Guest() {
        super(GUEST_ID, GUEST_EMAIL, GUEST_AGE);
    }
}
