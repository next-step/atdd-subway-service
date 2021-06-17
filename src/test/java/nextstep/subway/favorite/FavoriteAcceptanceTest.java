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

  private LineResponse 신분당선;
  private LineResponse 이호선;
  private LineResponse 삼호선;
  private StationResponse 강남역;
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
    강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
    양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
    교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
    남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

    //and 지하철 노선 등록되어 있음
    신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
    이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
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
    ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(교대역.getId(), 양재역.getId());
    //then 즐겨찾기 생성됨
    즐겨찾기_생성됨(즐겨찾기_생성_응답);

    //when 즐겨찾기 목록 조회 요청
    ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청();
    //then 즐겨찾기 목록 조회됨
    즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답, 즐겨찾기_생성_응답);

  }

  private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
    return RestAssured.given().log().all()
            .auth().oauth2(loginToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new FavoriteRequest(source, target))
            .when().post("/favorites")
            .then().log().all()
            .extract();
  }

  private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }
  private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
    return RestAssured.given().log().all()
        .auth().oauth2(loginToken)
        .when().get("/favorites")
        .then().log().all()
        .extract();
  }

  private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    List<FavoriteResponse> results = response.jsonPath().getList(".", FavoriteResponse.class);
    assertThat(results).containsExactly(createdResponse.as(FavoriteResponse.class));
  }
}
