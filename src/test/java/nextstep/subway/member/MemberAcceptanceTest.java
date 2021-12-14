package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberAcceptanceTest extends AcceptanceTest {
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
    // given
    회원_생성을_요청(EMAIL, PASSWORD, AGE);
    String 토큰 = 로그인됨(EMAIL, PASSWORD).getAccessToken();
    System.out.println("토큰 = " + 토큰);

    // when
    ExtractableResponse<Response> findResponse = 내_정보_조회_요청(토큰);
    // then
    내_정보_조회됨(findResponse);
    내_정보_데이터_확인됨(findResponse);

    // when
    ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(토큰);
    // then
    내_정보_수정됨(updateResponse);

    // when
    ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(토큰);
    // then
    내_정보_삭제됨(deleteResponse);
  }

  private ExtractableResponse<Response> 내_정보_조회_요청(String token) {
    return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .when()
            .get("/members/me")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 내_정보_수정_요청(String token) {
    MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when()
            .put("members/me")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 내_정보_삭제_요청(String token) {
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .when()
            .delete("/members/me")
            .then().log().all()
            .extract();
  }

  private void 내_정보_조회됨(ExtractableResponse<Response> findResponse) {
    assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private void 내_정보_데이터_확인됨(ExtractableResponse<Response> findResponse) {
    Member me = findResponse.as(Member.class);
    assertAll(
            () -> assertThat(me.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(me.getAge()).isEqualTo(AGE)
    );
  }

  private void 내_정보_수정됨(ExtractableResponse<Response> updateResponse) {
    assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private void 내_정보_삭제됨(ExtractableResponse<Response> deleteResponse) {
    assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }


  private TokenResponse 로그인됨(String email, String password) {
    TokenRequest tokenRequest = new TokenRequest(email, password);

    return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when()
            .post("login/token")
            .then().log().all()
            .extract()
            .as(TokenResponse.class);
  }

  public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
    MemberRequest memberRequest = new MemberRequest(email, password, age);

    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(memberRequest)
        .when().post("/members")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
    String uri = response.header("Location");

    return RestAssured
        .given().log().all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .when().get(uri)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
    String uri = response.header("Location");
    MemberRequest memberRequest = new MemberRequest(email, password, age);

    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(memberRequest)
        .when().put(uri)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
    String uri = response.header("Location");
    return RestAssured
        .given().log().all()
        .when().delete(uri)
        .then().log().all()
        .extract();
  }

  public static void 회원_등록되어_있음(Member member) {
    회원_생성을_요청(member.getEmail(), member.getPassword(), member.getAge());
  }

  public static void 회원_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
    MemberResponse memberResponse = response.as(MemberResponse.class);
    assertThat(memberResponse.getId()).isNotNull();
    assertThat(memberResponse.getEmail()).isEqualTo(email);
    assertThat(memberResponse.getAge()).isEqualTo(age);
  }

  public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  public static void 회원_삭제됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
