package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.로그인_요청;
import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {


    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest;

    /**
     * <p>Feature: 즐겨찾기를 관리한다.
     *
     * <p>Background
     * <p>Given 지하철역 등록되어 있음
     * <p>And 지하철 노선 등록되어 있음
     * <p>And 지하철 노선에 지하철역 등록되어 있음
     * <p>And 회원 등록되어 있음
     * <p>And 로그인 되어있음
     *
     * <p>Scenario: 즐겨찾기를 관리
     * <p>When 즐겨찾기 생성을 요청
     * <p>Then 즐겨찾기 생성됨
     * <p>When 즐겨찾기 목록 조회 요청
     * <p>Then 즐겨찾기 목록 조회됨
     * <p>When 즐겨찾기 삭제 요청
     * <p>Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void favoriteManage() {
        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        // And 지하철 노선에 지하철역 등록되어 있음
        lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        지하철_노선_등록되어_있음(lineRequest);

        // And 회원 등록되어 있음
        ExtractableResponse<Response> createMemberResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createMemberResponse);

        // And 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String accessToken = loginResponse.jsonPath().getString("accessToken");

        // When 즐겨찾기 생성을 요청
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 광교역.getId());

        ExtractableResponse<Response> createFavoriteResponse = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
        // Then 즐겨찾기 생성됨
        assertThat(createFavoriteResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
