package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceFixture.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceFixture.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceFixture.비회원_지하철_최단_경로_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceFixture.회원_지하철_최단_경로_요청;
import static nextstep.subway.station.StationAcceptanceFixture.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 구로디지털단지역;


    /**
     * 교대역      --- *2호선(50_200)* ---   강남역
     * |                              |
     * *3호선(15_300)*                     *신분당선(50_1000)*
     * |                              |
     * 남부터미널역  --- *3호선(10_300)*  ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        구로디지털단지역 = 지하철역_등록되어_있음("구로디지털단지역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 50, 1000)).as(LineResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-yellow-420", 교대역.getId(), 강남역.getId(), 50, 200)).as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-green-500", 교대역.getId(), 양재역.getId(), 25, 300)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 15);
    }

    /**
     * Scenario: 비회원 최단 경로를 조회한다.
     * <p>
     * When: 교대역에서 양재역까지의 최단경로 조회
     * Then: 최단 경로 조회(교대역 - 3호선(15) - 남부터미널역 - 3호선(10) - 양재역)
     * And: 최단 경로는 25km
     * And: 요금은 (기본:1250, 노선요금:300, 거리(25):300) 은 1850원이다.
     */
    @DisplayName("비회원 최단 경로 조회한 경우")
    @Test
    void guest_short_path() {
        // when
        ExtractableResponse<Response> 지하철_경로_조회_응답 = 비회원_지하철_최단_경로_요청(교대역.getId(), 양재역.getId());

        // then
        assertAll(
                () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(25),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getFare()).isEqualTo(1850)
        );
    }

    /**
     * Scenario: 어린이회원 최단 경로를 조회한다.
     * <p>
     * Given: 어린이 회원 요청을 한다.
     * When: 회원 토큰이 포함된 강남역에서 남부터미널까지의 최단경로 조회
     * Then: 최단 경로 조회(강남역 - 신분당선(50_1000) - 양재역 - 3호선(10_300) - 남부터미널역)
     * And: 최단 경로는 60km
     * And: 요금은 (기본:1250, 노선요금:1000, 거리(60):800+200) 은 3250원에서
     * 어린이할인으로 (3250 - 350) * 0.5 = 1450원이다.
     */
    @DisplayName("어린이 회원 최단 경로 요청")
    @Test
    void kid_short_path_find() {

        // given
        String email = "kid@kid.com";
        String password = "password";
        int age = 10;
        회원_생성을_요청(email, password, age);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(email, password));
        String 로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 지하철_경로_조회_응답 = 회원_지하철_최단_경로_요청(로그인_성공_토큰_값, 강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getFare()).isEqualTo(1450)
        );

    }

    /**
     * Scenario: 청소년회원 최단 경로를 조회한다.
     * <p>
     * Given: 청소년 회원 요청을 한다.
     * When: 회원 토큰이 포함된 강남역에서 남부터미널까지의 최단경로 조회
     * Then: 최단 경로 조회(강남역 - 신분당선(50_1000) - 양재역 - 3호선(10_300) - 남부터미널역)
     * And: 최단 경로는 60km
     * And: 요금은 (기본:1250, 노선요금:1000, 거리(60):800+200) 은 3250원에서
     * 청소년할인으로 (3250 - 350) * 0.8 = 2320원이다.
     */
    @DisplayName("청소년 회원 최단 경로 요청")
    @Test
    void teenager_short_path_find() {

        // given
        String email = "teenager@teenager.com";
        String password = "password";
        int age = 15;
        회원_생성을_요청(email, password, age);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(email, password));
        String 로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 지하철_경로_조회_응답 = 회원_지하철_최단_경로_요청(로그인_성공_토큰_값, 강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getFare()).isEqualTo(2320)
        );

    }

    /**
     * Scenario: 일반회원 최단 경로를 조회한다.
     * <p>
     * Given: 일반 회원 요청을 한다.
     * When: 회원 토큰이 포함된 강남역에서 남부터미널까지의 최단경로 조회
     * Then: 최단 경로 조회(강남역 - 신분당선(50_1000) - 양재역 - 3호선(10_300) - 남부터미널역)
     * And: 최단 경로는 60km
     * And: 요금은 (기본:1250, 노선요금:1000, 거리(60):800+200) 은 3250원에서
     * 일반회원의 경우 할인 혜택이 없다.
     */
    @DisplayName("일반 회원 최단 경로 요청")
    @Test
    void basic_short_path_find() {

        // given
        String email = "basic@basic.com";
        String password = "password";
        int age = 20;
        회원_생성을_요청(email, password, age);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(email, password));
        String 로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 지하철_경로_조회_응답 = 회원_지하철_최단_경로_요청(로그인_성공_토큰_값, 강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getFare()).isEqualTo(3250)
        );

    }

    @DisplayName("출발역과 도착역이 같은 경우 경로 조회 불가")
    @Test
    void same_start_arrive_section() {
        // when
        ExtractableResponse<Response> 출발역과_도착역이_같은_지하철_최단_경로_응답 = 비회원_지하철_최단_경로_요청(교대역.getId(), 교대역.getId());

        // then
        assertThat(출발역과_도착역이_같은_지하철_최단_경로_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 불가")
    @Test
    void not_connected_station_line() {
        // when
        ExtractableResponse<Response> 연결되지_않는_지하철_최단_경로_응답 = 비회원_지하철_최단_경로_요청(강남역.getId(), 구로디지털단지역.getId());

        // then
        assertThat(연결되지_않는_지하철_최단_경로_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회하는 경우")
    @Test
    void not_exist_station() {
        // when
        ExtractableResponse<Response> 존재하지_않는_도착역_조회_응답 = 비회원_지하철_최단_경로_요청(강남역.getId(), 10L);

        // then
        assertThat(존재하지_않는_도착역_조회_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
