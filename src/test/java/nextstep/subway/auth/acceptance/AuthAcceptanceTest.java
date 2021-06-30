package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthAcceptanceTest extends AcceptanceTest {

    private final String email = "joojimin@naver.com";
    private final String password = "123123";
    private final int age = 30;

    @BeforeEach
    void createMember() {
        // given
        ExtractableResponse<Response> 회원_생성_결과 = MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        MemberAcceptanceTest.회원_생성됨(회원_생성_결과);
    }


    @DisplayName("로그인 테스트")
    @Test
    void loginTest() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_성공(response);
    }

    @DisplayName("로그인 실패(등록 되지 않은 이메일 요청)")
    @Test
    void loginTestWithWrongEmail() {
        // when
        ExtractableResponse<Response> response = 로그인_요청("joojimin123@naver.com", password);

        // then
        로그인_실패(response);
    }

    @DisplayName("로그인 실패(등록 되지 않은 패스워드 요청)")
    @Test
    void loginTestWithWrongPassword() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, "1231234");

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        TokenResponse 토큰_정보 = 로그인_성공(response);

        // when
        ExtractableResponse<Response> 내정보_조회_결과 = MemberAcceptanceTest.내정보_조회_요청(토큰_정보.getAccessToken());

        // then
        MemberResponse memberResponse = MemberAcceptanceTest.내정보_조회_성공(내정보_조회_결과);
        assertAll(() -> {
            assertThat(memberResponse.getEmail().equals(email));
            assertThat(memberResponse.getAge().equals(age));
        });
    }

    @DisplayName("Bearer Auth가 없는 경우")
    @Test
    void myInfoWithBadBearerAuth() {
    	// when
	    ExtractableResponse<Response> 내정보_조회_결과 = 토큰_없는_내정보_조회_요청();

	    MemberAcceptanceTest.내정보_조회_실패(내정보_조회_결과);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
	    // given
	    ExtractableResponse<Response> response = 로그인_요청(email, password);
	    로그인_성공(response);

	    // when
	    ExtractableResponse<Response> 내정보_조회_결과 = MemberAcceptanceTest.내정보_조회_요청("test123");

	    // then
	    MemberAcceptanceTest.내정보_조회_실패(내정보_조회_결과);
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, password);

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(tokenRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all().extract();
        return response;
    }

    public static TokenResponse 로그인_성공(final ExtractableResponse<Response> response) {
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(TokenResponse.class);
    }

    public static void 로그인_실패(final ExtractableResponse<Response> response) {
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

	public static ExtractableResponse<Response> 토큰_없는_내정보_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/members/me")
			.then().log().all().extract();
	}
}
