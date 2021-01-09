package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.AGE;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 경의중앙선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 용산역;
    private StationResponse 공덕역;
    private StationResponse 오금역;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  ---  오금
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        용산역 = StationAcceptanceTest.지하철역_등록되어_있음("용산역").as(StationResponse.class);
        공덕역 = StationAcceptanceTest.지하철역_등록되어_있음("공덕역").as(StationResponse.class);
        오금역 = StationAcceptanceTest.지하철역_등록되어_있음("오금역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 900);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 오금역, 50);
    }

    @Test
    @DisplayName("지하철 경로 조회 시 거리 기준 요금 정보를 포함한다: 거리, 노선별 요금 정책 만족 (로그인X)")
    public void findPathWithFeeDistance() throws Exception {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        // then
        List<StationResponse> stationResponses = Arrays.asList(남부터미널역, 양재역, 강남역);
        지하철_최단경로_응답됨(response, stationResponses, 12);
        지하철_이용요금_응답됨(response, 2_250); // 1250(기본요금) + 100(거리요금) + 900(노선요금) == 2250
    }

    @Test
    @DisplayName("지하철 경로 조회 시 거리 기준 요금 정보를 포함한다: 로그인 사용자의 경우 연령별 요금 할인 적용(어린이)")
    public void findPathWithFeeLoginChild() throws Exception {
        // given
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 10;
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> loginResponse = 회원_로그인_요청(EMAIL, PASSWORD);
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        // then
        회원_로그인_됨(loginResponse);
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 강남역.getId(), accessToken);

        // then
        List<StationResponse> stationResponses = Arrays.asList(남부터미널역, 양재역, 강남역);
        지하철_최단경로_응답됨(response, stationResponses, 12);
        지하철_이용요금_응답됨(response, 950); // 1250(기본요금) + 100(거리요금) + 900(노선요금) == (2250 - 350) * 0.5
    }

    @Test
    @DisplayName("지하철 경로 조회 시 거리 기준 요금 정보를 포함한다: 로그인 사용자의 경우 연령별 요금 할인 적용(청소년)")
    public void findPathWithFeeLoginTeen() throws Exception {
        // given
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 15;
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> loginResponse = 회원_로그인_요청(EMAIL, PASSWORD);
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        // then
        회원_로그인_됨(loginResponse);
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 강남역.getId(), accessToken);

        // then
        List<StationResponse> stationResponses = Arrays.asList(남부터미널역, 양재역, 강남역);
        지하철_최단경로_응답됨(response, stationResponses, 12);
        지하철_이용요금_응답됨(response, 1_520); // 1250(기본요금) + 100(거리요금) + 900(노선요금) == (2250 - 350) * 0.8 == 1520
    }

    @Test
    @DisplayName("지하철 구간 최단경로를 조회한다.")
    public void findPath() throws Exception {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        List<StationResponse> stationResponses = Arrays.asList(교대역, 남부터미널역, 양재역);
        지하철_최단경로_응답됨(response, stationResponses, 5);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 응답 실패")
    public void sameSourceTarget() throws Exception {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        지하철_최단경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 응답 실패")
    public void notEdgeSourceTarget() throws Exception {
        // given
        경의중앙선 = 지하철_노선_등록되어_있음("경의중앙선", "bg-skyblue-600", 공덕역, 용산역, 10, 0);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 용산역.getId());

        // then
        지하철_최단경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 응답 실패")
    public void notFoundSourceTarget() throws Exception {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 잠실역.getId());

        // then
        지하철_최단경로_응답_실패됨(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int fee) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, fee);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long source, Long target, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public void 지하철_최단경로_응답됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses, int distance) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
        List<Long> expectedStations = stationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> resultStationIds = pathResponse.getStations().stream()
                .map(PathStation::getId)
                .collect(Collectors.toList());

        assertThat(expectedStations).containsAll(resultStationIds);
        assertThat(distance).isEqualTo(pathResponse.getDistance());
    }

    public void 지하철_최단경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.statusCode());
    }

    public void 지하철_이용요금_응답됨(ExtractableResponse<Response> response, int fee) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(fee).isEqualTo(pathResponse.getFee());
    }
}
