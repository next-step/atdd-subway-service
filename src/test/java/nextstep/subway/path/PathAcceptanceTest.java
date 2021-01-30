package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 성수역;
    private StationResponse 홍대입구역;
    private String accessToken;

    private static final int 기본요금 = 1250;
    private static final int 신분당선_요금 = 1000;
    private static final int 삼호선_요금 = 500;
    private static final int 이호선_요금 = 0;

    private static final int 요금할인_공제액 = 350;
    private static final double 할인율_청소년 = 0.2;
    private static final double 할인율_어린이 = 0.5;

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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        성수역 = StationAcceptanceTest.지하철역_등록되어_있음("성수역").as(StationResponse.class);
        홍대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("홍대입구역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 신분당선_요금)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 이호선_요금)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 5, 삼호선_요금)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 성수역, 30);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 성수역, 홍대입구역, 20);
    }

    @DisplayName("지하철 요금")
    @Test
    void fare() {
        // given when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        // then
        API_요청_확인(response, HttpStatus.OK);
        지하철_최단경로_조회_확인(response, Arrays.asList("교대역", "남부터미널역", "양재역"), 8, 기본요금 + 삼호선_요금);
    }

    @DisplayName("10키로 초과 시 추가운임")
    @Test
    void fare2() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 성수역);

        // then
        API_요청_확인(response, HttpStatus.OK);
        지하철_최단경로_조회_확인(response, Arrays.asList("교대역", "강남역", "성수역"), 40, 기본요금 + 600);
    }

    @DisplayName("50키로 초과 시 추가운임")
    @Test
    void fare3() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 홍대입구역);

        // then
        API_요청_확인(response, HttpStatus.OK);
        지하철_최단경로_조회_확인(response, Arrays.asList("교대역", "강남역", "성수역", "홍대입구역"), 60, 기본요금 + 1000);
    }

    @DisplayName("어린이 할인")
    @Test
    void fare4() {
        MemberAcceptanceTest.회원_생성을_요청("justhis@gmail.com", "1234", 7);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(new TokenRequest("justhis@gmail.com", "1234"));
        accessToken = MemberAcceptanceTest.getAccessToken(loginResponse);
        int 요금 = 2350;
        int 할인액 = (int) ((요금 - 요금할인_공제액) * 할인율_어린이);

        ExtractableResponse<Response> response = 회원_최단_경로_조회_요청(accessToken, 강남역, 남부터미널역);
        지하철_최단경로_조회_확인(response, Arrays.asList("강남역", "양재역", "남부터미널역"), 13, 요금 - 요금할인_공제액 - 할인액);
    }

    @DisplayName("청소년 할인")
    @Test
    void fare5() {
        MemberAcceptanceTest.회원_생성을_요청("justhis@gmail.com", "1234", 15);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(new TokenRequest("justhis@gmail.com", "1234"));
        accessToken = MemberAcceptanceTest.getAccessToken(loginResponse);
        int 요금 = 2350;
        int 할인액 = (int) ((요금 - 요금할인_공제액) * 할인율_청소년);

        ExtractableResponse<Response> response = 회원_최단_경로_조회_요청(accessToken, 강남역, 남부터미널역);
        지하철_최단경로_조회_확인(response, Arrays.asList("강남역", "양재역", "남부터미널역"), 13, 요금 - 요금할인_공제액 - 할인액);
    }

    @DisplayName("노선 연결이 되어있지 않으면 실패")
    @Test
    void selectPathNotConnected() {
        // given
        StationResponse 의정부역 = StationAcceptanceTest.지하철역_등록되어_있음("의정부역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(의정부역, 남부터미널역);

        // then
        API_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("존재하지 않는 지하철역 조회 시 실패")
    @Test
    void selectNotFoundStation() {
        StationResponse 의정부역 = new StationResponse(100L, "의정부역", LocalDateTime.now(), LocalDateTime.now());

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(의정부역, 남부터미널역);

        // then
        API_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse startStation, StationResponse arrivalStation) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", startStation.getId())
                .queryParam("target", arrivalStation.getId())
                .get("/paths")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원_최단_경로_조회_요청(String accessToken, StationResponse startStation, StationResponse arrivalStation) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .auth().oauth2(accessToken)
                .queryParam("source", startStation.getId())
                .queryParam("target", arrivalStation.getId())
                .get("/paths")
                .then().log().all()
                .extract();
    }

    private void 지하철_최단경로_조회_확인(ExtractableResponse<Response> response, List<String> expectedStations, int distance, int fare) {
        List<StationResponse> stationResponses = response.body().as(PathResponse.class).getStations();
        PathResponse pathResponse = response.body().as(PathResponse.class);
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .asList()
                .containsExactlyElementsOf(expectedStations);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private void API_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }
}
