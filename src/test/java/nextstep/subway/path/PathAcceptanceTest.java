package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
  private LineResponse 신분당선;
  private LineResponse 이호선;
  private LineResponse 삼호선;
  private StationResponse 강남역;
  private StationResponse 양재역;
  private StationResponse 교대역;
  private StationResponse 남부터미널역;

  /**
   * 교대역    --- *2호선* ---   강남역
   * |                        |
   * *3호선*                   *신분당선*
   * |                        |
   * 남부터미널역  --- *3호선* ---   양재
   */
  @BeforeEach
  public void setUp() {
    super.setUp();

    강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
    양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
    교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
    남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

    신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
    이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
    삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

    지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
  }

  @DisplayName("지하철 최단거리를 조회")
  @Test
  void findPathScenarioTest() {
    //when 교대역 - 양재역 최단거리 조회
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(교대역, 양재역);

    //then 최단거리가 반환됨
    최단거리_조회됨(경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 1_250);

    //when 새로운 최단거리를 가지는 경로가 추가됨
    지하철_노선_등록되어_있음(new LineRequest("최단거리선", "bg-grey-600", 교대역.getId(), 양재역.getId(), 3)).as(LineResponse.class);

    //when 교대역 - 양재역 최단거리 조회
    ExtractableResponse<Response> 새로운_최단거리_노선_추가_후_경로_탐색_결과 = 최단거리_경로_탐색(교대역, 양재역);

    //then 새로운 최단거리가 반환됨
    최단거리_조회됨(새로운_최단거리_노선_추가_후_경로_탐색_결과, Arrays.asList(교대역, 양재역), 3D, 1_250);

    //when 출발역과 도착역이 동일한 최단거리 조회
    ExtractableResponse<Response> 출발_도착이_동일한_경로_탐색_결과 = 최단거리_경로_탐색(교대역, 교대역);

    //then 거리가 0이고 역이 하나 뿐인 최단거리 반환됨
    최단거리_조회됨(출발_도착이_동일한_경로_탐색_결과, Arrays.asList(교대역), 0D, 0);
    최단거리_기대한값(출발_도착이_동일한_경로_탐색_결과, 0);

    //when 기등록된 노선과 연결되지 않은 경로가 추가됨
    StationResponse 용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
    StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
    지하철_노선_등록되어_있음(new LineRequest("1호선", "bg-navy-600", 용산역.getId(), 서울역.getId(), 5)).as(LineResponse.class);

    //when 연결되지 않은 역 끼리 최단거리 조회
    ExtractableResponse<Response> 연결되지_않은_역끼리_경로_탐색_결과 = 최단거리_경로_탐색(교대역, 서울역);

    //then 경로탐색에 실패함
    경로탐색_실패_연결되지_않은_역(연결되지_않은_역끼리_경로_탐색_결과);

    //when 존재하지 않는 역과 최단거리 조회
    StationResponse 존재하지_않는_역 = new StationResponse(999L, "존재하지 않는 역", LocalDateTime.now(), LocalDateTime.now());
    ExtractableResponse<Response> 존재하지_않는_역과_경로_탐색_결과 = 최단거리_경로_탐색(교대역, 존재하지_않는_역);

    //then 경로탐색에 실패함
    경로탐색_실패_존재하지_않는_역(존재하지_않는_역과_경로_탐색_결과);
  }

  @DisplayName("추가요금이 있는 노선을 지나가는 경로를 조회")
  @Test
  void findPathWithAdditionalLineFeeTest() {
    //given
    StationResponse 시작역 = 지하철역_등록되어_있음("시작역").as(StationResponse.class);
    StationResponse 중간역 = 지하철역_등록되어_있음("중간역").as(StationResponse.class);
    StationResponse 끝역 = 지하철역_등록되어_있음("끝역").as(StationResponse.class);
    LineResponse 추가요금_있는_노선 = 지하철_노선_등록되어_있음(new LineRequest("추가요금노선", "bg-black-600", 시작역.getId(), 중간역.getId(), 5, 1_000)).as(LineResponse.class);
    LineResponse 추가요금_없는_노선 = 지하철_노선_등록되어_있음(new LineRequest("노선", "bg-white-600", 중간역.getId(), 끝역.getId(), 5)).as(LineResponse.class);

    //when 추가요금이 있는 노선과 없는 노선을 경유하는 경로를 검색
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(시작역, 끝역);

    //then 이용요금 (1,250 원)에 노선 추가요금 (1,000원)이 더해진 요금이 조회됨
    최단거리_조회됨(경로_탐색_결과, Arrays.asList(시작역, 중간역, 끝역), 10D, 2_250);

    //given
    StationResponse 새끝역 = 지하철역_등록되어_있음("새끝역").as(StationResponse.class);
    LineResponse 저렴한_추가요금_노선 = 지하철_노선_등록되어_있음(new LineRequest("저렴한추가요금노선", "bg-grey-600", 중간역.getId(), 새끝역.getId(), 5, 500)).as(LineResponse.class);

    //when 추가요금이 모두 있는 노선을 경유하는 경로를 검색
    ExtractableResponse<Response> 모든_노선_추가요금_경로_탐색_결과 = 최단거리_경로_탐색(시작역, 새끝역);

    //then 이용요금 (1,250 원)에 추가요금이 더 비싼 노선의 추가요금 (1,000원)이 더해진 요금이 조회됨
    최단거리_조회됨(모든_노선_추가요금_경로_탐색_결과, Arrays.asList(시작역, 중간역, 새끝역), 10D, 2_250);
  }

  @DisplayName("연령별 할인 정책 경로를 조회")
  @Test
  void findPathWithAgeDiscountTest() {
    //given 성인회원 가입
    ExtractableResponse<Response> 성인_회원 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

    //when 성인회원이 로그인 후 경로 탐색
    TokenResponse 성인_회원_로그인 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    ExtractableResponse<Response> 성인_회원_경로_탐색_결과 = 로그인_유저의_최단거리_경로_탐색(성인_회원_로그인.getAccessToken(), 교대역, 양재역);

    //then 기본적인 요금(1,250 원)이 조회된다.
    최단거리_조회됨(성인_회원_경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 1_250);

    //given 청소년 회원 가입
    String teenagerEmail = "teen@teen.com";
    String teenagerPassword = "teen";
    ExtractableResponse<Response> 청소년_회원 = 회원_생성을_요청(teenagerEmail, teenagerPassword, 15);

    //when 청소년 회원이 로그인 후 경로 탐색
    TokenResponse 청소년_회원_로그인 = 로그인_요청(teenagerEmail, teenagerPassword).as(TokenResponse.class);
    ExtractableResponse<Response> 청소년_회원_경로_탐색_결과 = 로그인_유저의_최단거리_경로_탐색(청소년_회원_로그인.getAccessToken(), 교대역, 양재역);

    //then 기본 요금에서 350원을 공제한 금액에 20%를 할인한 금액이 조회된다.
    최단거리_조회됨(청소년_회원_경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 720);

    //given 어린이 회원 가입
    String childrenEmail = "children@children.com";
    String childrenPassword = "children";
    ExtractableResponse<Response> 어린이_회원 = 회원_생성을_요청(childrenEmail, childrenPassword, 9);

    //when 어린이 회원이 로그인 후 경로 탐색
    TokenResponse 어린이_회원_로그인 = 로그인_요청(childrenEmail, childrenPassword).as(TokenResponse.class);
    ExtractableResponse<Response> 어린이_회원_경로_탐색_결과 = 로그인_유저의_최단거리_경로_탐색(어린이_회원_로그인.getAccessToken(), 교대역, 양재역);

    //then 기본 요금에서 350원을 공제한 금액에 50%를 할인한 금액이 조회된다.
    최단거리_조회됨(어린이_회원_경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 450);

    //given 영유아 회원 가입
    String toddlerEmail = "toddler@toddler.com";
    String toddlerPassword = "toddler";
    ExtractableResponse<Response> 영유아_회원 = 회원_생성을_요청(toddlerEmail, toddlerPassword, 4);

    //when 영유아 회원이 로그인 후 경로 탐색
    TokenResponse 영유아_회원_로그인 = 로그인_요청(toddlerEmail, toddlerPassword).as(TokenResponse.class);
    ExtractableResponse<Response> 영유아_회원_경로_탐색_결과 = 로그인_유저의_최단거리_경로_탐색(영유아_회원_로그인.getAccessToken(), 교대역, 양재역);

    //then 0원 조회 (무료)
    최단거리_조회됨(영유아_회원_경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 0);
  }

  @DisplayName("경로 탐색 시 최단거리로 갈 수있는 경로를 출발역부터 도착역까지 순서로 반환한다.")
  @Test
  void findPathTest() {
    //when
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(교대역, 양재역);

    //then
    최단거리_조회됨(경로_탐색_결과, Arrays.asList(교대역, 남부터미널역, 양재역), 5D, 1_250);
  }

  @DisplayName("출발역과 도착역이 같다.")
  @Test
  void sameSourceAndTargetTest() {
    //given
    지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

    //when
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(교대역, 교대역);

    //then
    최단거리_조회됨(경로_탐색_결과, Arrays.asList(교대역), 0D, 0);
    최단거리_기대한값(경로_탐색_결과, 0);
  }

  @DisplayName("출발역과 도착역이 연결되어있지 않다.")
  @Test
  void noneConnectedStationTest() {
    //given
    StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
    StationResponse 용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
    LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-navy-600", 서울역.getId(), 용산역.getId(), 5)).as(LineResponse.class);

    //when
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(교대역, 서울역);

    //then
    경로탐색_실패_연결되지_않은_역(경로_탐색_결과);
  }

  @DisplayName("조회하는 역이 존재하지 않는다.")
  @Test
  void noneExistingStationTest() {
    //given
    StationResponse 존재하지_않는_역 = new StationResponse(999L, "존재하지 않는 역", LocalDateTime.now(), LocalDateTime.now());

    //when
    ExtractableResponse<Response> 경로_탐색_결과 = 최단거리_경로_탐색(교대역, 존재하지_않는_역);

    //then
    경로탐색_실패_존재하지_않는_역(경로_탐색_결과);
  }

  private void 경로탐색_실패_연결되지_않은_역(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  private void 경로탐색_실패_존재하지_않는_역(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  private void 최단거리_기대한값(ExtractableResponse<Response> response, int expectDistance) {
    PathResponse actual = response.as(PathResponse.class);
    assertThat(actual.getDistance()).isEqualTo(expectDistance);
  }

  private void 최단거리_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations, double expectDistance, int expectFee) {
    PathResponse actual = response.as(PathResponse.class);
    List<Long> actualStationIds = actual.getStations()
                                    .stream()
                                    .map(StationResponse::getId)
                                    .collect(Collectors.toList());
    List<Long> expectedStationIds = expectStations.stream()
                                    .map(StationResponse::getId)
                                    .collect(Collectors.toList());
    //최단거리 경로 응답
    assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds);
    //총거리도 함께 응답
    assertThat(actual.getDistance()).isEqualTo(expectDistance);
    //지하철 이용 요금도 함께 응답
    assertThat(actual.getRequireFee()).isEqualTo(expectFee);
  }

  private ExtractableResponse<Response> 최단거리_경로_탐색(StationResponse source, StationResponse target) {
    return RestAssured.given().log().all()
            .queryParam("source", source.getId())
            .queryParam("target", target.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 로그인_유저의_최단거리_경로_탐색(String token, StationResponse source, StationResponse target) {
    return RestAssured.given().log().all()
        .auth().oauth2(token)
        .queryParam("source", source.getId())
        .queryParam("target", target.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get("/paths")
        .then().log().all()
        .extract();
  }
}
