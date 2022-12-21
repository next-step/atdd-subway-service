package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private String 로그인_성공_토큰_값;

    private Long 신림역;
    private Long 강남역;
    private Long 잠실역;
    private Long 왕십리역;
    private Long 이호선;

    /**
     * Background
     *   Given 지하철역 등록되어 있음
     *   And 지하철 노선 등록되어 있음
     *   And 지하철 노선에 지하철역 등록되어 있음
     *   And 회원 등록되어 있음
     *   And 로그인 되어있음
     */
    @BeforeEach
    public void set_up() {
        super.setUp();

        신림역 = 지하철역_등록되어_있음("신림역").as(StationResponse.class).getId();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class).getId();
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class).getId();
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class).getId();

        LineRequest lineRequest = new LineRequest("이호선", "bg-green-600", 신림역, 왕십리역, 10);
        이호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class).getId();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        System.out.println(로그인_성공_토큰_값);

    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manage_favorite() {

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_성공_토큰_값, 신림역, 강남역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // Given
        즐겨찾기_생성_요청(로그인_성공_토큰_값, 강남역, 잠실역);
        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_목록_조회_요청(로그인_성공_토큰_값);
        // then
        즐겨찾기_목록_조회_요청됨(즐겨찾기_조회_응답);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성_응답) {
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록_조회_요청됨(ExtractableResponse<Response> 즐겨찾기_조회_응답) {
        assertAll(
            () -> assertThat(즐겨찾기_조회_응답.jsonPath().getList("source.name")).containsExactly("신림역", "강남역"),
            () -> assertThat(즐겨찾기_조회_응답.jsonPath().getList("target.name")).containsExactly("강남역", "잠실역"),
            () -> assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }
}
