package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    // background
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 사용자이메일;
    private String 암호;
    private Integer 나이;
    private String token;

    /**
     * 교대역    --- *2호선*(10m) ---   강남역
     * |                               |
     * *3호선(3m)*                      *신분당선*(10m)
     * |                               |
     * 남부터미널역  --- *3호선*(7m) ---   양재
     */
    @BeforeEach
    void setup() {
        super.setUp();

        사용자이메일 = "test@nextstep.com";
        암호 = "password";
        나이 = 32;

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음(사용자이메일, 암호, 나이);
        token = 로그인_됨(사용자이메일, 암호);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavoritesTest() {
        StationResponse source = 강남역;
        StationResponse target = 남부터미널역;

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_추가_요청(token, source, target);
        // then
        즐겨찾기_추가_요청_성공(createResponse);

        // when
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(token);
        // then
        즐겨찾기_목록_조회_성공(getResponse, source, target);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회_성공(
            ExtractableResponse<Response> response, StationResponse source, StationResponse target
    ) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorites = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getSource().getName()).isEqualTo(source.getName());
        assertThat(favorites.get(0).getTarget().getName()).isEqualTo(target.getName());
    }

    public static void 즐겨찾기_추가_요청_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(
            String token, StationResponse source, StationResponse target
    ) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source.getId(), target.getId()))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
