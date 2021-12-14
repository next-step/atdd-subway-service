package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
  private static final String EMAIL ="devsigner9920@naver.com";
  private static final String PASSWORD = "password!";
  private static final int AGE = 27;
  private TokenResponse 사용자_토큰;

  private StationResponse 교대역;
  private StationResponse 선릉역;
  private StationResponse 고터역;
  private StationResponse 선정릉역;

  private LineResponse 이호선;
  private LineResponse 삼호선;
  private LineResponse 구호선;
  private LineResponse 수인분당선;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
    선릉역 = 지하철역_생성_요청("선릉역").as(StationResponse.class);
    고터역 = 지하철역_생성_요청("고터역").as(StationResponse.class);
    선정릉역 = 지하철역_생성_요청("선정릉역").as(StationResponse.class);

    이호선 = 지하철_노선_등록되어_있음("이호선", "green", 교대역, 선릉역, 20);
    삼호선 = 지하철_노선_등록되어_있음("삼호선", "orange", 교대역, 고터역, 10);
    구호선 = 지하철_노선_등록되어_있음("구호선", "brown", 고터역, 선정릉역, 50);
    수인분당선 = 지하철_노선_등록되어_있음("수인분당선", "yellow", 선릉역, 선정릉역, 30);

    회원_생성을_요청(EMAIL, PASSWORD, AGE);
    사용자_토큰 = 로그인_됨(EMAIL, PASSWORD);
  }

  @DisplayName("즐겨찾기를 관리")
  @Test
  void manageFavorite() {
    // when
    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(교대역.getId(), 선정릉역.getId(), 사용자_토큰.getAccessToken());
    // then
    즐겨찾기_생성됨(createResponse);

    // when
    ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자_토큰.getAccessToken());
    // then
    즐겨찾기_목록_조회됨(findResponse);
    즐겨찾기_목록_데이터_확인됨(findResponse);

    // when
    String deleteLocation = createResponse.response().getHeader(HttpHeaders.LOCATION);
    ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(deleteLocation, 사용자_토큰.getAccessToken());
    // then
    즐겨찾기_삭제됨(deleteResponse);
  }

  private TokenResponse 로그인_됨(String email, String password) {
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

  private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceStationId, Long targetStationId, String token) {
    FavoriteRequest request = new FavoriteRequest(sourceStationId, targetStationId);
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .accept(MediaType.ALL_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .post("/favorites")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .when()
            .get("/favorites")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String deleteLocation, String token) {
    return RestAssured
            .given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
            .when()
            .delete(deleteLocation)
            .then().log().all()
            .extract();
  }

  private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
    assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> findResponse) {
    assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private void 즐겨찾기_목록_데이터_확인됨(ExtractableResponse<Response> findResponse) {
    List<FavoriteResponse> response = findResponse.jsonPath().getList(".", FavoriteResponse.class);
    assertThat(response.size()).isEqualTo(1);
  }

  private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
    assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}