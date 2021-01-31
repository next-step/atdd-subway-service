package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String PASSWORD = "1234";
    public static final String EMAIL = "eversong0723@gmail.com";
    private static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    public void managingFavorite() {
        // given
        StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 강남역.getId(), 교대역.getId(), 10, 200));
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, 10);
        TokenResponse 토큰 = AuthAcceptanceTest.토큰_로그인_요청(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class);
        String token = BEARER_TYPE + 토큰.getAccessToken();

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, new FavoriteRequest(강남역.getId(), 교대역.getId()));

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(token);

        // then
        즐겨찾기_목록_조회됨(findResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(1l, token);

        // then
        즐겨찾기_삭제됨(deleteResponse);

    }


    static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();

    }

    static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/" + id)
                .then().log().all()
                .extract();
    }

    static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}