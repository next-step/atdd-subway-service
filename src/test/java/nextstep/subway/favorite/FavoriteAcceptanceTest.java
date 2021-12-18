package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    LineResponse 삼호선;
    LineResponse 이호선;
    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    String favoriteUrl = "/favorites";
    String email = "email@email.com";
    String password = "password";
    int age = 20;
    String token;

    @BeforeEach
    void setup() {
        // Given 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        // And 지하철 노선 등록되어 있음
        이호선 = PathAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = PathAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        // And 지하철 노선에 지하철역 등록되어 있음
        PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        // And 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        // And 로그인 되어있음
        token = 로그인_요청(email, password).body().as(TokenResponse.class).getAccessToken();
    }
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성_요청(강남역.getId(), 남부터미널역.getId());
        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createFavoriteResponse);
        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> foundFavoriteResponse = 즐겨찾기_목록_조회_요청();
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(foundFavoriteResponse, 강남역, 남부터미널역);
        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createFavoriteResponse);
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetid) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(new FavoriteRequest(sourceId, targetid))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(favoriteUrl)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(favoriteUrl)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createFavoriteResponse) {
        String url = createFavoriteResponse.header("location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(url)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createFavoriteResponse) {
        Assertions.assertThat(createFavoriteResponse.statusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> foundFavoriteRespons, StationResponse source, StationResponse target) {
        FavoritesResponse favoritesResponse = foundFavoriteRespons.body().as(FavoritesResponse.class);
        assertAll(
                () -> assertThat(foundFavoriteRespons.statusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(favoritesResponse.getFavoriteResponses().get(0).getSource()).isEqualTo(source),
                () -> assertThat(favoritesResponse.getFavoriteResponses().get(0).getTarget()).isEqualTo(target)
        );
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}