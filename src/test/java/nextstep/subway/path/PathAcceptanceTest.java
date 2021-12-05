package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.exception.StationNotFoundException;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

/***
 *
 *   교대역   ↔(3) 남부터미널역  ↔(3) 매봉역
 *     ↕(2)                        ↕(3)
 *   강남역   ←---- (12) -----→  양재역  ←----- (12) -----→ 양재시민의숲역(START)
 *     ↕(2)
 *   역삼역(DESTINATION)
 *
 *   목표 예상 경로 : (신분당선탑승) 양재시민의숲역(출발) -> 양재역 -> (3호선환승) 매봉역 -> 남부터미널역 -> 교대역 -> (2호선환승) -> 강남역 -> 역삼역 (도착)
 *   예상 이동 역 개수 : 7개
 *   예상 이동 거리 : 25
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String BASE_URI = "paths";

    private static final int 신분당선_거리_12 = 12;
    private static final int 이호선_거리_2 = 2;
    private static final int 삼호선_거리_3 = 3;

    private Long 존재하지않은역_ID = Long.MAX_VALUE;
    private Long 연결되지않은역_ID;
    private Long 양재시민의숲역_ID;
    private Long 역삼역_ID;

    @BeforeEach
    public void setUp() {
        super.setUp();
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        StationResponse 교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 매봉역 = 지하철역_등록되어_있음("매봉역").as(StationResponse.class);

        StationResponse 양재시민의숲역 = 지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
        양재시민의숲역_ID = 양재시민의숲역.getId();

        StationResponse 역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        역삼역_ID = 역삼역.getId();

        StationResponse 연결되지않은역 = 지하철역_등록되어_있음("연결되지않은역").as(StationResponse.class);
        연결되지않은역_ID = 연결되지않은역.getId();

        //신분당선 강남역 - 양재역 - 양재시민의숲역 (각 구간의 거리 10)
        LineResponse 신분당선 =
                지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 신분당선_거리_12)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲역, 신분당선_거리_12);

        //2호선 역삼역 - 강남역 - 교대역 (각 구간의 거리 2)
        LineResponse 이호선 =
                지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 역삼역.getId(), 강남역.getId(), 이호선_거리_2)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 교대역, 이호선_거리_2);

        //3호선 매봉역 - 양재역 - 교대역  (각 구간의 거리 3)
        LineResponse 삼호선 =
                지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 양재역.getId(), 매봉역.getId(), 삼호선_거리_3)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 매봉역, 남부터미널역, 삼호선_거리_3);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 삼호선_거리_3);
    }

    @Test
    @DisplayName("양재시민의숲역에서_역삼역까지의 거리를 구한다.")
    public void findShortPath() {
        // given
        int expect = 7;

        // when
        ExtractableResponse<Response> response = 최단거리_조회_요청함(양재시민의숲역_ID, 역삼역_ID);

        // then
        PathResponse pathResponse = 최단거리_조회_응답됨(response);
        최단거리를_수동으로_검증(pathResponse, expect);
    }

    @Test
    @DisplayName("출발지와 목적지가 연결되어 있지 않음")
    public void pathNotFound() {
        // when
        ExtractableResponse<Response> 출발지_목적지_미연결 = 최단거리_조회_요청함(양재시민의숲역_ID, 연결되지않은역_ID);

        // then
        경로_조회_실패됨_미연결(출발지_목적지_미연결);

        ExtractableResponse<Response> 출발지_목적지_미연결_역방향 = 최단거리_조회_요청함(연결되지않은역_ID, 양재시민의숲역_ID);

        // then
        경로_조회_실패됨_미연결(출발지_목적지_미연결_역방향);
    }

    @Test
    @DisplayName("출발지와 목적지 경로를 같게 설정한다.")
    public void pathBeginIsEnd() {
        // when
        ExtractableResponse<Response> response = 최단거리_조회_요청함(양재시민의숲역_ID, 양재시민의숲역_ID);

        // then
        경로_조회_실패됨_출발지_목적지_경로가_같음(response);
    }

    @Test
    @DisplayName("출발지 또는 목적지를 존재하지 않은 역으로 조회한다.")
    public void pathNotExistStation() {
        // when
        ExtractableResponse<Response> 목적지가_미존재_응답 = 최단거리_조회_요청함(양재시민의숲역_ID, 존재하지않은역_ID);

        // then
        경로_조회_실패됨_역없음(목적지가_미존재_응답);

        // when
        ExtractableResponse<Response> 출발지_미존재_응답 = 최단거리_조회_요청함(존재하지않은역_ID, 양재시민의숲역_ID);

        // then
        경로_조회_실패됨_역없음(출발지_미존재_응답);
    }

    private void 최단거리를_수동으로_검증(PathResponse pathResponse, int expect) {
        assertThat(pathResponse.getStations()).hasSize(expect);
    }

    private PathResponse 최단거리_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.jsonPath()
                .getObject("", PathResponse.class);
        return pathResponse;
    }

    private ExtractableResponse<Response> 최단거리_조회_요청함(Long srcStationId, Long destStationId) {
        return RestAssured
                .given().log().all()
                .param("source", srcStationId)
                .param("target", destStationId)
                .when()
                .get(BASE_URI)
                .then().log().all().extract();
    }

    private void 경로_조회_실패됨_미연결(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body()
                .asString())
                .contains(PathNotFoundException.message);
    }

    private void 경로_조회_실패됨_출발지_목적지_경로가_같음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString())
                .contains(PathBeginIsEndException.message);
    }

    private void 경로_조회_실패됨_역없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString())
                .contains(StationNotFoundException.message);
    }
}
