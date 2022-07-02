package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
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
    private static final String FAVORITES_URL = "/favorites";

    private String 사용자;
    private StationResponse 강남역;
    private StationResponse 정자역;

    private static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    /**
     * Background Given 지하철역 등록되어 있음 And 지하철 노선 등록되어 있음 And 지하철 노선에 지하철역 등록되어 있음 And 회원 등록되어 있음 And 로그인 되어있음
     */
    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest 노선_생성_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(노선_생성_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다. Scenario: 즐겨찾기를 관리 When 즐겨찾기 생성을 요청 Then 즐겨찾기 생성됨 When 즐겨찾기 목록 조회 요청 Then 즐겨찾기 목록 조회됨 When
     * 즐겨찾기 삭제 요청 Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 정자역);
        // then
        즐겨찾기_생성됨(createResponse);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, HttpStatus.CREATED);
    }
}