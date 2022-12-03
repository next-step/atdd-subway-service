package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.path.PathSteps.지하철_경로_조회_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long lineA;
    private Long lineB;
    private Long lineC;
    private Long lineD;
    private Long lineE;

    private Long stationA;
    private Long stationB;
    private Long stationC;
    private Long stationD;
    private Long stationE;
    private Long stationF;

    /**
     * stationA  --- lineD    --- stationD                      stationF
     * /             거리 1        /                              /
     * *lineA* 거리 5                lineC 거리 1               *lineE* 거리 2
     * /                             /                            /
     * stationB  ---   lineB ---  stationC                      stationE
     * 거리 3
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationA = 지하철역_생성_요청("A").jsonPath().getLong("id");
        stationB = 지하철역_생성_요청("B").jsonPath().getLong("id");
        stationC = 지하철역_생성_요청("C").jsonPath().getLong("id");
        stationD = 지하철역_생성_요청("D").jsonPath().getLong("id");
        stationE = 지하철역_생성_요청("E").jsonPath().getLong("id");
        stationF = 지하철역_생성_요청("F").jsonPath().getLong("id");

        lineA = 지하철_노선_생성_요청(createLineCreateParams("A", "red", stationA, stationB, 5)).jsonPath().getLong("id");
        lineB = 지하철_노선_생성_요청(createLineCreateParams("B", "blue", stationB, stationC, 3)).jsonPath().getLong("id");
        lineC = 지하철_노선_생성_요청(createLineCreateParams("C", "yellow", stationC, stationD, 1)).jsonPath().getLong("id");
        lineD = 지하철_노선_생성_요청(createLineCreateParams("D", "orange", stationD, stationA, 1)).jsonPath().getLong("id");
        lineE = 지하철_노선_생성_요청(createLineCreateParams("E", "grey", stationE, stationF, 2)).jsonPath().getLong("id");
    }

    /*
        Scenario: 출발역과 도착역이 같은 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역과 도착역이 같은 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_sameStation() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(stationA, stationA);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
        Scenario: 연결되지 않은 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And A 노선이 등록되어 있음
        And B 노선이 등록되어 있음
        And A 노선과 B 노선이 연결되어 있지 않음

        When 연결되어 있지 않은 A 노선과 B 노선의 역의 최단 거리 경로를 조회 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_notConnect() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(stationA, stationF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
    */
    @Test
    void findPath_success() {
    }

    /*
        Scenario: 존재하지 않는 역의 최단 경로르 ㄹ조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 존재하지 않는 역의 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_notExist() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(100L, stationF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Map<String, String> createLineCreateParams(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", distance + "");
        return lineCreateParams;
    }
}
