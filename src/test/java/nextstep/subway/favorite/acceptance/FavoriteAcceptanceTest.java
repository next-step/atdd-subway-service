package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private LineResponse 신분당선;

    private String accessToken;
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, PASSWORD);
        accessToken = tokenResponse.jsonPath().getString("accessToken");
    }

    /**
     * Feature: 즐겨찾기 관리
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 동일 즐겨찾기 중복 생성을 요청
     *     Then 즐겨찾기 생성 실패됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     *     When 존재하지 않는 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제 실패
     */
    @DisplayName("즐겨찾기를 추가, 조회, 삭제한다")
    @Test
    void manageMyInfo() {
        // when: 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_추가_요청(accessToken, FavoriteRequest.of(강남역.getId(), 양재역.getId()));
        // then: 즐겨찾기 생성됨
        즐겨찾기_추가_성공(createResponse);
        // when: 즐겨찾기 중복 생성을 요청
        ExtractableResponse<Response> recreateResponse = 즐겨찾기_추가_요청(accessToken, FavoriteRequest.of(강남역.getId(), 양재역.getId()));
        // then: 즐겨찾기 생성됨
        즐겨찾기_추가_실패(recreateResponse);
        // when: 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(accessToken);
        // then: 즐겨찾기 생성됨
        즐겨찾기_조회_성공(getResponse);

        // when: 자신의 것이 아닌 즐겨찾기 삭제 요청
        ExtractableResponse<Response> noAuthDeleteResponse = 즐겨찾기_삭제_요청("fakeToken", 1L);
        // then: 즐겨찾기 삭제 실패
        권한없는_즐겨찾기_삭제_실패(noAuthDeleteResponse);

        // when: 즐겨찾기 삭제 요청
        List<StationResponse> stationResponses = getResponse.jsonPath().get();
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 1L);
        // then: 즐겨찾기 삭제됨
        즐겨찾기_삭제_성공(deleteResponse);
        // when: 존재하지 않는 즐겨찾기 삭제 요청
        ExtractableResponse<Response> reDeleteResponse = 즐겨찾기_삭제_요청(accessToken, 1L);
        // then: 즐겨찾기 삭제 실패
        존재하지않는_즐겨찾기_삭제_실패(reDeleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, FavoriteRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().
                extract();
    }

    public static void 즐겨찾기_추가_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_추가_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<StationResponse> stationResponses = response.jsonPath().get();
        assertThat(stationResponses).hasSize(1);
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 존재하지않는_즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 권한없는_즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}