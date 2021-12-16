package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_인증토큰_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private String 사용자토큰;

    //    Background
    //    Given 지하철역 등록되어 있음
    //    And 지하철 노선 등록되어 있음
    //    And 지하철 노선에 지하철역 등록되어 있음
    //    And 회원 등록되어 있음
    //    And 로그인 되어있음
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        회원_등록되어있음(MY_EMAIL, MY_PASSWORD, MY_AGE);
        사용자토큰 = 로그인_인증토큰_요청(MY_EMAIL, MY_PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자토큰, 강남역, 광교역);
        // then
        즐겨찾기_생성됨(createResponse);

        //    When 즐겨찾기 목록 조회 요청
        //    Then 즐겨찾기 목록 조회됨
        //    When 즐겨찾기 삭제 요청
        //    Then 즐겨찾기 삭제됨
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, StationResponse sourceStation, StationResponse targetStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}