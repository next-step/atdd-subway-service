package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_실패됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * Feature: 즐겨찾기를 관리한다.
     *
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
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void 즐겨찾기_관리_정상_시나리오() {
        //given
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "red", 강남역.getId(), 광교역.getId(), 10));
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        //When
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), 광교역.getId());
        //Then
        즐겨찾기_생성됨(createResponse);

        //When
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(token);
        //Then
        즐겨찾기_목록_조회됨(findResponse);

        //When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, createResponse);
        //Then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStationId, targetStationId);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}