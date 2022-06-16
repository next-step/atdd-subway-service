package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_생성이_요청됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성후_토큰발급;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 선릉역;
    private StationResponse 삼성역;
    private StationResponse 매봉역;
    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역   --- 선릉역 --- 삼성역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재   --- 매봉역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        매봉역 = StationAcceptanceTest.지하철역_등록되어_있음("매봉역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 15, 1000))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 500))
            .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 매봉역, 45);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 선릉역, 20);
        지하철_노선에_지하철역_등록_요청(이호선, 선릉역, 삼성역, 30);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_생성이_요청됨(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    @Test
    @DisplayName("지하철역 사이의 최단 경로를 조회한다.")
    void findShortestPath() {
        PathResponse pathResponse = 최단_경로를_조회함(accessToken, 교대역, 양재역).as(PathResponse.class);

        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1750);
    }

    @TestFactory
    @DisplayName("지하철역 사이의 최단 경로를 조회한다.")
    Stream<DynamicTest> findShortestPath_fare() {
        String babyAccessToken = 회원_생성후_토큰발급("Test1", "Password1", 5);
        String childAccessToken = 회원_생성후_토큰발급("Test2", "Password1", 10);
        String teenagerAccessToken = 회원_생성후_토큰발급("Test3", "Password1", 15);
        String adultAccessToken = 회원_생성후_토큰발급("Test4", "Password1", 25);
        String oldAccessToken = 회원_생성후_토큰발급("Test5", "Password1", 70);

        return Stream.of(
            dynamicTest("6세 미만이 10KM 이하의 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(babyAccessToken, 교대역, 강남역, 0);
            }),
            dynamicTest("어린이가 10KM 이하의 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(childAccessToken, 교대역, 강남역, 450);
            }),
            dynamicTest("청소년이 10KM 이하의 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(teenagerAccessToken, 교대역, 강남역, 720);
            }),
            dynamicTest("성인이 10KM 이하의 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(adultAccessToken, 교대역, 강남역, 1250);
            }),
            dynamicTest("65세 이상이 10KM 이하의 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(oldAccessToken, 교대역, 강남역, 0);
            }),
            dynamicTest("6세 미만이 30KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(babyAccessToken, 교대역, 선릉역, 0);
            }),
            dynamicTest("어린이가 30KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(childAccessToken, 교대역, 선릉역, 650);
            }),
            dynamicTest("청소년이 30KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(teenagerAccessToken, 교대역, 선릉역, 1040);
            }),
            dynamicTest("성인이 30KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(adultAccessToken, 교대역, 선릉역, 1650);
            }),
            dynamicTest("65세 이상이 30KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(oldAccessToken, 교대역, 선릉역, 0);
            }),
            dynamicTest("6세 미만이 60KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(babyAccessToken, 교대역, 삼성역, 0);
            }),
            dynamicTest("어린이가 60KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(childAccessToken, 교대역, 삼성역, 950);
            }),
            dynamicTest("청소년이 60KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(teenagerAccessToken, 교대역, 삼성역, 1520);
            }),
            dynamicTest("성인이 60KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(adultAccessToken, 교대역, 삼성역, 2250);
            }),
            dynamicTest("65세 이상이 60KM 거리를 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(oldAccessToken, 교대역, 삼성역, 0);
            }),
            dynamicTest("6세 미만이 60KM 거리와 추가요금이 있는 노선을 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(babyAccessToken, 강남역, 매봉역, 0);
            }),
            dynamicTest("어린이가 60KM 거리와 추가요금이 있는 노선을 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(childAccessToken, 강남역, 매봉역, 1950);
            }),
            dynamicTest("청소년이 60KM 거리와 추가요금이 있는 노선을 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(teenagerAccessToken, 강남역, 매봉역, 2520);
            }),
            dynamicTest("성인이 60KM 거리와 추가요금이 있는 노선을 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(adultAccessToken, 강남역, 매봉역, 3250);
            }),
            dynamicTest("65세 이상이 60KM 거리와 추가요금이 있는 노선을 이동할 경우", () -> {
                나이와_최단거리에_따른_요금_검증(oldAccessToken, 강남역, 매봉역, 0);
            })
        );
    }

    private void 나이와_최단거리에_따른_요금_검증(String accessToken, StationResponse source, StationResponse target, int expectedFare) {
        PathResponse response = 최단_경로를_조회함(accessToken, source, target).as(PathResponse.class);
        assertThat(response.getFare()).isEqualTo(expectedFare);
    }

    @TestFactory
    @DisplayName("최단경로 조회의 예외케이스를 검증한다.")
    Stream<DynamicTest> findShortestPath_fail() {
        return Stream.of(
            dynamicTest("출발역과 도착역을 같은 역으로 조회할 경우", () -> 최단_경로_조회시_실패_검증(accessToken, 교대역, 교대역)),
            dynamicTest("출발역과 도착역이 연결이 되어 있지 않은 경우", () -> {
                StationResponse 연결되지_않는_역 = StationAcceptanceTest.지하철역_등록되어_있음("연결되지 않은 역")
                    .as(StationResponse.class);
                최단_경로_조회시_실패_검증(accessToken, 교대역, 연결되지_않는_역);
            }),
            dynamicTest("존재하지 않은 출발역이나 도착역을 조회 할 경우", () -> {
                StationResponse 존재하지_않는_역 = new StationResponse(-1L, "존재하지 않는 역", LocalDateTime.now(),
                    LocalDateTime.now());
                최단_경로_조회시_실패_검증(accessToken, 교대역, 존재하지_않는_역);
            })
        );
    }

    @Test
    @DisplayName("유요하지 않는 토큰으로 지하철역 사이의 최단 경로를 조회할 떄 에러를 던진다.")
    void findShortestPath_auth_fail() {
        ExtractableResponse<Response> response = 최단_경로를_조회함("Invalid Token", 교대역, 양재역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 최단_경로_조회시_실패_검증(String token, StationResponse sourceStation, StationResponse targetStation ) {
        assertThat(최단_경로를_조회함(token, sourceStation, targetStation).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단_경로를_조회함(String token, StationResponse sourceStation, StationResponse targetStation) {
        return get(token,"/paths?source={sourceStationId}&target={targetStationId}", sourceStation.getId(), targetStation.getId());
    }

}
