package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String email = "email@email.com";
    private String password = "password";
    private StationResponse 상록수역;
    private StationResponse 사당역;
    private String token;

    @BeforeEach
    void setup() {
        super.setUp();

        상록수역 = 지하철역_등록되어_있음("상록수역").as(StationResponse.class);
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        회원_등록되어_있음(email, password, 20);
        token = 로그인_요청(email, password).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성_요청(token, 상록수역.getId(), 사당역.getId());
        // then
        즐겨찾기_생성됨(createFavoriteResponse);

        // when
        ExtractableResponse<Response> favoriteListResponse = 즐겨찾기_목록_조회(token);
        // then
        즐겨찾기_목록조회_응답됨(favoriteListResponse, Arrays.asList(상록수역, 사당역));

        // when
        ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제_요청(createFavoriteResponse, token);
        즐겨찾기_삭제됨(deleteFavoriteResponse);
    }

    @DisplayName("출발역과 도착역이 같으면 즐겨찾기 생성 시 에러가 발생한다")
    @Test
    void createFavoriteFail() {
        //given
        회원_등록되어_있음("test2@naver.com", password, 20);
        String newToken = 로그인_요청("test2@naver.com", password).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성_요청(token, 상록수역.getId(), 상록수역.getId());
        // then
        즐겨찾기_생성_실패(createFavoriteResponse);
    }

    @DisplayName("다른계정의 즐겨찾기를 삭제하면 에러가 발생한다")
    @Test
    void deleteFavoriteFail() {
        //given
        회원_등록되어_있음("test2@naver.com", password, 20);
        String newToken = 로그인_요청("test2@naver.com", password).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성_요청(token, 상록수역.getId(), 사당역.getId());
        // then
        즐겨찾기_생성됨(createFavoriteResponse);

        // when
        ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제_요청(createFavoriteResponse, newToken);
        즐겨찾기_삭제_실패됨(deleteFavoriteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String accessToken) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_목록조회_응답됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
