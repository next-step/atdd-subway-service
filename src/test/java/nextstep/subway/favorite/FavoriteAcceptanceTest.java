package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteStationResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 이어지지않은역_1;
    private StationResponse 이어지지않은역_2;
    private LineResponse 신분당선;
    private LineResponse 별도호선;
    private String accessToken;
    private final String failToken = "failToken";

    /*
     Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
        And 로그인 되어있음
    */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(nextstep.subway.station.dto.StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(nextstep.subway.station.dto.StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(nextstep.subway.station.dto.StationResponse.class);
        이어지지않은역_1 = StationAcceptanceTest.지하철역_등록되어_있음("이어지지않은역_1").as(nextstep.subway.station.dto.StationResponse.class);
        이어지지않은역_2 = StationAcceptanceTest.지하철역_등록되어_있음("이어지지않은역_2").as(nextstep.subway.station.dto.StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "신분당선",
                        "bg-red-600",
                        강남역.getId(),
                        양재역.getId(),
                        10))
                .as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역, 3);
        별도호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest(
                        "별도호선",
                        "bg-red-600",
                        이어지지않은역_1.getId(),
                        이어지지않은역_2.getId(),
                        5))
                .as(LineResponse.class);

        MemberAcceptanceTest.회원_생성을_요청("eunveloper@gmail.com", "1234", 28);
        accessToken = AuthAcceptanceTest.로그인_요청("eunveloper@gmail.com", "1234").as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void saveFavorite() {
        // when 즐겨찾기를 등록한다.
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // then 즐겨찾기 등록을 성공한다.
        즐겨찾기_등록_성공(response);
    }

    @DisplayName("존재하지 않는 지하철역의 즐겨찾기를 등록한다.")
    @Test
    void saveFavoriteNotFoundStation() {
        // when 즐겨찾기를 등록한다.
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 999L);

        // then 즐겨찾기 등록을 실패한다.
        파라미터_문제로_요청_실패(response);
    }

    @DisplayName("이어지지 않는 지하철역의 즐겨찾기를 등록한다.")
    @Test
    void saveFavoriteNotLinkedStation() {
        // when 즐겨찾기를 등록한다.
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 이어지지않은역_1.getId());

        // then 즐겨찾기 등록을 실패한다.
        파라미터_문제로_요청_실패(response);
    }

    @DisplayName("잘못된 인증정보로 지하철역의 즐겨찾기를 등록한다.")
    @Test
    void saveFavoriteUnAuthorization() {
        // when 즐겨찾기를 등록한다.
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(failToken, 강남역.getId(), 양재역.getId());

        // then 즐겨찾기 등록을 실패한다.
        인증_문제로_요청_실패(response);
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorite() {
        // given 즐겨찾기가 등록되어 있다.
        즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // when 즐겨찾기를 조회한다.
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);

        // then 즐겨찾기 조회를 성공한다.
        즐겨찾기_조회_성공(response);
        즐겨찾기_조회_포함됨(response, Arrays.asList(강남역.getId()));
    }

    @DisplayName("잘못된 인증정보로 즐겨찾기를 조회한다.")
    @Test
    void getFavoriteUnAuthorization() {
        // given 즐겨찾기가 등록되어 있다.
        즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // when 즐겨찾기를 조회한다.
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(failToken);

        // then 즐겨찾기 조회를 실패한다.
        인증_문제로_요청_실패(response);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void removeFavorite() {
        // given 즐겨찾기가 등록되어 있다.
        ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // when 즐겨찾기를 삭제한다.
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse.header("Location"), accessToken);

        // then 즐겨찾기 삭제를 성공한다.
        즐겨찾기_삭제_성공(response);
    }

    @DisplayName("존재하지 않는 즐겨찾기를 삭제한다.")
    @Test
    void removeNotFoundFavorite() {
        // given 즐겨찾기가 등록되어 있다.
        ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // when 즐겨찾기를 삭제한다.
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청("/favorites/" + 999, accessToken);

        // then 즐겨찾기 삭제를 실패한다.
        파라미터_문제로_요청_실패(response);
    }

    @DisplayName("잘못된 인증정보로 즐겨찾기를 삭제한다.")
    @Test
    void removeFavoriteUnAuthorization() {
        // given 즐겨찾기가 등록되어 있다.
        ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());

        // when 즐겨찾기를 삭제한다.
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse.header("Location"), failToken);

        // then 즐겨찾기 삭제를 실패한다.
        인증_문제로_요청_실패(response);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_등록_요청(accessToken, 강남역.getId(), 양재역.getId());
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_등록_요청(accessToken, 양재역.getId(), 교대역.getId());
        // Then 즐겨찾기 생성됨
        즐겨찾기_등록_성공(createResponse1);
        즐겨찾기_등록_성공(createResponse2);

        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(accessToken);
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_조회_성공(findResponse);
        즐겨찾기_조회_포함됨(findResponse, Arrays.asList(강남역.getId(), 양재역.getId()));

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse1.header("Location"), accessToken);
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제_성공(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(sourceId, targetId))
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_조회_포함됨(ExtractableResponse<Response> response, List<Long> expectedFavoriteIds) {
        List<Long> resultLineIds = response.jsonPath().getList(".", FavoriteResponse.class)
                .stream()
                .map(FavoriteResponse::getSource)
                .collect(Collectors.toList())
                .stream()
                .map(FavoriteStationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedFavoriteIds);
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 파라미터_문제로_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 인증_문제로_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}