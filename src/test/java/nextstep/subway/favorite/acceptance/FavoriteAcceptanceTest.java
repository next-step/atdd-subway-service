package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.getAccessToken;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 판교역;
    private StationResponse 양재역;

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = getAccessToken(로그인_요청(EMAIL, PASSWORD));
    }

    @DisplayName("즐겨찾기 관리 성공 시나리오")
    @Test
    void success_scenario() {
        // Given
        즐겨찾기_생성을_요청(token, 강남역, 광교역);
        int 목록_예상_사이즈 = 2;

        // When
        ExtractableResponse<Response> 즐겨찾기_생성_결과 = 즐겨찾기_생성을_요청(token, 강남역, 광교역);
        // Then
        즐겨찾기_생성됨(즐겨찾기_생성_결과);

        // When
        ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_요청(token);
        // Then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_결과, 목록_예상_사이즈);

        // When
        ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_목록_삭제_요청(즐겨찾기_생성_결과.as(FavoriteResponse.class).getId());
        // Then
        즐겨찾기_삭제됨(즐겨찾기_삭제_결과);
    }

    @DisplayName("즐겨찾기 관리 실패 시나리오")
    @Test
    void fail_scenario() {
        // Given
        StationResponse 존재하지않는역 = new StationResponse(99L, "존재하지않는역", LocalDateTime.now(), LocalDateTime.now());
        즐겨찾기_생성을_요청(token, 강남역, 광교역);

        // When
        ExtractableResponse<Response> 존재하지_않는_역으로_즐겨찾기_생성_결과 = 즐겨찾기_생성을_요청(token, 존재하지않는역, 광교역);
        // Then
        즐겨찾기_생성_실패됨(존재하지_않는_역으로_즐겨찾기_생성_결과);

        // When
        ExtractableResponse<Response> 존재하지_않는_ID로_즐겨찾기_삭제_결과 = 즐겨찾기_목록_삭제_요청(99L);
        // Then
        즐겨찾기_삭제_실패됨(존재하지_않는_ID로_즐겨찾기_삭제_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse 강남역, StationResponse 광교역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 광교역.getId());

        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int size) {
        List<FavoriteResponse> favorites = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites).hasSize(size);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
