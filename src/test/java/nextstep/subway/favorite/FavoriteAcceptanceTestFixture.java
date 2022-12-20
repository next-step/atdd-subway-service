package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.토큰_값;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestFixture.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestFixture.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceTestFixture extends AcceptanceTest {

    StationResponse 강남역;
    StationResponse 광교역;
    StationResponse 양재역;
    StationResponse 정자역;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public String myAccessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 4);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 정자역, 6);

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        myAccessToken = 토큰_값(로그인_되어_있음(new TokenRequest(EMAIL, PASSWORD)));
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, FavoriteCreateRequest favoriteCreateRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteCreateRequest)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static FavoriteResponse 즐겨찾기_등록되어_있음(String accessToken, FavoriteCreateRequest favoriteCreateRequest) {
        return 즐겨찾기_정보(즐겨찾기_생성_요청(accessToken, favoriteCreateRequest));
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static FavoriteResponse 즐겨찾기_정보(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", FavoriteResponse.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static List<FavoriteResponse> 즐겨찾기_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", FavoriteResponse.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/" + favoriteId)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
