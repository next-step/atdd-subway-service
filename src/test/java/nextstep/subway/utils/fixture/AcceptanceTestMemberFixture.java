package nextstep.subway.utils.fixture;

import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;

public class AcceptanceTestMemberFixture {
    public final MemberResponse 성인;
    public final MemberResponse 청소년;
    public final MemberResponse 어린이;
    public final String 공통_비밀번호 = "password";

    public AcceptanceTestMemberFixture() {
        성인 = MemberAcceptanceTest.회원_정보_조회_요청(MemberAcceptanceTest.회원_생성을_요청("adult@adult.com", 공통_비밀번호, 30)).as(MemberResponse.class);
        청소년 = MemberAcceptanceTest.회원_정보_조회_요청(MemberAcceptanceTest.회원_생성을_요청("teenager@teenager.com", 공통_비밀번호, 15)).as(MemberResponse.class);
        어린이 = MemberAcceptanceTest.회원_정보_조회_요청(MemberAcceptanceTest.회원_생성을_요청("kid@kid.com", 공통_비밀번호, 10)).as(MemberResponse.class);
    }
}
