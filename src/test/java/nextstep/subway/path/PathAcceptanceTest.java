package nextstep.subway.path;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 신분당선;
    private StationResponse 선릉역;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;

    /**
     * 교대역    --- *2호선 10km * --- 강남역 ---- 50km ----- 선릉역
     * |                        |
     * *3호선 50km*              *신분당선 10km*
     * |                        |
     * 남부터미널역  --- *3호선 10km* --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("이호선", "파란색", 교대역.getId(), 선릉역.getId(), 60, 0).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "초록색", 교대역.getId(), 양재역.getId(), 60, 0).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "빨간색", 강남역.getId(), 양재역.getId(), 10, 1000).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 50);
        지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 10);
    }

    @DisplayName("최단경로를 조회한다.")
    @Test
    public void findPath() {
        // given
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 교대역.getId(), 양재역.getId());

        // then
        최단경로_조회_성공(response, 20, Arrays.asList(교대역, 강남역, 양재역));
    }

    @DisplayName("출발역과 도착역을 같은 역으로 최단경로를 조회한다.")
    @Test
    public void findPathSameStation() {
        // given
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 교대역.getId(), 교대역.getId());

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("연결되지 않는 경로를 조회한다.")
    @Test
    public void findNotConnectPath() {
        // given
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);
        StationResponse stationResponse = 지하철역_등록되어_있음("상록수역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), stationResponse.getId(), 교대역.getId());

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역으로 조회한다.")
    @Test
    public void findPathNotExistStation() {
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 9L, 교대역.getId());

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("기본 운임비용")
    @Test
    public void getGeneralFare() {
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 강남역.getId(), 교대역.getId());

        // then
        PathResponse pathResponse = response.as(PathResponse.class);
        long expectedFare = 1250;
        요금도_함께_응답함(response, expectedFare);
    }

    @DisplayName("청소년 운임비용")
    @Test
    public void getYouthFare() {
        회원_등록되어_있음("test@naver.com", "1234", 18);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 강남역.getId(), 교대역.getId());

        // then
        long expectedFare = (long) ((1250 - 350) * 0.7);
        요금도_함께_응답함(response, expectedFare);
    }

    @DisplayName("어린이 운임비용")
    @Test
    public void getChildrenFare() {
        회원_등록되어_있음("test@naver.com", "1234", 6);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 강남역.getId(), 교대역.getId());

        // then
        long expectedFare = (long) ((1250 - 350) * 0.5);
        요금도_함께_응답함(response, expectedFare);
    }

    @DisplayName("노선추가 운임비용")
    @Test
    public void getLineExtraFare() {
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 강남역.getId(), 양재역.getId());

        // then
        long expectedFare = 1250 + 신분당선.getExtraFare();
        요금도_함께_응답함(response, expectedFare);
    }

    @DisplayName("거리별 운임비용 50km 이하")
    @Test
    public void getDistanceExtraFare_50KM_이하() {
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 강남역.getId(), 선릉역.getId());

        // then
        PathResponse pathResponse = response.as(PathResponse.class);
        long expectedFare = 1250 + (long) ((Math.ceil((pathResponse.getDistance() - 1) / 5) + 1) * 100);
        요금도_함께_응답함(response, expectedFare);
    }

    @DisplayName("거리별 운임비용 50km 초과")
    @Test
    public void getDistanceExtraFare_50KM_초과() {
        회원_등록되어_있음("test@naver.com", "1234", 20);
        TokenResponse tokenResponse = 로그인_요청("test@naver.com", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(tokenResponse.getAccessToken(), 교대역.getId(), 선릉역.getId());

        // then
        PathResponse pathResponse = response.as(PathResponse.class);
        long expectedFare = 1250 + (long) ((Math.ceil((pathResponse.getDistance() - 1) / 8) + 1) * 100);
        요금도_함께_응답함(response, expectedFare);
    }

    private ExtractableResponse<Response> 최단경로_조회_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths/?source={sourceStationId}&target={targetStationId}", sourceStationId, targetStationId)
                .then().log().all()
                .extract();
    }

    private void 최단경로_조회_성공(ExtractableResponse<Response> response, int distance, List<StationResponse> stationResponses) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> expectedStationIds = stationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> resultStationIds = pathResponse.getStations().stream()
                .map(PathResponse.StationInfo::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
                () -> assertThat(resultStationIds).containsAll(expectedStationIds)
        );

    }

    private void 최단경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 요금도_함께_응답함(ExtractableResponse<Response> response, long expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}
