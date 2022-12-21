package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

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

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        신림역 = 지하철역_등록되어_있음("신림역").as(StationResponse.class).getId();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class).getId();
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class).getId();
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class).getId();

        LineRequest lineRequest = new LineRequest("이호선", "bg-green-600", 신림역, 왕십리역, 10);
        이호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class).getId();

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

        FavoriteRequest favoriteRequest = new FavoriteRequest(신림역, 강남역);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .auth().oauth2(로그인_성공_토큰_값)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }
}
