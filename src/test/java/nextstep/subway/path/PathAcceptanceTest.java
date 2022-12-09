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
import static org.junit.jupiter.api.Assertions.assertAll;

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
     * stationA  --- lineD    ---       stationD                      stationF
     * /             거리 1               /                              /
     * *lineA* 거리 5, 추가요금 900    lineC 거리 1               lineE (거리 11, 추가요금 900원)
     * /                                    /                            /
     * stationB  ---   lineB  ---  stationC                      stationE
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationA = 지하철역_생성_요청(memberA, "A").jsonPath().getLong("id");
        stationB = 지하철역_생성_요청(memberA, "B").jsonPath().getLong("id");
        stationC = 지하철역_생성_요청(memberA, "C").jsonPath().getLong("id");
        stationD = 지하철역_생성_요청(memberA, "D").jsonPath().getLong("id");
        stationE = 지하철역_생성_요청(memberA, "E").jsonPath().getLong("id");
        stationF = 지하철역_생성_요청(memberA, "F").jsonPath().getLong("id");

        lineA = 지하철_노선_생성_요청(createLineCreateParams("A", "red", stationA, stationB, 5, 900)).jsonPath().getLong("id");
        lineB = 지하철_노선_생성_요청(createLineCreateParams("B", "blue", stationB, stationC, 3, 0)).jsonPath().getLong("id");
        lineC = 지하철_노선_생성_요청(createLineCreateParams("C", "yellow", stationC, stationD, 1, 0)).jsonPath().getLong("id");
        lineD = 지하철_노선_생성_요청(createLineCreateParams("D", "orange", stationD, stationA, 1, 0)).jsonPath().getLong("id");
        lineE = 지하철_노선_생성_요청(createLineCreateParams("E", "grey", stationE, stationF, 11, 900)).jsonPath().getLong("id");
    }

    /*
        Scenario: 출발역과 도착역이 같은 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역과 도착역이 같은 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @DisplayName("출발역과 도착역이 같은 두 역의 최단 거리 경로를 조회")
    @Test
    void findPath_fail_sameStation() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(memberA, stationA, stationA);
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
    @DisplayName("연결되지 않은 두 역의 최단 거리 경로를 조회")
    @Test
    void findPath_fail_notConnect() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(memberA, stationA, stationF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 지하철 이용 요금도 함께 응답함
    */
    @DisplayName("두 역의 최단 거리 경로를 조회")
    @Test
    void findPath_success() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(memberA, stationA, stationC);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationA, stationD, stationC),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(2),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /*
        Scenario: 존재하지 않는 역의 최단 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 존재하지 않는 역의 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @DisplayName("존재하지 않는 역의 최단 경로를 조회")
    @Test
    void findPath_fail_notExist() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(memberA, 100L, stationF);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /*
      Scenario: 두 역의 최단 거리 경로를 조회 / 노선 추가 요금 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답 (5KM)
        And 지하철 이용 요금도 함께 응답함
          기본요금(1250) + 노선요금(900)
    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 노선 추가 요금 조회")
    @Test
    void findPath_addFare() {

        ExtractableResponse<Response> response = 지하철_경로_조회_요청(memberA, stationA, stationB);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationA, stationB),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    /*
      Scenario: 두 역의 최단 거리 경로를 조회 / 청소년 할인

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답 (11KM)
        And 지하철 이용 요금도 함께 응답함
          기본요금(1250) + 노선요금(900) + 거리요금(100) + 청소년 할인
    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 청소년 할인")
    @Test
    void findPath_addFare_discount_teenager() {

        ExtractableResponse<Response> response = 지하철_경로_조회_요청(teenager, stationA, stationB);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationA, stationB),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1440)
        );
    }


    /*
      Scenario: 비로그인 사용자의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답 (11KM)
        And 지하철 이용 요금도 함께 응답함
          기본요금(1250) + 노선요금(900) + 거리요금(100)
    */
    @DisplayName("비로그인 사용자의 최단 거리 경로를 조회")
    @Test
    void findPath_Guest() {

        ExtractableResponse<Response> response = 지하철_경로_조회_요청(stationA, stationB);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationA, stationB),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    /*
      Scenario: 두 역의 최단 거리 경로를 조회 / 어린이 할인

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답 (11KM)
        And 지하철 이용 요금도 함께 응답함
            기본요금(1250) + 노선요금(900) + 거리요금(100) + 어린이 할인
   */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 어린이 할인")
    @Test
    void findPath_addFare_discount_children() {

        ExtractableResponse<Response> response = 지하철_경로_조회_요청(children, stationE, stationF);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationE, stationF),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(11),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(950)
        );
    }

    private Map<String, String> createLineCreateParams(String name, String color, Long upStationId, Long downStationId, int distance, int fare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("fare", fare + "");
        return lineCreateParams;
    }
}
