package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어있음;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
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

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String TEST_EMAIL = "email@email.com";
    private static final String TEST_PWD = "password";
    private static final int TEST_AGE = 100;
    private static String accessToken;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 정자역;

    /**
     * 교대역  --- *2호선(10)* --- 강남역
     * |                           |
     * *3호선(3)*                  *신분당선(10)*
     * |                           |
     * 남부터미널역 --- *3호선(2)* --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured.defaultParser = Parser.JSON;

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 800);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음(TEST_EMAIL, TEST_PWD, TEST_AGE);
        accessToken = 로그인_되어있음(TEST_EMAIL, TEST_PWD);
    }

    @DisplayName("출발역에서 도착역으로 가는 최단 경로를 조회한다")
    @Test
    public void findShortestPathTest() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        // then
        최단_경로_조회됨(response, 남부터미널역, 양재역, 강남역);
        최단_경로_거리_일치함(response, 12);
        최단_경로_요금_일치함(response, 2250);
    }

    @DisplayName("출발역에서 도착역이 동일할 때 조회 실패한다.")
    @Test
    public void findShortestPathTest_sameSrcAndDest() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        최단_경로_조회_실패함(response);
    }

    @DisplayName("출발역이 대상 노선 내에 없을 때 조회 실패한다.")
    @Test
    public void findShortestPathTest_stationNotExist() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(정자역.getId(), 양재역.getId());

        // then
        최단_경로_조회_실패함(response);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(Long srcStationId,
        Long destStationId) {
        PathRequest param = new PathRequest(srcStationId, destStationId);
        return 지하철_경로_조회_요청(param);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(PathRequest params) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams("source", params.getSrcStationId())
            .queryParams("target", params.getDestStationId())
            .when().get("/path")
            .then().log().all().
                extract();
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response,
        StationResponse... expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPath())
            .containsExactly(expectedStations);
    }

    private void 최단_경로_거리_일치함(ExtractableResponse<Response> response, double expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPathWeight()).isEqualTo(expectedDistance);
    }

    private void 최단_경로_요금_일치함(ExtractableResponse<Response> response, int expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    private void 최단_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
