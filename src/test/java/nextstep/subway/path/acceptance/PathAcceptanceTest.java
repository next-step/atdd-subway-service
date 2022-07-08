package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

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
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로 조회 기능")
    @Test
    void getShortestPathTest() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId());

        // then
        최단_경로_길이_비교(response, 10);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    @Test
    void 예외_출발역과_도착역이_같음() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 강남역.getId());

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    @Test
    void 예외_출발역과_도착역_미연결() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 광교역.getId());

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하는 경우 예외 발생")
    @Test
    void 예외_존재하지_않는_출발역_또는_도착역() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(Long.MAX_VALUE-1, Long.MAX_VALUE-2);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("비회원인 경우 성인 요금 조회")
    @Test
    void 비회원_성인_요금_조회() {
        // given
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId());

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("성인 요금 조회")
    @Test
    void 성인_요금_조회() {
        // given
        회원_생성을_요청("adult", "adult", 25);
        ExtractableResponse<Response> loginResponse = 로그인(new TokenRequest("adult", "adult"));
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId(), accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("청소년 요금 조회")
    @Test
    void 청소년_요금_조회() {
        // given
        회원_생성을_요청("teenager", "teenager", 15);
        ExtractableResponse<Response> loginResponse = 로그인(new TokenRequest("teenager", "teenager"));
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId(), accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(720);
    }

    @DisplayName("어린이 요금 조회")
    @Test
    void 어린이_요금_조회() {
        // given
        회원_생성을_요청("child", "child", 8);
        ExtractableResponse<Response> loginResponse = 로그인(new TokenRequest("child", "child"));
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId(), accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(450);
    }

    public ExtractableResponse<Response> 최단_경로_조회(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", source)
                .param("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 최단_경로_조회(Long source, Long target, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", source)
                .param("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public void 최단_경로_길이_비교(ExtractableResponse<Response> response, int distance) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
    }

    public void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }
}
