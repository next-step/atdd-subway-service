package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthFixture.로그인_요청_후_토큰_가져오기;
import static nextstep.subway.line.acceptance.LineFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberFixture.회원_생성을_요청;
import static nextstep.subway.station.StationFixture.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 정자역;

    private String accessToken;

    public static ExtractableResponse<Response> 즐겨찾기_생성(final String accessToken, Long source,
        Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(final String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken,
        final String uri) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId(), 10));
        회원_생성을_요청("test@test.com", "1234", 10);
        accessToken = 로그인_요청_후_토큰_가져오기("test@test.com", "1234");
    }

    /*
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
    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageFavorite() {
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성(accessToken, 강남역.getId(),
            정자역.getId());
        //then
        즐겨찾기_생성됨(createResponse);

        //when
        List<FavoriteResponse> getFavoriteResponses = 즐겨찾기_목록_조회(accessToken).jsonPath()
            .getList(".", FavoriteResponse.class);
        //then
        즐겨찾기_목록_조회됨(getFavoriteResponses);

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken,
            getURI(createResponse));
        //then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(getURI(response)).isNotNull();
    }

    private String getURI(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    private void 즐겨찾기_목록_조회됨(List<FavoriteResponse> responses) {
        assertThat(responses)
            .hasSize(1)
            .extracting(FavoriteResponse::getSource)
            .map(StationResponse::getName)
            .isEqualTo("강남역");
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}