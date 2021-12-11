package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.path.PathAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static String 토큰;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선(10)* ---   강남역
     * |                           |
     * *3호선(3)*                   *신분당선(10)*
     * |                           |
     * 남부터미널역  --- *3호선(2)* --- 양재역
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }
    
    @DisplayName("즐겨찾기를 관리하는 시나리오를 테스트한다.")
    @Test
    void scenarioTest() {
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(토큰, 강남역, 남부터미널역);
        //then
        즐겨찾기_생성됨(createResponse);
        //when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(토큰);
        //then
        즐겨찾기_목록_조회됨(findResponse);
        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, 토큰);
        //then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("즐겨찾기 삭제시 유저가 즐겨찾기 주인이 아닌 경우 테스트")
    @Test
    void deleteFavorite() {
        //given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(토큰, 강남역, 남부터미널역);
        회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        String 새로운_유저_토큰 = 로그인_되어_있음(NEW_EMAIL, NEW_PASSWORD);

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, 새로운_유저_토큰);

        //then
        즐겨찾기_삭제_실패됨(deleteResponse);
    }

    private void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String token) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse upStation, StationResponse downStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(upStation.getId(), downStation.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
