package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));

        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        accessToken = 로그인_요청(EMAIL, PASSWORD)
                .as(TokenResponse.class)
                .getAccessToken();

    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 강남역, 양재역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회됨(listResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("출발역과 종료역을 같게 하여 즐겨찾기 생성한다")
    @Test
    void createFavoriteWithSameStation() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 강남역, 강남역);
        // then
        즐겨찾기_생성_실패됨(createResponse);
    }

    @DisplayName("기존에 존재하는 즐겨찾기 항목으로 즐겨찾기를 등록한다")
    @Test
    void createFavoriteWithDuplicateStations() {
        // given
        즐겨찾기_생성을_요청(accessToken, 강남역, 양재역);
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 강남역, 양재역);
        // then
        즐겨찾기_생성_실패됨(createResponse);
    }

    @DisplayName("다른 사용자의 즐겨찾기 항목을 삭제한다")
    @Test
    void deleteFavoriteOtherCreated() {
        // given
        String email = "aaa@gmail.com";
        String password = "1234";
        int age = 10;
        회원_생성을_요청(email, password, age);

        String otherAccessToken = 로그인_요청(email, password)
                .as(TokenResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(otherAccessToken, 강남역, 양재역);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, response);
        // then
        즐겨찾기_조회_안됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_조회_안됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
