package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록됨;
import static nextstep.subway.path.acceptance.PathFareAcceptanceTest.운임_요금_일치됨;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 인수 테스트")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 선릉역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;
    private StationResponse 매봉역;
    private StationResponse 이수역;
    private StationResponse 사당역;

    /**
     * 2호선: 교대 - 강남 - 선릉
     * 3호선: 교대 - 남부터미널 - 양재 - 매봉
     * 4호선: 이수 - 사당
     * 신분당선: 강남 - 양재
     *
     * 교대역 ------- (2) ---- 강남역 ---- (2) --- 선릉
     * |                      |
     * (2)                    (8)
     * |                      |
     * 남부터미널역 --- (5) ---- 양재 ----- (6) --- 매봉
     *
     * 이수 --- (3) --- 사당
     *
     * 양재 -> 교대 : (양재-8-강남-2-교대) 10 / (양재-5-남부터미널-2-교대) 7
     * 매봉 -> 강남 : (매봉-6-양재-8-강남) 14 / (매봉-6-양재-5-남부터미널-2-교대-2-강남) 15
     * 선릉 -> 매봉 : (선릉-2-강남-2-교대-2-남부터미널-5-양재-6-매봉) 17 / (선릉-2-강남-8-양재-6-매봉) 16
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        선릉역 = 지하철역_생성_요청("선릉역").as(StationResponse.class);
        교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_생성_요청("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);
        매봉역 = 지하철역_생성_요청("매봉역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 0, 교대역, 강남역, 2);
        지하철_구간_등록됨(이호선, 강남역, 선릉역, 2);

        LineResponse 삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 0, 교대역, 남부터미널역, 2);
        지하철_구간_등록됨(삼호선, 남부터미널역, 양재역, 5);
        지하철_구간_등록됨(삼호선, 양재역, 매봉역, 6);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 0, 강남역, 양재역, 8);

        이수역 = 지하철역_생성_요청("이수역").as(StationResponse.class);
        사당역 = 지하철역_생성_요청("사당역").as(StationResponse.class);
        LineResponse 사호선 = 지하철_노선_등록되어_있음("사호선", "bg-sky-600", 0, 이수역, 사당역, 3);
    }

    @Test
    @DisplayName("최단 경로 정상 기능")
    void normalScenario() {
        ExtractableResponse<Response> response7km = 최단_경로_조회_요청(양재역, 교대역);
        최단_경로_조회됨(response7km);
        최단_경로_구간_목록_일치됨(response7km, Arrays.asList(양재역, 남부터미널역, 교대역));
        최단_경로_거리_일치됨(response7km, 7);
        운임_요금_일치됨(response7km, 1250);

        ExtractableResponse<Response> response14km = 최단_경로_조회_요청(매봉역, 강남역);
        최단_경로_조회됨(response14km);
        최단_경로_구간_목록_일치됨(response14km, Arrays.asList(매봉역, 양재역, 강남역));
        최단_경로_거리_일치됨(response14km, 14);
        운임_요금_일치됨(response14km, 1350);

        ExtractableResponse<Response> response16km = 최단_경로_조회_요청(선릉역, 매봉역);
        최단_경로_조회됨(response16km);
        최단_경로_구간_목록_일치됨(response16km, Arrays.asList(선릉역, 강남역, 양재역, 매봉역));
        최단_경로_거리_일치됨(response16km, 16);
        운임_요금_일치됨(response16km, 1450);
    }

    @Test
    @DisplayName("최단 경로 예외 발생")
    void exceptionScenario() {
        // 출발역이나 도착역이 등록되어 있지 않음
        StationResponse 양재시민의숲 = 노선에_등록되지_않은_역("양재시민의숲역");
        최단_경로_조회_실패됨(최단_경로_조회_요청(양재시민의숲, 교대역));

        // 출발역과 도착역이 같음
        최단_경로_조회_실패됨(최단_경로_조회_요청(선릉역, 선릉역));

        // 출발역과 도착역이 연결이 되어 있지 않음
        최단_경로_조회_실패됨(최단_경로_조회_요청(강남역, 이수역));
    }

    private StationResponse 노선에_등록되지_않은_역(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured.given().log().params()
                .queryParam("sourceId", source.getId())
                .queryParam("targetId", target.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(TokenResponse token, StationResponse source, StationResponse target) {
        return RestAssured.given().log().params()
                .queryParam("sourceId", source.getId())
                .queryParam("targetId", target.getId())
                .auth().oauth2(token.getAccessToken())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단_경로_구간_목록_일치됨(ExtractableResponse<Response> response, List<StationResponse> excepted) {
        assertThat(response.jsonPath().getList("stations.name", String.class))
                .isEqualTo(getStationNames(excepted));
    }

    public static void 최단_경로_거리_일치됨(ExtractableResponse<Response> response, int distance) {
        assertThat(response.jsonPath().getObject("distance", Integer.class))
                .isEqualTo(distance);
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
