package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.domain.LineFixture.이호선;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     Feature: 즐겨찾기를 관리한다.

     Background
     Given 지하철역 등록되어 있음
     And 지하철 노선 등록되어 있음
     And 지하철 노선에 지하철역 등록되어 있음
     And 회원 등록되어 있음
     And 로그인 되어있음

     Scenario: 즐겨찾기를 관리
     When 즐겨찾기 생성을 요청
     Then 즐겨찾기 생성됨
     When 즐겨찾기 목록 조회 요청
     Then 즐겨찾기 목록 조회됨
     When 즐겨찾기 삭제 요청
     Then 즐겨찾기 삭제됨
     */

    private StationResponse 영등포구청역;
    private StationResponse 당산역;
    private StationResponse 합정역;
    private StationResponse 홍대입구역;
    private LineResponse 이호선;

    private static String token;
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private static final String BASE_URL = "/favorites";

    @BeforeEach
    void setup() {
        영등포구청역 = StationAcceptanceTest.지하철역_생성_요청("영등포구청역").as(StationResponse.class);
        당산역 = StationAcceptanceTest.지하철역_생성_요청("당산역").as(StationResponse.class);
        합정역 = StationAcceptanceTest.지하철역_생성_요청("합정역").as(StationResponse.class);
        홍대입구역 = StationAcceptanceTest.지하철역_생성_요청("홍대입구역").as(StationResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "Yellow-green", 영등포구청역.getId(), 당산역.getId(), 20)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 당산역, 합정역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 합정역, 홍대입구역, 10);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = AuthAcceptanceTest.토큰_발급(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기 생성을 요청한다.")
    @Test
    void 즐겨찾기_생성_테스트() {
        // when
        ExtractableResponse<Response> createRequest = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 홍대입구역.getId()));

        // then
        즐겨찾기_생성됨(createRequest);

    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void 즐겨찾기_목록_조회_테스트() {
        // given
        즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 홍대입구역.getId()));
        즐겨찾기_생성_요청(new FavoriteRequest(영등포구청역.getId(), 홍대입구역.getId()));
        즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 합정역.getId()));

        // when
        ExtractableResponse<Response> retrieveResponse = 즐겨찾기_목록_조회_요청();

        // then
        즐겨찾기_목록_조회됨(retrieveResponse);

    }

    @DisplayName("즐겨찾기를 삭제 조회한다.")
    @Test
    void 즐겨찾기_삭제_테스트() {
        // given
        FavoriteResponse 즐겨찾기1 = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 홍대입구역.getId())).as(FavoriteResponse.class);
        FavoriteResponse 즐겨찾기2 = 즐겨찾기_생성_요청(new FavoriteRequest(영등포구청역.getId(), 홍대입구역.getId())).as(FavoriteResponse.class);
        FavoriteResponse 즐겨찾기3 = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 합정역.getId())).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> deleteRequest = 즐겨찾기_삭제_요청(즐겨찾기3.getId());

        // then
        즐겨찾기_목록_조회_요청();
        즐겨찾기_삭제됨(deleteRequest);

    }

    @DisplayName("기존에 등록된 즐겨찾기를 생성한다.")
    @Test
    void 중복된_즐겨찾기_등록_테스트() {
        // given
        FavoriteResponse 즐겨찾기1 = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 홍대입구역.getId())).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> createRequest = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 홍대입구역.getId()));

        // then
        즐겨찾기_생성되지_않음(createRequest);
    }

    @DisplayName("출발역과 도착역이 같은 구간을 즐겨찾기 생성한다.")
    @Test
    void 출발역과_도착역이_같은_즐겨찾기_등록_테스트() {
        // when
        ExtractableResponse<Response> createRequest = 즐겨찾기_생성_요청(new FavoriteRequest(당산역.getId(), 당산역.getId()));

        // then
        즐겨찾기_생성되지_않음(createRequest);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(BASE_URL + "/" + favoriteId)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_생성되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}