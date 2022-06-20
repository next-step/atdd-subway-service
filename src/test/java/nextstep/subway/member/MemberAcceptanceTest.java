package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.factory.AuthAcceptanceFactory.로그인_되어_있음;
import static nextstep.subway.member.factory.MemberAcceptanceFactory.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String 이메일 = "email@email.com";
    public static final String 패스워드 = "password";
    public static final String 새로운_이메일 = "newemail@email.com";
    public static final String 새로운_패스워드 = "newpassword";
    public static final int 나이 = 20;
    public static final int 새로운_나이 = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> 회원_생성_결과 = 회원_생성을_요청(이메일, 패스워드, 나이);
        // then
        회원_생성됨(회원_생성_결과);

        // when
        ExtractableResponse<Response> 회원_정보_조회_결과 = 회원_정보_조회_요청(회원_생성_결과);
        // then
        회원_정보_조회됨(회원_정보_조회_결과, 이메일, 나이);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(회원_생성_결과, 새로운_이메일, 새로운_패스워드, 새로운_나이);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(회원_생성_결과);
        // then
        회원_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("나의 정보를 관리한다.")
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> 회원_생성_결과 = 회원_생성을_요청(이메일, 패스워드, 나이);
        // then
        회원_생성됨(회원_생성_결과);

        String 로그인_토큰 = 로그인_되어_있음(이메일, 패스워드);

        // when
        ExtractableResponse<Response> 내_정보_조회_요청_결과 = 내_정보_조회_요청(로그인_토큰);
        // then
        회원_정보_조회됨(내_정보_조회_요청_결과, 이메일, 나이);

        // when
        ExtractableResponse<Response> 내_정보_수정_요청_결과 = 내_정보_수정_요청(로그인_토큰, 새로운_이메일, 새로운_패스워드, 새로운_나이);
        // then
        회원_정보_수정됨(내_정보_수정_요청_결과);

        // when
        ExtractableResponse<Response> 내_정보_삭제_요청_결과 = 내_정보_삭제_요청(로그인_토큰);
        회원_삭제됨(내_정보_삭제_요청_결과);
    }
}
