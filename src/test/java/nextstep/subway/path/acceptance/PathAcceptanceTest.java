package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 용산역;
    private StationResponse 동작역;
    private StationResponse 이촌역;
    private StationResponse 판교역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                          |
     *                          *신분당선*
     *                          |
     *                          판교역
     *
     * 동작역   --- *4호선*   ---  이촌
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        용산역 = StationAcceptanceTest.지하철역_등록되어_있음("용산역").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);
        이촌역 = StationAcceptanceTest.지하철역_등록되어_있음("이촌역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);

        LineRequest 신분당선_등록_요청_데이터 = new LineRequest("신분당선", "bg-red-600", 800, 강남역.getId(), 양재역.getId(), 10);
        LineRequest 이호선_등록_요청_데이터 = new LineRequest("이호선", "bg-red-600", 0, 교대역.getId(), 강남역.getId(), 10);
        LineRequest 삼호선_등록_요청_데이터 = new LineRequest("삼호선", "bg-red-600", 0, 교대역.getId(), 남부터미널역.getId(), 5);
        LineRequest 사호선_등록_요청_데이터 = new LineRequest("사호선", "bg-red-600", 0, 동작역.getId(), 이촌역.getId(), 5);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음(신분당선_등록_요청_데이터).as(LineResponse.class);
        LineResponse 이호선 = 지하철_노선_등록되어_있음(이호선_등록_요청_데이터).as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(삼호선_등록_요청_데이터).as(LineResponse.class);
        LineResponse 사호선 = 지하철_노선_등록되어_있음(사호선_등록_요청_데이터).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 10);
    }

    @DisplayName("최단 경로 조회 게스트")
    @Test
    void 최단_경로_조회_게스트() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(HttpStatus.OK.value()),
                () -> 최단_경로_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역)),
                () -> 최단_경로_거리_확인(response, 7),
                () -> 최단_경로_요금_확인(response, 1250)
        );
    }

    @DisplayName("최단 경로 조회 - 어린이")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(ints = {6, 8, 12})
    void 최단_경로_조회_어린이(int age) {
        // given
        TokenResponse tokenResponse = 사용자_생성_후_로그인(age);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_경로_조회_요청(tokenResponse, 교대역, 판교역);

        // then
        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(HttpStatus.OK.value()),
                () -> 최단_경로_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역, 판교역)),
                () -> 최단_경로_거리_확인(response, 17),
                () -> 최단_경로_요금_확인(response, 950)
        );
    }

    @DisplayName("최단 경로 조회 - 청소년")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(ints = {13, 15, 18})
    void 최단_경로_조회_청소년(int age) {
        // given
        TokenResponse tokenResponse = 사용자_생성_후_로그인(age);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_경로_조회_요청(tokenResponse, 교대역, 판교역);

        // then
        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(HttpStatus.OK.value()),
                () -> 최단_경로_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역, 판교역)),
                () -> 최단_경로_거리_확인(response, 17),
                () -> 최단_경로_요금_확인(response, 1520)
        );
    }

    @DisplayName("최단 경로 조회 - 성인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(ints = {19, 25, 50})
    void 최단_경로_조회_성인(int age) {
        // given
        TokenResponse tokenResponse = 사용자_생성_후_로그인(age);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_경로_조회_요청(tokenResponse, 교대역, 판교역);

        // then
        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(HttpStatus.OK.value()),
                () -> 최단_경로_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역, 판교역)),
                () -> 최단_경로_거리_확인(response, 17),
                () -> 최단_경로_요금_확인(response, 2250)
        );
    }

    @DisplayName("실패 테스트")
    @Nested
    class FailTest {
        @DisplayName("출발역과 도착역이 같음")
        @Test
        void 출발역과_도착역이_같음() {
            // when
            ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 강남역);

            // then
            assertThat(response.statusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("출발역이 전체 구간에 포함되지 않음")
        @Test
        void 출발역이_전체_구간에_포함되지_않음() {
            // when
            ExtractableResponse<Response> response = 지하철_경로_조회_요청(용산역, 강남역);

            // then
            assertThat(response.statusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("도착역이 전체 구간에 포함되지 않음")
        @Test
        void 도착역이_전체_구간에_포함되지_않음() {
            // when
            ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 용산역);

            // then
            assertThat(response.statusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 출발역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_출발역_기준() {
            // when
            ExtractableResponse<Response> response = 지하철_경로_조회_요청(동작역, 강남역);

            // then
            assertThat(response.statusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 도착역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_도착역_기준() {
            // when
            ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 이촌역);

            // then
            assertThat(response.statusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_지하철_경로_조회_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then()
                .log()
                .all()
                .extract();
    }

    private TokenResponse 사용자_생성_후_로그인(int age) {
        String email = "email@test.com";
        String password = "password";

        MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        return AuthAcceptanceTest.로그인_요청(email, password).as(TokenResponse.class);
    }

    public static void 최단_경로_거리_확인(ExtractableResponse<Response> response, int expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getDistance())
                .isEqualTo(expectedDistance);
    }

    public static void 최단_경로_확인(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds)
                .containsExactlyElementsOf(expectedStationIds);
    }

    public static void 최단_경로_요금_확인(ExtractableResponse<Response> response, int expectedPrice) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getPrice())
                .isEqualTo(expectedPrice);
    }
}
