package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private LineResponse 삼호선;
    private TokenResponse 로그인_토큰;
    private TokenResponse 배드_토큰;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        로그인_토큰 = AuthAcceptanceTest.로그인_요청_성공(EMAIL, PASSWORD);
        배드_토큰 = new TokenResponse("bad");
    }

    @TestFactory
    @DisplayName("즐겨찾기를 관리한다")
    Stream<DynamicTest> manageFavorite() {
        return Stream.of(
                dynamicTest("즐겨찾기 생성을 요청(다른 토큰으로 삭제요청) 실패", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(배드_토큰, 교대역.getId(), 양재역.getId());
                    즐겨찾기_생성_실패됨(response);
                }),
                dynamicTest("즐겨찾기 생성을 요청", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인_토큰, 교대역.getId(), 양재역.getId());
                    즐겨찾기_생성됨(response);
                }),
                dynamicTest("즐겨찾기 목록 조회 요청", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_목록_요청(로그인_토큰);
                    즐겨찾기_목록_요청됨(response);
                    즐겨찾기_목록_카운트_확인(response, 1);
                }),
                dynamicTest("즐겨찾기 삭제 요청(다른 토큰으로 삭제요청) 실패", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(배드_토큰, 1L);
                    즐겨찾기_삭제_실패됨(response);
                }),
                dynamicTest("즐겨찾기 삭제 요청", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(로그인_토큰, 1L);
                    즐겨찾기_삭제됨(response);
                }),
                dynamicTest("즐겨찾기 삭제 후 목록 조회 요청", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_목록_요청(로그인_토큰);
                    즐겨찾기_목록_요청됨(response);
                    즐겨찾기_목록_카운트_확인(response, 0);
                })
        );
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_목록_카운트_확인(ExtractableResponse<Response> response, int count) {
        assertThat(response.jsonPath().getList(".", FavoriteResponse.class).size()).isEqualTo(count);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, Long favoriteId) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/favorites/" + favoriteId)
                .then().log().all().extract();
        return response;
    }

    private void 즐겨찾기_목록_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_요청(TokenResponse tokenResponse) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
        return response;
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}