package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


/**
 * Feature: 지하철 경로 관련 기능
 * <p>
 * Background
 * Given 지하철역 여러개 등록되어 있음
 * And 지하철 노선 여러개 등록되어 있음
 * And 지하철 노선에 지하철역(지하철 구간) 여러개 등록되어 있음
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 분당선;
    private LineResponse 삼호선;
    private LineResponse 팔호선;
    private StationResponse 정자역;
    private StationResponse 양재역;
    private StationResponse 수서역;
    private StationResponse 서현역;
    private StationResponse 잠실역;
    private StationResponse 복정역;
    private String 성인회원;
    private String adultEmail = "adult@gmail.com";

    /**
     * 양재역 ------*3호선(5)*------ 수서역
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * *신분당선(10)*             *분당선(5)*
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * 정쟈역 ------*분당선(5)*------ 서현역
     * 잠실역 ------*팔호선(20)*----- 복정역
     */
    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        지하철역_여러개_등록();

        노선_여러개_등록();

        지하철_노선에_지하철역_등록(분당선, 서현역, 정자역, 5);

        성인_회원_등록();

        성인_회원_로그인_됨();
    }


    /**
     * Scenario: 최단 구간을 조회
     * When 지하철 경로 조회 요청
     * Then 출발역과 도착역 사이의 최단 경로 조회됨.
     */
    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        ExtractableResponse<Response> 성인_회원_경로조회_결과 = 최단_경로_조회_요청(성인회원, 양재역.getId(), 서현역.getId());
        지하철_최단_경로_조회됨(성인_회원_경로조회_결과, 10, 1450);


    }


    /**
     * Scenario: 최단 구간을 조회
     * When 열결되지 않은 출발역과 도착역으로 최단 거리 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("연결되지 않은 역의 최단 거리를 조회할 때 예외가 발생한다.")
    @Test
    void findShortestPathNotConnectedException() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원, 수서역.getId(), 잠실역.getId());

        //then
        지하철_최단_경로_조회_실패됨(response);
    }

    /**
     * Scenario: 최단 구간을 조회
     * When 출발역과 같은 도착역으로 최단 거리 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("출발역과 같은 도착역을 입력하면 예외가 발생한다.")
    @Test
    void findShortestPathInvalidSameStationsException() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원, 수서역.getId(), 수서역.getId());

        //then
        지하철_최단_경로_조회_실패됨(response);
    }

    /**
     * Scenario: 최단 구간을 조회
     * When 존재하지 않는 역으로 최단 거리 조회 요청
     * Then 최단 경로 조회 실패
     */
    @DisplayName("존재하지 않는 역으로 최단 거리를 조회할 때 예외가 발생한다.")
    @Test
    void findShortestPathNotExistsException() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원, 수서역.getId(), 0L);

        //then
        지하철_최단_경로_조회_실패됨(response);
    }


    private ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .queryParams(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }


    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, int distance, int fare) {
        List<String> stationNames = response.as(PathResponse.class).getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance),
                () -> assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare),
                () -> assertThat(stationNames).containsExactly("양재역", "수서역", "서현역")
        );
    }

    private void 지하철_최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



    private void 지하철역_여러개_등록() {
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        수서역 = StationAcceptanceTest.지하철역_등록되어_있음("수서역").as(StationResponse.class);
        서현역 = StationAcceptanceTest.지하철역_등록되어_있음("서현역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        복정역 = StationAcceptanceTest.지하철역_등록되어_있음("복정역").as(StationResponse.class);
    }

    private void 노선_여러개_등록() {
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("신분당선", "red", 양재역.getId(), 정자역.getId(), 10))
                .as(LineResponse.class);
        분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("분당선", "yellow", 수서역.getId(), 정자역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("삼호선", "orange", 양재역.getId(), 수서역.getId(), 5))
                .as(LineResponse.class);
        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("팔호선", "pink", 잠실역.getId(), 복정역.getId(), 20))
                .as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private void 성인_회원_등록() {
        ExtractableResponse<Response> createAdultResponse = 회원_생성을_요청(adultEmail, PASSWORD, 19);
        회원_생성됨(createAdultResponse);
    }

    private void 성인_회원_로그인_됨() {
        ExtractableResponse<Response> loginAdultResponse = 로그인_요청(new TokenRequest(adultEmail, PASSWORD));
        로그인_됨(loginAdultResponse);

        성인회원 = loginAdultResponse.as(TokenResponse.class).getAccessToken();
    }

}
