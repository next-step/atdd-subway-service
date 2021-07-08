package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.path.PathAcceptanceTest.*;
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
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private LineResponse 이호선;
    private StationResponse 강남역, 역삼역, 삼성역;
    private TokenResponse loginToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        삼성역 = 지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 강남역.getId(), 삼성역.getId(), 10))
            .as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(이호선, 역삼역, 삼성역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        loginToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getObject(".", TokenResponse.class);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 삼성역.getId());
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(favoriteRequest);
        Long createdId = extractId(response);
        // then
        즐겨찾기_생성됨(response);

        // when
        response = 즐겨찾기_목록_조회_요청();
        // then
        즐겨찾기_목록_조회됨(response);

        // when
        response = 즐겨찾기_삭제_요청(createdId);
        // then
        즐겨찾기_삭제됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(loginToken.getAccessToken())
            .when().delete("/favorites/{id}", id)
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        List<FavoriteResponse> favoriteResponse = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponse).isNotEmpty();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all()
            .auth().oauth2(loginToken.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(FavoriteRequest favoriteRequest) {
        return RestAssured.given().log().all()
            .auth().oauth2(loginToken.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long extractId(ExtractableResponse<Response> response) {
        String[] locations = response.header("Location").split("/");
        return Long.parseLong(locations[locations.length - 1]);
    }
}