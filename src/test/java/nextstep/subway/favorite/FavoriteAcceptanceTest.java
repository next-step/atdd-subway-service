package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  private LineResponse 삼호선;
  private StationResponse 양재역;
  private StationResponse 교대역;
  private StationResponse 남부터미널역;
  private String loginToken;

  @BeforeEach
  @Override
  //Background
  public void setUp() {
    super.setUp();
    //given 지하철역 등록되어 있음
    양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
    교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
    남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

    //and 지하철 노선 등록되어 있음
    삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

    //and 지하철 노선에 지하철역 등록되어 있음
    지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

    //and 회원 등록되어 있음
    ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
    loginToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class)
                    .getAccessToken();
  }

  @DisplayName("즐겨찾기를 관리")
  @Test
  void favoriteManagementTest() {
    //when 즐겨찾기 생성을 요청
    ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(loginToken, 교대역.getId(), 양재역.getId());
    //then 즐겨찾기 생성됨
    즐겨찾기_생성됨(즐겨찾기_생성_응답);

    //when 잘못된 토큰으로 즐겨찾기 생성을 요청
    ExtractableResponse<Response> 잘못된_토큰_즐겨찾기_생성_응답 = 즐겨찾기_생성_요청("invalidToken", 교대역.getId(), 양재역.getId());
    //then 즐겨찾기 생성 실패함
    잘못된_토큰_때문에_즐겨찾기_등록_실패(잘못된_토큰_즐겨찾기_생성_응답);

    //when 존재하지 않는 역의 id로 생성을 요청
    Long notExistSourceId = 999L;
    Long notExistTargetId = 9999L;
    ExtractableResponse<Response> 존재하지않는_역_즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(loginToken, notExistSourceId, notExistTargetId);
    //then 즐겨찾기 생성 실패함
    존재하지않는_역은_즐겨찾기_등록_실패(존재하지않는_역_즐겨찾기_생성_응답);

    //when 즐겨찾기 목록 조회 요청
    ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(loginToken);
    //then 즐겨찾기 목록 조회됨
    즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답, 즐겨찾기_생성_응답);

    //when 즐겨찾기 삭제 요청
    ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(loginToken, 즐겨찾기_생성_응답);
    //then 즐겨찾기 삭제됨
    즐겨찾기_삭제됨(즐겨찾기_삭제_응답);

  }

  private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
    return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new FavoriteRequest(source, target))
            .when().post("/favorites")
            .then().log().all()
            .extract();
  }

  private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }
  private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
    return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when().get("/favorites")
            .then().log().all()
            .extract();
  }

  private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    List<FavoriteResponse> results = response.jsonPath().getList(".", FavoriteResponse.class);
    assertThat(results).containsExactly(createdResponse.as(FavoriteResponse.class));
  }

  private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> createdResponse) {
    FavoriteResponse favoriteResponse = createdResponse.as(FavoriteResponse.class);
    return RestAssured.given().log().all()
        .auth().oauth2(token)
        .pathParam("id", favoriteResponse.getId())
        .when().delete("/favorites/{id}")
        .then().log().all()
        .extract();
  }

  private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  private void 잘못된_토큰_때문에_즐겨찾기_등록_실패(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  private void 존재하지않는_역은_즐겨찾기_등록_실패(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}
