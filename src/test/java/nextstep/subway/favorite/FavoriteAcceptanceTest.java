package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteReuqest;
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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 삼성역;
    private StationResponse 선릉역;
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10));
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "green", 삼성역.getId(), 선릉역.getId(), 10));

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = AuthAcceptanceTest.로그인_요청(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 양재역.getId());
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성_요청(accessToken, 삼성역.getId(), 선릉역.getId());
        // then
        즐겨찾기_생성_됨(createResponse1);
        즐겨찾기_생성_됨(createResponse2);

        // when
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록조회_요청(accessToken);
        // then
        즐겨찾기_목록조회_됨(listResponse, 2);

        // when
        Long createdId = Long.parseLong(createResponse2.header("Location").split("/")[2]);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createdId);
        // then
        즐겨찾기_삭제_됨(deleteResponse);
    }

    @DisplayName("즐겨찾기 추가 예외 -출발과 도착역이 같으면 즐겨찾기를 추가할 수 없다.")
    @Test
    void create_exception() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 강남역.getId());

        // then
        요청_실패_됨(response);
    }

    private void 요청_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        FavoriteReuqest request = new FavoriteReuqest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_됨(ExtractableResponse<Response> createResponse1) {
        assertThat(createResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록조회_됨(ExtractableResponse<Response> listResponse, int size) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.jsonPath().getList(".", FavoriteResponse.class)).hasSize(size);
    }

    private void 즐겨찾기_삭제_됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
