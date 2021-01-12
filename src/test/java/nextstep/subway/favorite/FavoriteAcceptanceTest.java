package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final int DEFAULT_DISTANCE = 10;
    private static final String EMAIL = "example@sample.com";
    private static final String PASSWORD = "example";
    private static final int AGE = 18;

    private StationResponse 신도림;
    private StationResponse 도림천;
    private StationResponse 양천구청;

    private LineResponse 이호선;

    private TokenResponse tokenResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        신도림 = StationAcceptanceTest.지하철역_등록되어_있음("신도림").as(StationResponse.class);
        도림천 = StationAcceptanceTest.지하철역_등록되어_있음("도림천").as(StationResponse.class);
        양천구청 = StationAcceptanceTest.지하철역_등록되어_있음("양천구청").as(StationResponse.class);

        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 신도림.getId(), 도림천.getId(), DEFAULT_DISTANCE)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 도림천, 양천구청, DEFAULT_DISTANCE);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);

        tokenResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기 정보를 관리한다")
    @Test
    public void manageFavorite() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 양천구청.getId());
        즐겨찾기_생성됨(createdResponse);
        ExtractableResponse<Response> selectedResponse = 즐겨찾기_목록_조회_요청(tokenResponse.getAccessToken());
        즐겨찾기_목록_조회됨(selectedResponse, 신도림, 양천구청);
        FavoriteResponse favoriteResponse = createdResponse.as(FavoriteResponse.class);
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_삭제_요청(tokenResponse.getAccessToken(), favoriteResponse.getId());
        즐겨찾기_삭제됨(deletedResponse);
    }

    @DisplayName("중복 된 즐겨찾기 정보를 등록한다")
    @Test
    public void createFavoriteDuplicate() {
        즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 양천구청.getId());
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 양천구청.getId());
        즐겨찾기_생성_실패됨(createdResponse);
    }

    @DisplayName("등록되지 않은 역 id로 즐겨찾기 정보를 등록한다")
    @Test
    public void createInvalidStationId() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 99L);
        즐겨찾기_생성_실패됨(createdResponse);
    }

    @DisplayName("인증되지 않은 토큰으로 즐겨찾기 정보를 조회한다")
    @Test
    public void findAllInvalidToken() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 양천구청.getId());
        즐겨찾기_생성됨(createdResponse);
        ExtractableResponse<Response> selectedResponse = 즐겨찾기_목록_조회_요청("invalid");
        즐겨찾기_조회_실패됨(selectedResponse);
    }

    @DisplayName("인증정보 없이 즐겨찾기 정보를 조회한다")
    @Test
    public void findAllWithoutAuth() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 신도림.getId(), 양천구청.getId());
        즐겨찾기_생성됨(createdResponse);
        ExtractableResponse<Response> selectedResponse = 인증없이_즐겨찾기_목록_조회_요청();
        즐겨찾기_조회_실패됨(selectedResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/favorites?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> createdResponse) {
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 인증없이_즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> selectedResponse, StationResponse source, StationResponse target) {
        assertThat(selectedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = Arrays.asList(selectedResponse.as(FavoriteResponse[].class));
        FavoriteResponse favoriteResponse = favoriteResponses.get(0);

        assertThat(favoriteResponse.getSource()).isEqualTo(source);
        assertThat(favoriteResponse.getTarget()).isEqualTo(target);
    }

    public static void 즐겨찾기_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deletedResponse) {
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}