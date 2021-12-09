package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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
import static nextstep.subway.station.StationAcceptanceTest.getStationNames;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 인수 테스트")
class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남;
    private StationResponse 선릉;
    private StationResponse 교대;
    private StationResponse 남부터미널;
    private StationResponse 양재;
    private StationResponse 매봉;
    private StationResponse 이수;
    private StationResponse 사당;

    /**
     * 2호선: 교대 - 강남 - 선릉
     * 3호선: 교대 - 남부터미널 - 양재 - 매봉
     * 4호선: 이수 - 사당
     * 신분당선: 강남 - 양재
     * <p>
     * 교대역 ------- (2) ---- 강남역 ---- (2) --- 선릉
     * |                      |
     * (2)                    (8)
     * |                      |
     * 남부터미널역 --- (5) ---- 양재 ----- (6) --- 매봉
     * <p>
     * 이수 --- (3) --- 사당
     * <p>
     * 양재 -> 교대 : (양재-8-강남-2-교대) 10 / (양재-5-남부터미널-2-교대) 7
     * 매봉 -> 강남 : (매봉-6-양재-8-강남) 14 / (매봉-6-양재-5-남부터미널-2-교대-2-강남) 15
     * 선릉 -> 매봉 : (선릉-2-강남-2-교대-2-남부터미널-5-양재-6-매봉) 17 / (선릉-2-강남-8-양재-6-매봉) 14
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남 = 지하철역_등록되어_있음("강남").as(StationResponse.class);
        선릉 = 지하철역_등록되어_있음("선릉").as(StationResponse.class);
        교대 = 지하철역_등록되어_있음("교대").as(StationResponse.class);
        남부터미널 = 지하철역_등록되어_있음("남부터미널").as(StationResponse.class);
        양재 = 지하철역_등록되어_있음("양재").as(StationResponse.class);
        매봉 = 지하철역_등록되어_있음("매봉").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대, 강남, 2);
        지하철_구간_등록됨(이호선, 강남, 선릉, 2);

        LineResponse 삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대, 남부터미널, 2);
        지하철_구간_등록됨(삼호선, 남부터미널, 양재, 5);
        지하철_구간_등록됨(삼호선, 양재, 매봉, 6);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남, 양재, 8);

        이수 = 지하철역_등록되어_있음("이수").as(StationResponse.class);
        사당 = 지하철역_등록되어_있음("사당").as(StationResponse.class);
        LineResponse 사호선 = 지하철_노선_등록되어_있음("사호선", "bg-sky-600", 이수, 사당, 3);
    }

    @Test
    @DisplayName("최단 경로 정상 기능")
    void normalScenario() {
        ExtractableResponse<Response> response1 = 최단_경로_조회_요청(양재, 교대);
        최단_경로_조회됨(response1);
        최단_경로_구간_목록_일치됨(response1, Arrays.asList(양재, 남부터미널, 교대));

        ExtractableResponse<Response> response2 = 최단_경로_조회_요청(매봉, 강남);
        최단_경로_조회됨(response2);
        최단_경로_구간_목록_일치됨(response2, Arrays.asList(매봉, 양재, 강남));

        ExtractableResponse<Response> response3 = 최단_경로_조회_요청(선릉, 매봉);
        최단_경로_조회됨(response3);
        최단_경로_구간_목록_일치됨(response3, Arrays.asList(선릉, 강남, 양재, 매봉));
    }

    @Test
    @DisplayName("최단 경로 예외 발생")
    void exceptionScenario() {
        StationResponse 양재시민의숲 = 노선에_등록되지_않은_역("양재시민의숲");
        최단_경로_조회_실패됨(최단_경로_조회_요청(양재시민의숲, 교대));

        최단_경로_조회_실패됨(최단_경로_조회_요청(선릉, 선릉));

        최단_경로_조회_실패됨(최단_경로_조회_요청(강남, 이수));
    }

    private StationResponse 노선에_등록되지_않은_역(String name) {
        return 지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured.given().log().params()
                .queryParam("sourceId", source.getId())
                .queryParam("targetId", target.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_구간_목록_일치됨(ExtractableResponse<Response> response, List<StationResponse> excepted) {
        assertThat(response.jsonPath().getList("stations.name", String.class))
                .isEqualTo(getStationNames(excepted));
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
