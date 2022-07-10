package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {


    private StationResponse 신도림역;
    private StationResponse 영등포역;
    private StationResponse 신길역;
    private StationResponse 대방역;
    private LineResponse 일호선;

    private String 로그인_토큰;

    //given : Background
    @BeforeEach
    public void setUp() {
        super.setUp();
        // 지하철역
        신도림역 = StationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        영등포역 = StationAcceptanceTest.지하철역_등록되어_있음("영등포역").as(StationResponse.class);
        신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
        대방역 = StationAcceptanceTest.지하철역_등록되어_있음("대방역").as(StationResponse.class);

        //노선
        일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-blue-600", 신도림역.getId(), 영등포역.getId(), 5, 0)).as(LineResponse.class);

        //노선에 지하철 추가
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 영등포역, 신길역,10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 신길역, 대방역,5);

        //회원
        AuthAcceptanceTest.회원_등록되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, 20);

        //로그인
        로그인_토큰 = AuthAcceptanceTest.로그인_토큰_요청(new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD)).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(로그인_토큰, new FavoriteRequest(신도림역.getId(), 신길역.getId()));
        //then
        즐겨찾기가_생성됨(createResponse);

        //when
        ExtractableResponse<Response> retrieveFavorites = 즐겨찾기_목록_조회_요청(로그인_토큰);
        //then
        즐겨찾기_목록_조회됨(retrieveFavorites, Arrays.asList(신도림역.getId(), 신길역.getId()));

        //when
        ExtractableResponse<Response> responseFavorite = 즐겨찾기_조회_요청(createResponse);
        FavoriteResponse favorite = responseFavorite.as(FavoriteResponse.class);
        //then
        즐겨찾기가_조회됨(responseFavorite);

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(로그인_토큰, favorite.getId());
        //then
        즐겨찾기가_삭제됨(deleteResponse);

        //when
        ExtractableResponse<Response> searchDeleteFavorite = 즐겨찾기_조회_요청(favorite.getId());
        //then
        즐겨찾기가_조회가_되지않음(searchDeleteFavorite);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, FavoriteRequest favoriteRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> retrieveFavorites, List<Long> favoriteTargets) {
        assertThat(retrieveFavorites.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrieveFavorites.jsonPath().getList("target.id", Long.class)).containsAnyElementsOf(favoriteTargets);
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header(HttpHeaders.LOCATION))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(long favoriteId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("favoriteId", favoriteId)
                .when().get("/favorites/{favoriteId}")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기가_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("source.name")).isNotEmpty();
    }

    private void 즐겨찾기가_조회가_되지않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, long favoriteId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .pathParam("favoriteId", favoriteId)
                .when().delete("/favorites/{favoriteId}")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기가_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}