package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;
    private Long 강남역;
    private Long 양재역;
    private Long 정자역;
    private Long 광교역;

    private String accessToken;

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    void setup() {
        super.setUp();

        // given
        강남역 = 지하철역_ID_추출(지하철역_등록되어_있음("강남역"));
        양재역 = 지하철역_ID_추출(지하철역_등록되어_있음("양재역"));
        정자역 = 지하철역_ID_추출(지하철역_등록되어_있음("정자역"));
        광교역 = 지하철역_ID_추출(지하철역_등록되어_있음("광교역"));

        Map<String, String> lineRequest = LineAcceptanceTest.지하철_노선_생성_요청_파라미터("신분당선", "bg-red-600", 강남역,
            광교역, 10);
        신분당선 = 지하철_노선_ID_추출(지하철_노선_등록되어_있음(lineRequest));

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰값_추출(토큰_발급_요청(EMAIL, PASSWORD));
    }

    /**
     * When 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 성공
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회 성공
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제 성공
     */
    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);
        // then
        즐겨찾기_생성_성공(즐겨찾기_생성_응답);
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회_성공(즐겨찾기_목록_조회_응답, "강남역", "양재역");
        // when
        long 즐겨찾기_ID = 목록_첫번째_즐겨찾기_ID_추출(즐겨찾기_목록_조회_응답);
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기_ID);
        // then
        즐겨찾기_삭제_성공(즐겨찾기_삭제_응답);
    }

    /**
     * When 존재하지 않는 시작역 ID로 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 실패
     * When 존재하지 않는 도착역 ID로 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 실패
     * When 존재하지 않는 즐겨찾기 ID로 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제 실패
     */
    @DisplayName("즐겨찾기 관리 실패")
    @Test
    void manageFavorite_failed() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답1 = 즐겨찾기_생성_요청(accessToken, -1L, 양재역);
        // then
        즐겨찾기_생성_실패(즐겨찾기_생성_응답1);
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답2 = 즐겨찾기_생성_요청(accessToken, 강남역, -1L);
        // then
        즐겨찾기_생성_실패(즐겨찾기_생성_응답2);
        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(accessToken, -1L);
        // then
        즐겨찾기_삭제_실패(즐겨찾기_삭제_응답);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록_조회_성공(ExtractableResponse<Response> response, String expectedSource,
        String expectedTarget) {
        List<FavoriteResponse> favorites = Arrays.asList(response.as(FavoriteResponse[].class));
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(favorites).hasSize(1),
            () -> assertThat(favorites.get(0).getSource().getName()).isEqualTo(expectedSource),
            () -> assertThat(favorites.get(0).getTarget().getName()).isEqualTo(expectedTarget)
        );
    }

    private long 목록_첫번째_즐겨찾기_ID_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class).get(0);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, long id) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/" + id)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
