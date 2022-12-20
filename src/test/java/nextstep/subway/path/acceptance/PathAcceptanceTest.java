package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
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

import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 서울역;
    private String 회원토큰;
    
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
        서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);

        LineRequest 신분당선_등록 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900);
        LineRequest 이호선_등록 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 200);
        LineRequest 삼호선_등록 = new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 300);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_등록).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_등록).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_등록).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        ExtractableResponse<Response> response = MemberAcceptanceTest.회원_생성을_요청("youth@email.com", "password", 16);
        회원토큰 = AuthAcceptanceTest.토큰_발급(new TokenRequest("youth@email.com", "password")).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void 최단_경로_조회() {
        ExtractableResponse<Response> response = 게스트_최단_경로_찾기(강남역, 남부터미널역);
        PathResponse pathResponse = response.as(PathResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations()
                        .stream().map(it -> it.getName())
                        .collect(Collectors.toList())).containsExactly("강남역", "양재역", "남부터미널역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2250)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우의 최단 경로를 조회한다.")
    @Test
    void 동일역_간_최단_거리_찾기_테스트() {
        ExtractableResponse<Response> response = 게스트_최단_경로_찾기(강남역, 강남역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우의 최단 경로를 조회한다.")
    @Test
    void 연결되지_않은_역의_최단_거리_찾기_테스트() {
        ExtractableResponse<Response> response = 게스트_최단_경로_찾기(강남역, 서울역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("등록되지 않은 역으로 최단 경로를 조회한다.")
    @Test
    void 등록되지_않은_역_최단_거리_찾기_테스트() {
        ExtractableResponse<Response> response = 게스트_최단_경로_찾기(강남역, new StationResponse(100L, "간이역", null, null));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비회원의 경로 요금을 조회한다.")
    @Test
    void 비회원_경로_요금_조회_테스트() {
        ExtractableResponse<Response> response = 게스트_최단_경로_찾기(강남역, 남부터미널역);
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(2250);
    }

    @DisplayName("일반회원의 경로 요금을 조회한다.")
    @Test
    void 회원_경로_요금_조회_테스트() {
        ExtractableResponse<Response> response = 로그인_회원_최단_경로_찾기(회원토큰, 강남역, 남부터미널역);
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(1520);
    }

    public static ExtractableResponse<Response> 로그인_회원_최단_경로_찾기(String token, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 게스트_최단_경로_찾기(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
                .then().log().all()
                .extract();
    }
}
