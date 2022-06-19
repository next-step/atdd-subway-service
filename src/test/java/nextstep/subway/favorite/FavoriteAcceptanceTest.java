package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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

    StationResponse 왕십리역;
    StationResponse 서울숲역;
    StationResponse 강남구청역;
    StationResponse 선릉역;

    LineResponse 분당선;

    String 사용자;
    String 다른_사용자;

    @BeforeEach
    void init() {
        // given
        왕십리역 = StationAcceptanceTest.지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        서울숲역 = StationAcceptanceTest.지하철역_등록되어_있음("서울숲역").as(StationResponse.class);
        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("강남구청역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        // and
        분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("분당선", "노랑", 왕십리역.getId(), 선릉역.getId(), 7)).as(LineResponse.class);
        // and
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(분당선, 왕십리역, 서울숲역, 3);
        // and
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD, MemberAcceptanceTest.NEW_AGE);
        // and
        사용자 = AuthAcceptanceTest.로그인되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        다른_사용자 = AuthAcceptanceTest.로그인되어_있음(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     * <p>
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * <p>
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 서울숲역.getId(), 선릉역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(findResponse, Arrays.asList(createResponse));

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * Scenario: 즐겨찾기를 조회한다.
     * Given 사용자와 다른 사용자의 즐겨찾기 생성 요청
     * and 즐겨찾기 생성됨
     * When 사용자의 즐겨찾기 목록 조회 요청
     * Then 사용자의 즐겨찾기 목록만 조회됨
     */
    @DisplayName("사용자의 즐겨찾기 목록 조회")
    @Test
    void findFavorites() {
        // given
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성_요청(사용자, 서울숲역.getId(), 선릉역.getId());
        즐겨찾기_생성됨(createResponse1);
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성_요청(다른_사용자, 왕십리역.getId(), 강남구청역.getId());
        즐겨찾기_생성됨(createResponse2);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_목록_조회됨(findResponse, Arrays.asList(createResponse1));
    }

    /**
     * Scenario: 즐겨찾기를 관리 예외
     * given 즐겨찾기 생성 요청
     * and 즐겨찾기 생성됨
     * When 다른 사용자의 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제 실패
     */
    @DisplayName("다른 사용자의 즐겨찾기 삭제")
    @Test
    void deleteOthersFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 서울숲역.getId(), 선릉역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(다른_사용자, createResponse);

        // then
        즐겨찾기_삭제_실패됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponses) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> foundFavorites = findResponse.jsonPath()
                .getList(".", FavoriteResponse.class);
        List<FavoriteResponse> createdFavorites = createResponses.stream()
                .map(response -> response.as(FavoriteResponse.class))
                .collect(Collectors.toList());
        assertThat(foundFavorites).containsExactlyInAnyOrderElementsOf(createdFavorites);
    }
    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    private void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
