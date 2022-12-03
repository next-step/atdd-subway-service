package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
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
    private StationResponse 역삼역;

    private StationResponse 선릉역;

    private StationResponse 고속터미널역;
    private StationResponse 남부터미널역;

    private StationResponse 양재시민의숲;

    /**
     * 지하철 노선도
     *
     * 고속터미널역
     * |
     * *3호선*
     * |
     * 교대역    --- *2호선* ---   강남역 --- *2호선* --- 역삼역 --- *2호선* ---선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                          |
     *                          *신분당선*
     *                          |
     *                          양재시민의 숲
     *
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재시민의숲.getId(), 40)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 선릉역.getId(), 20)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 고속터미널역.getId(), 양재역.getId(), 30)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 고속터미널역, 교대역, 10);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 5);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 20);
    }

    @Test
    @DisplayName("성공적으로 최단 경로를 조회한다")
    void getShortPath() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 양재시민의숲.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        최단경로를_맞게_조회했는지_체크한다(결과, Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 양재시민의숲.getId()), 40);
    }

    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외를 발생한다")
    void isSameStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("존재하지 않는 역이 출발지일 경우 예외가 발생한다")
    void isNotExistStartStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(Station.from("성균관대학교역").getId(), 역삼역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("존재하지 않는 역이 도착지일 경우 예외가 발생한다")
    void isNotExistEndStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), Station.from("성균관대학교역").getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }

    private void 최단경로를_맞게_조회했는지_체크한다(ExtractableResponse<Response> response, List<Long> expectIds, int expectDistance) {
        List<Long> stationIds = response.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId)
                .collect(Collectors.toList());

        int distance = response.jsonPath().getInt("distance");

        assertThat(stationIds).containsAll(expectIds);
        assertThat(distance).isEqualTo(expectDistance);
    }

    private void 상태값이_기대값과_일치하는지_체크한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }


    private static ExtractableResponse<Response> 지하철_경로_조회_요청(Long sourceId, Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
