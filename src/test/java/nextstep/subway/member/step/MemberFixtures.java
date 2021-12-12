package nextstep.subway.member.step;

import nextstep.subway.member.domain.Member;

public class MemberFixtures {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    public static Member 회원생성() {
        return new Member(EMAIL, PASSWORD, AGE);
    }
}
