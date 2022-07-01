package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private TokenRequest tokenRequest;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private static StationResponse 강남역;
    private StationResponse 광교역;
    private static StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private ExtractableResponse<Response> loginResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        loginResponse = 로그인(new TokenRequest(EMAIL, PASSWORD));
        지하철_노선_생성();
    }

    @DisplayName("노선 즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인(new TokenRequest(EMAIL, PASSWORD));
        // then
        로그인_성공(loginResponse);

        // given
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
        FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 양재역.getId());
        // when
        ExtractableResponse<Response> saveFavoriteResponse = 즐겨찾기_추가(accessToken, favoriteRequest);
        // then
        즐겨찾기_추가_성공(saveFavoriteResponse);

        // when
        ExtractableResponse<Response> findFavoritesResponse = 즐겨찾기_전체_조회(accessToken);
        // then
        즐겨찾기_전체_조회(findFavoritesResponse);

        // when
        FavoriteResponse favoriteResponse = saveFavoriteResponse.as(FavoriteResponse.class);
        ExtractableResponse<Response> deleteFavoritesResponse = 즐겨찾기_삭제(accessToken, favoriteResponse.getId());
        // then
        즐겨찾기_삭제_성공(deleteFavoritesResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가(String accessToken, FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_전체_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/" + id)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_추가_성공(ExtractableResponse<Response> response) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(favoriteResponse.getId()).isNotNull(),
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(양재역.getName())
        );
    }

    public static void 즐겨찾기_전체_조회(ExtractableResponse<Response> response) {
        List<FavoriteResponse> favoriteResponseList = response.jsonPath().getList(".", FavoriteResponse.class).stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(favoriteResponseList).hasSize(1),
                () -> assertThat(favoriteResponseList.get(0).getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favoriteResponseList.get(0).getTarget().getName()).isEqualTo(양재역.getName())
        );
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public void 지하철_노선_생성() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);

        loginResponse = 로그인(new TokenRequest(EMAIL, PASSWORD));
    }
}
