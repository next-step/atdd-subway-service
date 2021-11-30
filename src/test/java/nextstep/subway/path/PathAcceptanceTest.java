package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;

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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 지하철_노선에_미등록_역_조회_요청() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 사당역);

        응답_BAD_REQUEST(response);
    }


    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 지하철_노선에_조회_요청_출발역_도착역_동일_오류() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 강남역);

        응답_BAD_REQUEST(response);
    }

    @Test
    @DisplayName(
            "지하철 노선에 등록된 역 기준으로 순방향 최단거리 검증"
                    + "(교대 --(10)> 강남 --(8)> 양재 --(5)> 남부터미널역)"
                    + "(교대 --(3)> 남부터미널역 <========= 가장 최단거리)"
    )
    public void 지하철_노선에_등록_역_최단거리_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(교대역, 남부터미널역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName(
            "지하철 노선에 등록된 역 기준으로 역방향 최단거리 검증"
                    + "(교대 <(10)-- 강남 <(8)-- 양재 <(5)-- 남부터미널역)"
                    + "(교대 <(3)-- 남부터미널역 <========= 가장 최단거리)"
    )
    public void 지하철_노선에_등록_역_역방향_최단거리_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(남부터미널역, 교대역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName(
            "최단 거리 운임 비용 검증"
                    + "(교대 --(10)> 강남 --(8)> 양재 --(5)> 남부터미널역)"
                    + "(강남 --> 남부터미널역)"
                    + "거리 : 13 / 예상 운임비용 : 1250"
    )
    public void 지하철_노선에_등록_역_최단거리_운임_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 남부터미널역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        운임_비용_확인(actual);
    }

    private static void 운임_비용_확인(PathResponse actual) {
        assertThat(actual.getPareMoney()).isEqualTo(BigDecimal.valueOf(1250));
    }

    private static ExtractableResponse<Response> 최단_거리_조회_요청(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("source", 출발역.getId(), "target", 도착역.getId())
                .when().get("/paths?source={source}&target={target}")
                .then().log().all()
                .extract();

    }

    private static void 응답_OK(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 응답_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
