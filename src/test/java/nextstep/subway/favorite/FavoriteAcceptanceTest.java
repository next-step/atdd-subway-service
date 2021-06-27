package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    TokenResponse 사용자;

    StationResponse 강남역;
    StationResponse 광교역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "레드", 강남역.getId(), 광교역.getId(), 10));

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 회원_로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기를 관리 한다")
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse
            = 즐겨찾기_생성을_요청(사용자, 강남역, 광교역);

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> queryResponse
            = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_목록_조회됨(queryResponse, 강남역, 광교역);

        // when
        ExtractableResponse<Response> deleteResponse
            = 즐겨찾기_삭제_요청(createResponse, 사용자);

        // then
        즐겨찾기_삭제됨(deleteResponse);

        // when
        ExtractableResponse<Response> emptyResponse
            = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_빈_목록_조회됨(emptyResponse);
    }

    private static void 즐겨찾기_빈_목록_조회됨(ExtractableResponse<Response> emptyResponse) {
        assertThat(emptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(emptyResponse.as(List.class).isEmpty()).isTrue();
    }

    private static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
                ExtractableResponse<Response> createResponse, TokenResponse tokenResponse) {
        String id = createResponse.header("Location").split("/")[2];
        ExtractableResponse<Response> deleteResponse = RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/favorites/{id}", id)
            .then().log().all().extract();
        return deleteResponse;
    }

    private static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> queryResponse, StationResponse source, StationResponse target) {
        assertThat(queryResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorite
            = queryResponse.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorite.get(0).getId()).isNotNull();
        assertThat(favorite.get(0).getSource()).isEqualTo(source);
        assertThat(favorite.get(0).getTarget()).isEqualTo(target);
    }

    private static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성을_요청(
            TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());
        ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all().extract();
        return createResponse;
    }
}
