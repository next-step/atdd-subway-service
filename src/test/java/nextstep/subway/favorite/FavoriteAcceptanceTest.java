package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        LineAcceptanceTest.지하철_노선_생성_요청(신분당선);
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
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
    void manageFavorite() {
        //given
        String token = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD)
                .as(TokenResponse.class)
                .getAccessToken();

        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), 광교역.getId());
        //then
        응답결과_확인(createResponse, HttpStatus.CREATED);

        //when
        ExtractableResponse<Response> findAllResponse = 즐겨찾기_목록_조회_요청(token);
        List<FavoriteResponse> favoriteResponses = Arrays.asList(findAllResponse.body().as(FavoriteResponse[].class));
        //then
        응답결과_확인(findAllResponse, HttpStatus.OK);
        즐겨찾기_목록_조회됨(favoriteResponses);

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, favoriteResponses.get(0).getId());
        //then
        응답결과_확인(deleteResponse, HttpStatus.NO_CONTENT);
    }

    private void 즐겨찾기_목록_조회됨(List<FavoriteResponse> favoriteResponses) {
        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(광교역.getId());
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .body(new FavoriteRequest(sourceStationId, targetStationId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .when().delete("/favorites", favoriteId)
                .then().log().all()
                .extract();
    }
}
