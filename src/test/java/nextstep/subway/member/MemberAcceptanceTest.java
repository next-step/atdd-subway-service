package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.*;
import static nextstep.subway.member.MemberAcceptanceStep.*;

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
        // Given 회원 등록됨
        회원_등록됨(EMAIL, PASSWORD, AGE);
        // When 로그인 요청
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(EMAIL, PASSWORD);
        // Then 로그인 됨
        로그인_응답됨(로그인_요청_결과);
        로그인_됨(로그인_요청_결과);

        // When 나의 정보 요청
        ExtractableResponse<Response> 나의_정보_조회_요청_결과 = 나의_정보_조회_요청(로그인_요청_결과.as(TokenResponse.class).getAccessToken());
        // Then 나의 정보 확인
        나의_정보_확인(나의_정보_조회_요청_결과, EMAIL, AGE);

        // When 나의 정보 수정
        ExtractableResponse<Response> 나의_정보_수정_요청_결과 = 나의_정보_수정_요청(로그인_요청_결과.as(TokenResponse.class).getAccessToken(), NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // Then 나의 정보 수정 응답됨
        나의_정보_수정_응답됨(나의_정보_수정_요청_결과);

        // When 나의 정보 재요청
        ExtractableResponse<Response> 나의_정보_조회_재요청_결과 = 나의_정보_조회_요청(로그인_요청_결과.as(TokenResponse.class).getAccessToken());
        // Then 나의 정보 확인
        나의_정보_확인(나의_정보_조회_재요청_결과, NEW_EMAIL, NEW_AGE);

        // When 수정된 정보로 로그인 요청
        ExtractableResponse<Response> 로그인_재요청_결과 = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        // Then 로그인 됨
        로그인_응답됨(로그인_재요청_결과);
        로그인_됨(로그인_재요청_결과);

        // When 나의 정보 삭제
        ExtractableResponse<Response> 나의_정보_삭제_요청_결과 = 나의_정보_삭제_요청(로그인_요청_결과.as(TokenResponse.class).getAccessToken());
        // Then 나의 정보 삭제 응답됨
        나의_정보_삭제_응답됨(나의_정보_삭제_요청_결과);

        // When 삭제된 정보로 로그인 요청
        ExtractableResponse<Response> 삭제된_로그인_재요청_결과 = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        // Then 나의 정보 확인
        로그인_실패됨(삭제된_로그인_재요청_결과);
    }
}
