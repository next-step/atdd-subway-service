package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철_구간_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static TokenResponse tokenResponse;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        // Feature: 즐겨찾기를 관리한다.
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);

        // And 지하철 노선에 지하철역(구간) 등록되어 있음
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(신분당선, 양재역, 정자역, 5);

        // And 회원 등록되어 있음
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(MY_EMAIL, MY_PASSWORD, MY_AGE);
        회원_생성됨(createResponse);

        // And 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 로그인_요청(MY_EMAIL, MY_PASSWORD);
        tokenResponse = loginResponse.as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리 한다.")
    @Test
    void performScenario() {
        // Scenario: 즐겨찾기를 관리
        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(강남역.getId(), 정자역.getId());

        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> favoritesResponse = 즐겨찾기_목록_조회_요청();

        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회_확인(favoritesResponse);

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse);

        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    private void 즐겨찾기_목록_조회_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        FavoriteResponse favoriteResponse = Arrays.stream(response.as(FavoriteResponse[].class))
            .collect(Collectors.toList())
            .get(0);

        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSourceStation().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponse.getTargetStation().getName()).isEqualTo(정자역.getName());
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(FavoriteRequest.of(source, target))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(uri)
            .then().log().all().extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
