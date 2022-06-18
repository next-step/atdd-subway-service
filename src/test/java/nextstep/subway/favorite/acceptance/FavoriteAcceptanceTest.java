package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private String token;

    @BeforeEach
    public void setUp(){
        super.setUp();

        //given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

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


    /**
     * Feature: 즐겨찾기 관리를 실패한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기 관리를 실패
     *     When 즐겨찾기 생성을 두 번 요청
     *     Then 두 번째 즐겨찾기 생성은 실패
     *     When 존재하지 않는 역으로 즐겨찾기 생성 요청
     *     Then 즐겨찾기 생성 실패
     *     When 동일한 역을 출발지/목적지로 즐겨찾기 생성 요청
     *     Then 즐겨찾기 생성 실패
     *     When 잘못된 토큰으로 즐겨찾기 생성 요청
     *     Then 즐겨찾기 생성 실패
     *     When 잘못된 토큰으로 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제 실패
     */
    @DisplayName("즐겨찾기 관리를 실패한다.")
    @Test
    void 즐겨찾기_관리_비정상_시나리오() {
        //When
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), 광교역.getId());
        ExtractableResponse<Response> createResponseDup = 즐겨찾기_생성_요청(token, 강남역.getId(), 광교역.getId());
        //Then
        즐겨찾기_생성_실패(createResponseDup);

        //When
        Station 존재하지않는역 = Station.from("존재하지않는역");
        ExtractableResponse<Response> unknownCreateResponse = 즐겨찾기_생성_요청(token, 존재하지않는역.getId(), 광교역.getId());
        //Then
        즐겨찾기_생성_실패(unknownCreateResponse);

        //When
        ExtractableResponse<Response> sameStationCreateResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), 강남역.getId());
        //Then
        즐겨찾기_생성됨(sameStationCreateResponse);

        //When
        ExtractableResponse<Response> createFailResponse = 즐겨찾기_생성_요청("invalidToken", 강남역.getId(), 광교역.getId());
        //Then
        즐겨찾기_생성_실패(createFailResponse);

        //When
        ExtractableResponse<Response> deleteFailResponse = 즐겨찾기_삭제_요청("invalidToken", sameStationCreateResponse);
        //Then
        즐겨찾기_삭제_실패(deleteFailResponse);
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStationId, targetStationId);
        return postWithAuth(accessToken, "/favorites", favoriteRequest);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return getWithAuth(accessToken, "/favorites");
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return deleteWithAuth(accessToken, uri);
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

    private void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}