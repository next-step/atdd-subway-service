package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private StationResponse 학여울역;
    private StationResponse 수서역;
    private StationResponse 양재시민의숲;

    /**
     * 교대역 -- *2호선* 10 --- 강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 3                        10
     * |                        |
     * 남부터미널역 -- *3호선* 2 -양재 -- *3호선* 15 -- 학여울역 -- *3호선* 50 -- 수서역
     *                          |
     *                          *신분당선*
     *                          4
     *                          |
     *                          양재시민의숲
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        학여울역 = StationAcceptanceTest.지하철역_등록되어_있음("학여울역").as(StationResponse.class);
        수서역 = StationAcceptanceTest.지하철역_등록되어_있음("수서역").as(StationResponse.class);
        양재시민의숲 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900L)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 학여울역, 15);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 학여울역, 수서역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲, 4);
    }

    @DisplayName("다양한 최단 경로 및 요금을 조회한다. 성공 검증")
    @Test
    void successPaths() {
        ExtractableResponse<Response> 최단_경로_기본요금 = 최단_경로_조회_요청(교대역, 양재역);

        최단_경로_조회_됨(최단_경로_기본요금, Arrays.asList(교대역, 남부터미널역, 양재역), 5L, 1250L);

        ExtractableResponse<Response> 중간_경로_요금 = 최단_경로_조회_요청(교대역, 학여울역);

        최단_경로_조회_됨(중간_경로_요금, Arrays.asList(교대역, 남부터미널역, 양재역, 학여울역), 20L, 1650L);

        ExtractableResponse<Response> 최대_경로_요금 = 최단_경로_조회_요청(교대역, 수서역);

        최단_경로_조회_됨(최대_경로_요금, Arrays.asList(교대역, 남부터미널역, 양재역, 학여울역, 수서역), 70L, 2150L);

        /*ExtractableResponse<Response> 노선별_요금 = 최단_경로_조회_요청(교대역, 양재시민의숲);

        최단_경로_조회_됨(노선별_요금, Arrays.asList(교대역, 남부터미널역, 양재역, 양재시민의숲), 9L, new BigInteger("2150"));*/
    }

    @DisplayName("최단 경로를 조회한다. 실패 검증")
    @Test
    void failsPaths() {
        ExtractableResponse<Response> 출발역과_도착역이_같음 = 최단_경로_조회_요청(교대역, 교대역);

        최단_경로_조회_실패(출발역과_도착역이_같음);

        StationResponse 용마산역 = StationAcceptanceTest.지하철역_등록되어_있음("용마산역").as(StationResponse.class);
        StationResponse 중곡역 = StationAcceptanceTest.지하철역_등록되어_있음("중곡역").as(StationResponse.class);
        LineResponse 칠호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("칠호선", "bg-red-600", 용마산역.getId(), 중곡역.getId(), 5)).as(LineResponse.class);
        ExtractableResponse<Response> 출발역과_도착역이_연결이_안되어있음 = 최단_경로_조회_요청(용마산역, 교대역);

        최단_경로_조회_실패(출발역과_도착역이_연결이_안되어있음);

        ExtractableResponse<Response> 존재하지_않은_출발역_도착역 = 최단_경로_조회_요청(-1L, 0L);

        존재하지_않은_지하철_조회_실패(존재하지_않은_출발역_도착역);
    }

    private void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 존재하지_않은_지하철_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", sourceId, targetId)
                .then().log().all()
                .extract();

    }


    private static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        PathRequest pathRequest = new PathRequest(sourceStation.getId(), targetStation.getId());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", sourceStation.getId(), targetStation.getId())
                .then().log().all()
                .extract();

    }

    private void 최단_경로_조회_됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations, Long expectedDistance, long expectedFare) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        Long distance = path.getDistance();
        Long fare = path.getFare();
        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
        assertThat(distance).isEqualTo(expectedDistance);
        assertThat(fare).isEqualTo(expectedFare);
    }
}
