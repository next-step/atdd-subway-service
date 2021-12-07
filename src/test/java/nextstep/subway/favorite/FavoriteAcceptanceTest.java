package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorites.dto.FavoriteRequest;
import nextstep.subway.favorites.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인을_성공하면_토큰을_발급받는다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Feature: 즐겨찾기를 관리한다.
 * <p>
 * Background
 * Given 지하철역 등록되어 있음
 * And 지하철 노선 등록되어 있음
 * And 지하철 노선에 지하철역 등록되어 있음
 * And 회원 등록되어 있음
 * And 로그인 되어있음
 * <p>
 * Scenario: 즐겨찾기를 관리
 * When 즐겨찾기 생성을 요청
 * Then 즐겨찾기 생성됨
 * When 즐겨찾기 목록 조회 요청
 * Then 즐겨찾기 목록 조회됨
 * When 즐겨찾기 삭제 요청
 * Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String BASE_URI = "/favorites";
    private String 사용자;
    private Long 출발지_ID;
    private Long 목적지_ID;
    private Long 연결되지_않은_목적지_ID;
    private Long 존재하지_않는_역_ID = Long.MAX_VALUE;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        LineResponse 일호선 = 일호선_등록되어_있음();
        LineResponse 이호선 = 이호선_등록되어_있음();
        출발지_ID = 일호선.getStations().get(0).getId();
        목적지_ID = 일호선.getStations().get(1).getId();
        연결되지_않은_목적지_ID = 이호선.getStations().get(1).getId();
        사용자 = 로그인을_성공하면_토큰을_발급받는다(로그인_요청함(회원_등록되어_있음(EMAIL, PASSWORD, AGE)));
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorites() {
        // when
        ExtractableResponse<Response> 즐겨찾기_추가_응답 = 즐겨찾기_추가함(출발지_ID, 목적지_ID, 사용자);

        // then
        즐겨찾기_추가됨(즐겨찾기_추가_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회함(사용자);

        // then
        즐겨찾기_조회됨(즐겨찾기_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청함(사용자, 즐겨찾기_조회_응답);

        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("즐겨찾기 생성 예외 처리")
    @Test
    public void handlingAddFavorite() {
        // when
        ExtractableResponse<Response> 존재하지_않은_역_즐겨찾기_등록 = 즐겨찾기_추가함(출발지_ID, 존재하지_않는_역_ID, 사용자);

        // then
        즐겨찾기_요청_실패함(존재하지_않은_역_즐겨찾기_등록);

        // when
        ExtractableResponse<Response> 연결되지_않은_역_즐겨찾기_등록 = 즐겨찾기_추가함(출발지_ID, 연결되지_않은_목적지_ID, 사용자);

        // then
        즐겨찾기_요청_실패함(연결되지_않은_역_즐겨찾기_등록);
    }

    @DisplayName("즐겨찾기 삭제 예외 처리")
    @Test
    public void handlingDelFavorite() {
        ExtractableResponse<Response> 존재하지_않은_즐겨찾기_삭제 = 즐겨찾기_삭제_요청함(사용자, 존재하지_않는_역_ID);

        즐겨찾기_요청_실패함(존재하지_않은_즐겨찾기_삭제);
    }

    private void 즐겨찾기_요청_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청함(String token, ExtractableResponse<Response> response) {
        Long deleteId = response.jsonPath()
                .getList(".", FavoriteResponse.class)
                .get(0)
                .getId();

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(BASE_URI + "/{id}", deleteId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청함(String token, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(BASE_URI + "/{id}", id)
                .then().log().all().extract();
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response2) {
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorites = response2.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites.size()).isGreaterThanOrEqualTo(0);
    }

    private ExtractableResponse<Response> 즐겨찾기_조회함(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(BASE_URI)
                .then().log().all().extract();
    }

    private void 즐겨찾기_추가됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_추가함(Long source, Long target, String token) {
        FavoriteRequest request = FavoriteRequest.of(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URI)
                .then().log().all().extract();
    }

    private LineResponse 일호선_등록되어_있음() {
        StationResponse 인천 = 지하철역_등록되어_있음("인천").as(StationResponse.class);
        StationResponse 소요산 = 지하철역_등록되어_있음("소요산").as(StationResponse.class);

        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(
                new LineRequest("일호선", "남색", 인천.getId(), 소요산.getId(), 100));

        return response.jsonPath().getObject("", LineResponse.class);
    }

    private LineResponse 이호선_등록되어_있음() {
        StationResponse 건대입구 = 지하철역_등록되어_있음("건대입구").as(StationResponse.class);
        StationResponse 왕십리 = 지하철역_등록되어_있음("왕십리").as(StationResponse.class);

        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "녹색", 건대입구.getId(), 왕십리.getId(), 100));

        return response.jsonPath().getObject("", LineResponse.class);
    }

}