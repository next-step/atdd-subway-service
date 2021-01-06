package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@DisplayName("즐겨찾기 관련 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 생성 요청")
    @Test
    void addFavorite() {
        // given
        // 지하철역 등록 되어 있음
        StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        LineRequest lineRequest = new LineRequest("신분당선", "red", 양재역.getId(), 광교역.getId(), 10);
        // 지하철 노선 등록 되어 있음
        // 지하철 노선에 지하철역 등록되어 있음
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest);
        // 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청("hglee", "1234", 30);
        // 로그인 되어있음
        TokenResponse tokenResponse = AuthAcceptanceTest.토큰_발급_요청("hglee", "1234").as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(양재역.getId() + "", 광교역.getId() + "", tokenResponse.getAccessToken());

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String source, String target, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return RestAssured.given().auth().oauth2(token).log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/favorites").
                then().
                log().all().
                extract();
    }

    @DisplayName("즐겨찾기 목록 조회 요청")
    @Test
    void findAllFavorite() {
        // given
        // 즐겨찾기 생성됨

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                accept(MediaType.APPLICATION_JSON_VALUE).
                get("/favorites").
                then().
                log().all().
                extract();
    }

    @DisplayName("즐겨찾기 삭제 요청")
    @Test
    void removeFavorite() {
        // when
        long favoriteId = 1L;
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(favoriteId);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(long favoriteId) {
        return RestAssured.given().log().all().
                when().
                delete("/favorites/{id}", favoriteId + "").
                then().
                log().all().
                extract();
    }
}