package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 잠실역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private static String 성인_토큰;
    private static String 청소년_토큰;
    private static String 어린이_토큰;
    private static String 비회원;


    /**
     * 교대역    --- *2호선* (15) ---   강남역
     * |                        |
     * *3호선* (3)                   *신분당선* (10)
     * |                        |
     * 남부터미널역  --- *3호선* (2) ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);


        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 15)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 남부터미널역.getId(), 양재역.getId(), 2)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceRequest.회원_생성을_요청("성인@email.com", "password", 20);
        MemberAcceptanceRequest.회원_생성을_요청("청소년@email.com", "password", 16);
        MemberAcceptanceRequest.회원_생성을_요청("어린이@email.com", "password", 8);
        성인_토큰 = AuthAcceptanceRequest.로그인_요청("성인@email.com", "password")
                .as(TokenResponse.class)
                .getAccessToken();
        청소년_토큰 = AuthAcceptanceRequest.로그인_요청("청소년@email.com", "password")
                .as(TokenResponse.class)
                .getAccessToken();
        어린이_토큰 = AuthAcceptanceRequest.로그인_요청("어린이@email.com", "password")
                .as(TokenResponse.class)
                .getAccessToken();
        비회원 = "guest";
    }

    /**
     * Feature: 최단 경로 조회 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *   Scenario: 최단 경로를 조회한다.
     *     When 강남역-양재역 최단 경로를 로그인하지 않는 사용자가 조회하면,
     *     Then 경유거리, 이용요금을 응답
     *     When 강남역-양재역 최단 경로를 로그인 한 청소년이 조회하면,
     *     Then 경유거리, 이용요금을 응답
     *     When 강남역-양재역 최단 경로를 로그인 한 어린이가 조회하면,
     *     Then 경유거리, 이용요금을 응답
     */
    @TestFactory
    Stream<DynamicTest> 최단_경로_조회() {
        return Stream.of(
                dynamicTest("강남역-양재역 최단 경로를 로그인하지 않는 사용자가 조회하면, 경유지/경유거리/이용요금을 응답", () -> {
                    //when
                    ExtractableResponse<Response> response = 최단경로_토큰_조회(비회원, 강남역.getId(), 양재역.getId());

                    //then
                    최단경로_토큰_조회_금액_성공(response, 1250, 10);
                }),

                dynamicTest("강남역-양재역 최단 경로를 로그인 한 청소년이 조회하면, 경유지/경유거리/이용요금을 응답", () -> {
                    //when
                    ExtractableResponse<Response> response = 최단경로_토큰_조회(청소년_토큰, 강남역.getId(), 양재역.getId());

                    //then
                    최단경로_토큰_조회_금액_성공(response, 1250, 720);
                }),

                dynamicTest("강남역-양재역 최단 경로를 로그인 한 어린이가 조회하면, 경유지/경유거리/이용요금을 응답", () -> {
                    //when
                    ExtractableResponse<Response> response = 최단경로_토큰_조회(어린이_토큰, 강남역.getId(), 양재역.getId());

                    최단경로_토큰_조회_금액_성공(response, 1250, 450);
                })
        );
    }

    /**
     * Feature: 노선 별 추가요금 테스트
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *   Scenario: 최단 경로를 조회한다.
     *     When 교대역-양재역 최단 경로를 로그인 한 성인이 조회하면,
     *     Then 1250원의 이용요금이 발생한다.
     *     When 강남역-양재역 최단 경로를 로그인 한 성인이 조회하면,
     *     Then 1350원의 이용요금이 발생
     */
    @TestFactory
    Stream<DynamicTest> 노선_별_추가요금_테스트() {
        return Stream.of(
                dynamicTest("교대역-양재역 최단 경로를 로그인 한 성인이 조회하면, 1250원의 이용요금이 발생", () -> {
                    //when
                    ExtractableResponse<Response> response = 최단경로_토큰_조회(성인_토큰, 교대역.getId(), 양재역.getId());

                    //then
                    최단경로_토큰_조회_금액_성공(response, 1250, 5);
                }),

                dynamicTest("교대역-강남역 최단 경로를 로그인 한 성인이 조회하면, 1350원의 이용요금이 발생", () -> {
                    //when
                    ExtractableResponse<Response> response = 최단경로_토큰_조회(성인_토큰, 교대역.getId(), 강남역.getId());

                    //then
                    최단경로_토큰_조회_금액_성공(response, 1350, 15);
                })
        );
    }

    private ExtractableResponse<Response> 최단경로_토큰_조회(String token, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return RestAssured
                .given().log().all().auth().oauth2(token)
                .queryParams(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단경로_토큰_조회_금액_성공(ExtractableResponse<Response> response, int fare, int distance) {
        int resultFare = response.jsonPath().get("fare");
        int resultDistance = response.jsonPath().get("distance");

        assertThat(resultFare).isEqualTo(fare);
        assertThat(resultDistance).isEqualTo(distance);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
