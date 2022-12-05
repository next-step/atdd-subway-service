package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceSupport.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.path.PathAcceptanceSupport.로그인_최단_경로를_조회한다;
import static nextstep.subway.path.PathAcceptanceSupport.조회_실패됨;
import static nextstep.subway.path.PathAcceptanceSupport.지하철_노선_등록되어_있음;
import static nextstep.subway.path.PathAcceptanceSupport.비로그인_최단_경로를_조회한다;
import static nextstep.subway.station.StationAcceptanceSupport.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 길음역;
    private final Long 존재하지_않는_역_ID = 100L;
    private final String ADULT_EMAIL = "adult@email.com";
    private final String TEENAGER_EMAIL = "teenager@email.com";
    private final String KIDS_EMAIL = "kids@email.com";
    private final String PASSWORD = "password";
    private final int ADULT_AGE = 19;
    private final int TEENAGER_AGE = 13;
    private final int KIDS_AGE = 6;
    private String 어린이_토큰;
    private String 청소년_토큰;
    private String 성인_토큰;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        길음역 = 지하철역_등록되어_있음("길음역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                "신분당선", "bg-red-600", 강남역, 양재역, 15, 500);
        이호선 = 지하철_노선_등록되어_있음(
                "이호선", "bg-red-600", 교대역, 강남역, 10, 900);
        삼호선 = 지하철_노선_등록되어_있음(
                "삼호선", "bg-red-600", 교대역, 양재역, 20, 300);
        사호선 = 지하철_노선_등록되어_있음(
                "사호선", "bg-red-600", 길음역, 사당역, 10, 0);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        회원_생성을_요청(KIDS_EMAIL, PASSWORD, KIDS_AGE);
        어린이_토큰 = 로그인_요청(KIDS_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        회원_생성을_요청(TEENAGER_EMAIL, PASSWORD, TEENAGER_AGE);
        청소년_토큰 = 로그인_요청(TEENAGER_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        회원_생성을_요청(ADULT_EMAIL, PASSWORD, ADULT_AGE);
        성인_토큰 = 로그인_요청(ADULT_EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("비회원, 최단경로를 조회한다")
    @Test
    void 비로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 비로그인_최단_경로를_조회한다(길음역.getId(), 사당역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
            () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("사당역", "길음역")),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
            () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("성인 회원, 최단경로를 조회한다")
    @Test
    void 성인_회원_로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 로그인_최단_경로를_조회한다(성인_토큰, 길음역.getId(), 사당역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("사당역", "길음역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("청소년 회원, 최단경로를 조회한다")
    @Test
    void 청소년_회원_로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 로그인_최단_경로를_조회한다(청소년_토큰, 길음역.getId(), 사당역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("사당역", "길음역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(720)
        );
    }

    @DisplayName("비회원, 노선 추가 요금, 최단경로를 조회한다")
    @Test
    void 비로그인_노선추가요금_최단경로를_조회한다() {
        PathResponse pathResponse = 비로그인_최단_경로를_조회한다(강남역.getId(), 남부터미널역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "교대역", "남부터미널역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2550)
        );
    }

    @DisplayName("성인 회원, 노선 추가 요금, 최단경로를 조회한다")
    @Test
    void 성인_로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 로그인_최단_경로를_조회한다(성인_토큰, 강남역.getId(), 남부터미널역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "교대역", "남부터미널역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2550)
        );
    }

    @DisplayName("청소년 회원, 노선 추가 요금, 최단경로를 조회한다")
    @Test
    void 청소년_로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 로그인_최단_경로를_조회한다(청소년_토큰, 강남역.getId(), 남부터미널역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "교대역", "남부터미널역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2020)
        );
    }

    @DisplayName("어린이 회원, 노선 추가 요금, 최단경로를 조회한다")
    @Test
    void 어린이_로그인_최단경로를_조회한다() {
        PathResponse pathResponse = 로그인_최단_경로를_조회한다(어린이_토큰, 강남역.getId(), 남부터미널역.getId()).as(PathResponse.class);

        List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "교대역", "남부터미널역")),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1750)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void 출발역과_도착역이_같은_경우() {
        ExtractableResponse<Response> response = 비로그인_최단_경로를_조회한다(강남역.getId(), 강남역.getId());
        조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        ExtractableResponse<Response> response = 비로그인_최단_경로를_조회한다(강남역.getId(), 길음역.getId());
        조회_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역을 조회 할 경우")
    @Test
    void 존재하지_않은_출발역을_조회_할_경우() {
        ExtractableResponse<Response> response = 비로그인_최단_경로를_조회한다(존재하지_않는_역_ID, 강남역.getId());
        조회_실패됨(response);
    }

    @DisplayName("존재하지 않은 도착역을 조회 할 경우")
    @Test
    void 존재하지_않은_도착역을_조회_할_경우() {
        ExtractableResponse<Response> response = 비로그인_최단_경로를_조회한다(강남역.getId(), 존재하지_않는_역_ID);
        조회_실패됨(response);
    }
}
