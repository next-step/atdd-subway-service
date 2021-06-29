package nextstep.subway.member;

import nextstep.subway.member.domain.Member;

public class MemberTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 21;
    public static final int TEENAGER = 18;
    public static final int CHILD = 12;

    public static final Member 일반_멤버 = new Member(1L, EMAIL, PASSWORD, AGE);
    public static final Member 청소년_멤버 = new Member(2L, EMAIL, PASSWORD, TEENAGER);
    public static final Member 어린이_멤버 = new Member(3L, EMAIL, PASSWORD, CHILD);
}
