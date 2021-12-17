package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.fixture.TestLoginFactory;
import nextstep.subway.fixture.TestMemberFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     */
    @DisplayName("로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        // given
        TestMemberFactory.회원_등록_요청("nextstep@gmail.com", "1234", 10);

        // when
        ExtractableResponse<Response> response = TestLoginFactory.로그인_요청("nextstep@gmail.com", "1234");

        // then
        TestLoginFactory.로그인_응답_성공됨(response);
    }

    @DisplayName("등록되지 않은 회원으로 로그인 시도 시 예외처리한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = TestLoginFactory.로그인_요청("nextstep@gmail.com", "1234");

        // then
        TestLoginFactory.로그인_응답_실패됨(response);
    }

    @DisplayName("등록된 정보가 다르게 로그인 시도 시 예외처리한다.")
    @Test
    void myInfoWithBadBearerAuth2() {
        // given
        TestMemberFactory.회원_등록_요청("nextstep@gmail.com", "1234", 10);
        // when
        ExtractableResponse<Response> response = TestLoginFactory.로그인_요청("nextstep@gmail.com", "099877655");

        // then
        TestLoginFactory.로그인_응답_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 유효하지않은토큰 = "TOKEN";

        // when
        ExtractableResponse<Response> response = TestMemberFactory.본인_정보_조회_요청(유효하지않은토큰);

        // then
        TestMemberFactory.본인_정보_조회_실패됨(response);
    }

}
