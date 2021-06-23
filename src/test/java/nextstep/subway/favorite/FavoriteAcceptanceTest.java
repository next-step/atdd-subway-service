package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인되어있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 방화역;
    private StationResponse 청구역;
    private StationResponse 미사역;
    private StationResponse 하남검단산역;
    private LineResponse 오호선;
    private TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        방화역 = 지하철역_등록되어_있음("방화역").as(StationResponse.class);
        청구역 = 지하철역_등록되어_있음("청구역").as(StationResponse.class);
        미사역 = 지하철역_등록되어_있음("미사역").as(StationResponse.class);
        하남검단산역 = 지하철역_등록되어_있음("하남검단산역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("오호선", "보라색", 방화역.getId(), 하남검단산역.getId(), 10);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(오호선, 방화역, 청구역, 3);
        지하철_노선에_지하철역_등록되어_있음(오호선, 청구역, 미사역, 3);

        회원_생성_되어있음(EMAIL, PASSWORD, AGE);
        토큰 = 로그인되어있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    public void manageFavorite() throws Exception {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청();

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> favoritesResponse = 즐겨찾기목록_조회_요청();

        // then
        즐겨찾기목록_조회됨(favoritesResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse);

        // then
        즐겨찾기_삭제됨(response);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .delete(createResponse.header("Location"))
                .then()
                        .log().all()
                        .extract();
        return response;
    }

    private void 즐겨찾기목록_조회됨(ExtractableResponse<Response> favoritesResponse) {
        assertThat(favoritesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기목록_조회_요청() {
        ExtractableResponse<Response> favoritesResponse =
                given()
                        .log().all()
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .get("/favorites")
                .then()
                        .log().all()
                        .extract();
        return favoritesResponse;
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청() {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(방화역.getId()));
        params.put("target", String.valueOf(하남검단산역.getId()));
        ExtractableResponse<Response> createResponse =
                given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .post("/favorites")
                .then()
                        .log().all().extract();
        return createResponse;
    }
}