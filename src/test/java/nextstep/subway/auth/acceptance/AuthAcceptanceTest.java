package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 시도")
public class AuthAcceptanceTest extends AcceptanceTest {
  private Member 로그인_시도_성공_회원;
  private Member 로그인_시도_실패_회원;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    로그인_시도_성공_회원 = new Member("devsigner9920@gmail.com", "password", 27);
    로그인_시도_실패_회원 = new Member("devsigner9920@gmail.com", "invalidPassword", 27);
  }

  @DisplayName("Bearer Auth")
  @Test
  void myInfoWithBearerAuth() {
    // given
    회원_등록되어_있음(로그인_시도_성공_회원);
    TokenRequest request = new TokenRequest(로그인_시도_성공_회원.getEmail(), 로그인_시도_성공_회원.getPassword());

    // when
    ExtractableResponse<Response> response = 로그인_요청(request);

    // then
    로그인_됨(response);
  }

  @DisplayName("Bearer Auth 로그인 실패")
  @Test
  void myInfoWithBadBearerAuth() {
    // given
    회원_등록되어_있음(로그인_시도_성공_회원);
    TokenRequest request = new TokenRequest(로그인_시도_실패_회원.getEmail(), 로그인_시도_실패_회원.getPassword());

    // when
    ExtractableResponse<Response> response = 로그인_요청(request);

    // then
    로그인_실패됨(response);
  }

  @DisplayName("Bearer Auth 유효하지 않은 토큰")
  @Test
  void myInfoWithWrongBearerAuth() {
    // given
    회원_등록되어_있음(로그인_시도_성공_회원);
    로그인되어_있음(로그인_시도_성공_회원);
    String 유효하지_않은_토큰 = "invalidToken";

    // when
    ExtractableResponse<Response> response = 로그인된_회원_정보_요청(유효하지_않은_토큰);

    토큰_유효하지_않음(response);
  }

  private void 토큰_유효하지_않음(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.body().jsonPath().get("message").toString()).isEqualTo("로그인 유저 정보가 올바르지 않습니다.");
  }

  private ExtractableResponse<Response> 로그인된_회원_정보_요청(String token) {
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .when()
            .get("/members/me")
            .then().log().all()
            .extract();
  }

  private void 로그인되어_있음(Member member) {
    로그인_요청(new TokenRequest(member.getEmail(), member.getPassword()));
  }

  private ExtractableResponse<Response> 로그인_요청(TokenRequest request) {
    return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .post("/login/token")
            .then().log().all()
            .extract();
  }

  private void 로그인_됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private void 로그인_실패됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

}
