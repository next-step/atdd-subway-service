package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
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

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역; //1
    private StationResponse 양재역; //2
    private StationResponse 교대역; //3
    private StationResponse 남부터미널역; //4

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

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

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("3호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철 경로를 조회한다.")
    void getPath() {
        // when
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceTest.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> authResponse = AuthAcceptanceTest.토큰_발급_요청(EMAIL, PASSWORD, AGE);
        // then
        assertThat(authResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse token = authResponse.jsonPath().getObject(".", TokenResponse.class);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getObject(".", PathResponse.class)
                .getStations().stream()
                .map(station -> station.getId())
                .collect(Collectors.toList()))
                .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
        assertThat(response.jsonPath().getInt("fare")).isNotNull();
    }


    @Test
    @DisplayName("로그인하지 않은 사용자가 지하철 경로를 조회한다.")
    void getPathOfAnonymous() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getObject(".", PathResponse.class)
                .getStations().stream()
                .map(station -> station.getId())
                .collect(Collectors.toList()))
                .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
        assertThat(response.jsonPath().getInt("fare")).isNotNull();
    }
}
