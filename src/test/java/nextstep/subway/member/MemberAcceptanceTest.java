package nextstep.subway.member;

import static nextstep.subway.behaviors.MemberBehaviors.로그인_되어있음;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_삭제;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_삭제됨;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_수정;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_수정됨;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_조회;
import static nextstep.subway.behaviors.MemberBehaviors.내정보_조회됨;
import static nextstep.subway.behaviors.MemberBehaviors.회원_삭제_요청;
import static nextstep.subway.behaviors.MemberBehaviors.회원_삭제됨;
import static nextstep.subway.behaviors.MemberBehaviors.회원_생성됨;
import static nextstep.subway.behaviors.MemberBehaviors.회원_생성을_요청;
import static nextstep.subway.behaviors.MemberBehaviors.회원_정보_수정_요청;
import static nextstep.subway.behaviors.MemberBehaviors.회원_정보_수정됨;
import static nextstep.subway.behaviors.MemberBehaviors.회원_정보_조회_요청;
import static nextstep.subway.behaviors.MemberBehaviors.회원_정보_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어있음(EMAIL, PASSWORD);
        String 사용자토큰 = tokenResponse.getAccessToken();

        //when
        ExtractableResponse<Response> response = 내정보_조회(사용자토큰);
        //then
        내정보_조회됨(response);

        //when
        ExtractableResponse<Response> updateResponse = 내정보_수정(사용자토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        //then
        내정보_수정됨(updateResponse);

        //when
        ExtractableResponse<Response> deleteResponse = 내정보_삭제(사용자토큰);
        //then
        내정보_삭제됨(deleteResponse);
    }
}
