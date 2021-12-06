package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    StationResponse 잠실역;
    StationResponse 삼성역;
    StationResponse 선릉역;
    LineResponse 이호선;
    String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        삼성역 = 지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest(
            "이호선", "bg-green-600", 잠실역.getId(), 삼성역.getId(), 10)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 선릉역, 10);
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_발급을_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(잠실역.getId(), 삼성역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        //when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청();
        // then
        즐겨찾기_목록_조회됨(findResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/favorites")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/favorites")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
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

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
