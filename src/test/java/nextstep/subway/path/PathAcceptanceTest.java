package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

    private StationResponse 양재시민의숲역;
    private StationResponse 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        StationResponse 교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 매봉역 = 지하철역_등록되어_있음("매봉역").as(StationResponse.class);

        양재시민의숲역 = 지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);

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
                지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 매봉역.getId(), 양재역.getId(), 삼호선_거리_3)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 남부터미널역, 삼호선_거리_3);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 삼호선_거리_3);
    }

    @Test
    @DisplayName("매봉역에서_교대역까지_최단경로_구하기")
    public void test() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("source", 양재시민의숲역.getId())
                .param("target", 역삼역.getId())
                .when()
                .get(BASE_URI)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
