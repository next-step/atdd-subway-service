package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberRequest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // Given 지하철역 등록되어 있음
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // And 지하철 노선에 지하철역 등록되어 있음
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 광교역, 5);

        // And 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // And 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String token = loginResponse.as(TokenResponse.class).getAccessToken();

        // When 즐겨찾기 생성 요청
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성을_요청(token, 양재역.getId(), 광교역.getId());
        // THen 즐겨찾기 생성됨
        assertEquals(HttpStatus.CREATED.value(), createFavoriteResponse.statusCode());

        // When 즐겨찾기 목록 조회
        ExtractableResponse<Response> listFavoriteResponse = 즐겨찾기_목록_조회_요청(token);
        // Then 즐겨찾기 목록 조회됨
        assertEquals(HttpStatus.OK.value(), listFavoriteResponse.statusCode());
        assertThat(listFavoriteResponse.as(List.class)).hasSize(1);

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제_요청(token, createFavoriteResponse);
        // Then 즐겨찾기 삭제됨
        assertEquals(HttpStatus.OK.value(), deleteFavoriteResponse.statusCode());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> createResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete(createResponse.header("Location"))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
