package nextstep.subway.member;

import nextstep.subway.member.dto.MemberResponse;

public class MemberTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 21;
    public static final int TEENAGER = 18;
    public static final int CHILD = 12;

    public static final MemberResponse 일반_사용자 = new MemberResponse(1L, EMAIL, AGE);
    public static final MemberResponse 청소년_사용자 = new MemberResponse(2L, EMAIL, TEENAGER);
    public static final MemberResponse 어린이_사용자 = new MemberResponse(3L, EMAIL, CHILD);
}
