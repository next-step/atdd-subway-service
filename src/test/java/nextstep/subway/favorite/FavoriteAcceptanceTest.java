package nextstep.subway.favorite;

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
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private TokenResponse 로그인_유저_토큰;

    /**
     * 교대역    --- *2호선*(10) --- 강남역
     * |                        |
     * *3호선*(3)                *신분당선*(10)
     * |                        |
     * 남부터미널역--- *3호선*(2) --- 양재
     */
    @BeforeEach
    public void setUp() {
        // given
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600",0, 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600",0, 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600",0, 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        ExtractableResponse<Response> 회원_생성_결과 = MemberAcceptanceTest.회원_생성을_요청("joojimin@naver.com", "123123", 30);
        MemberAcceptanceTest.회원_생성됨(회원_생성_결과);

        ExtractableResponse<Response> 로그인_결과 = AuthAcceptanceTest.로그인_요청("joojimin@naver.com", "123123");
        로그인_유저_토큰 = AuthAcceptanceTest.로그인_성공(로그인_결과);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void favoriteTest() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(로그인_유저_토큰.getAccessToken(), 남부터미널역.getId(), 강남역.getId());
        // then
        FavoriteResponse 생성된_즐겨찾기 = 즐겨찾기_생성_성공(즐겨찾기_생성_요청_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회(로그인_유저_토큰.getAccessToken());
        // then
        즐겨찾기_목록_조회_성공(즐겨찾기_목록_조회_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(로그인_유저_토큰.getAccessToken(), 생성된_즐겨찾기.getId());
        // then
        즐겨찾기_삭제_성공(즐겨찾기_삭제_결과);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long source, final Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);

        // when
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static FavoriteResponse 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(FavoriteResponse.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(final String accessToken) {
        // when
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static void 즐겨찾기_목록_조회_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken, final Long favoriteId) {
        // when
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
