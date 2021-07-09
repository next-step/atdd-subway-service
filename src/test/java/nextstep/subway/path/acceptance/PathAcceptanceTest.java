package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 신림역;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private StationResponse 화곡역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 지하철역_등록_되어_있음
        신림역 = 지하철역_등록("신림역");
        강남역 = 지하철역_등록("강남역");
        교대역 = 지하철역_등록("교대역");
        잠실역 = 지하철역_등록("잠실역");
        삼성역 = 지하철역_등록("삼성역");
        화곡역 = 지하철역_등록("화곡역");
    }

    private StationResponse 지하철역_등록(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    @Test
    @DisplayName("모든 구간의 거리가 동일할 때 최적의 경로 조회")
    void 지하철역_경로_조회() {
        // given
        노선_등록_되어_있음();
        구간_등록_되어_있음();
        //동일한 거리의 다른 경로 구간 등록 되어있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 10);


        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 삼성역);

        // then
        // 최단 경로 조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 삼성역));
    }

    @Test
    @DisplayName("짧은 경로 조회")
    void 지하철역_최적_경로_조회() {
        // given
        노선_등록_되어_있음();
        구간_등록_되어_있음();
        // 구간 수는 많지만 더 짧은 거리의 구간이 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);


        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 삼성역);

        // then
        // 최단 경로 조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 잠실역, 삼성역));
    }

    @Test
    @DisplayName("연결되어 있지 않는 경로 조회")
    void 지하철역_최적_경로_조회_예외() {
        // given
        노선_등록_되어_있음();
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);

        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 신림역);

        // then
        // 최단 경로 조회됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("두 역의 최단 거리 경로를 조회")
    @Test
    void 거리_요금_포함된_지하철_경로_조회_중간_구간() {
        // given
        노선_등록_되어_있음();
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 화곡역, 50);


        // when
        // 출발역에서_도착역까지_최단_거리_경로_조회_요청
        ExtractableResponse<Response> response = PathAcceptanceTest.지하철_경로_조회(강남역, 화곡역);

        // than
        // 최단_거리_경로_조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 잠실역, 삼성역, 화곡역));

        // and
        // 최단_거리_응답함
        지하철_경로_거리_응답됨(pathResponse, 80);

        // and
        // 지하철_이용_요금도_함께_응답됨 30Km -> 1,250 + (100 * 4) = 1,650 + 1호선 900원
        지하철_요금_응답됨(pathResponse, 2550);
    }

    @DisplayName("두 역의 최단 거리 경로를 환승 조회")
    @Test
    void 거리_요금_포함된_지하철_경로_조회_환승_구간() {
        // given
        노선_등록_되어_있음();
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 신림역, 50);


        // when
        // 출발역에서_도착역까지_최단_거리_경로_조회_요청
        ExtractableResponse<Response> response = PathAcceptanceTest.지하철_경로_조회(강남역, 화곡역);

        // than
        // 최단_거리_경로_조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 잠실역, 삼성역, 신림역, 화곡역));

        // and
        // 최단_거리_응답함
        지하철_경로_거리_응답됨(pathResponse, 90);

        // and
        // 지하철_이용_요금도_함께_응답됨 30Km -> 1,250 + (100 * 5) = 1,750 + 5호선 1000원
        지하철_요금_응답됨(pathResponse, 2750);
    }

    @DisplayName("유아 로그인 두 역의 최단 거리 유아용 요금 조회")
    @Test
    void 지하철_경로_유아용_요금_조회() {
        // given
        노선_등록_되어_있음();
        지하철_구간_등록_되어_있음();

        // and
        // 유아용 로그인 되어 있음
        MemberAcceptanceTest.회원_생성을_요청("child@test.com", "1234", 7);
        String token = MemberAcceptanceTest.토큰_발급을_요청("child@test.com", "1234").jsonPath().getString("accessToken");

        // when
        // 출발역에서_도착역까지_최단_거리_경로_조회_요청
        ExtractableResponse<Response> response = PathAcceptanceTest.지하철_인증_경로_조회(token, 강남역, 화곡역);

        // than
        // 최단_거리_경로_조회됨
        PathResponse pathResponse = response.as(PathResponse.class);

        // and
        // 지하철_이용_요금도_함께_응답됨 30Km -> (1,250 + (100 * 5) - 350 + 1000) * 0.5 = 1200
        지하철_요금_응답됨(pathResponse, 1200);
    }

    @DisplayName("청소년 로그인 두 역의 최단 거리 청소년용 요금 조회")
    @Test
    void 지하철_경로_청소년_요금_조회() {
        // given
        노선_등록_되어_있음();
        지하철_구간_등록_되어_있음();

        // and
        // 유아용 로그인 되어 있음
        MemberAcceptanceTest.회원_생성을_요청("child@test.com", "1234", 15);
        String token = MemberAcceptanceTest.토큰_발급을_요청("child@test.com", "1234").jsonPath().getString("accessToken");

        // when
        // 출발역에서_도착역까지_최단_거리_경로_조회_요청
        ExtractableResponse<Response> response = PathAcceptanceTest.지하철_인증_경로_조회(token, 강남역, 화곡역);

        // than
        // 최단_거리_경로_조회됨
        PathResponse pathResponse = response.as(PathResponse.class);

        // and
        // 지하철_이용_요금도_함께_응답됨 30Km -> (1,250 + (100 * 5) - 350 + 1000) * 0.8 = 1920
        지하철_요금_응답됨(pathResponse, 1920);
    }

    private static ExtractableResponse<Response> 지하철_인증_경로_조회(String token, StationResponse 강남역, StationResponse 화곡역) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths/sourceId/{sourceId}/targetId/{targetId}", 강남역.getId(), 화곡역.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_구간_등록_되어_있음() {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 신림역, 50);
    }

    private void 지하철_요금_응답됨(PathResponse pathResponse, int charge) {
        assertThat(pathResponse.getCharge()).isEqualTo(charge);
    }

    private void 지하철_경로_거리_응답됨(PathResponse pathResponse, int distance) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private void 지하철_경로_조회됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);

    }

    public static ExtractableResponse<Response> 지하철_경로_조회(StationResponse 강남역, StationResponse 삼성역) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths/sourceId/{sourceId}/targetId/{targetId}", 강남역.getId(), 삼성역.getId())
                .then().log().all()
                .extract();
    }

    private void 구간_등록_되어_있음() {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
    }

    private void 노선_등록_되어_있음() {
        LineRequest lineRequest = new LineRequest("이호선", "bg-red-600", 강남역.getId(), 교대역.getId(), 10, 900);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("오호선", "bg-red-600", 신림역.getId(), 화곡역.getId(), 10, 1000);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }
}
