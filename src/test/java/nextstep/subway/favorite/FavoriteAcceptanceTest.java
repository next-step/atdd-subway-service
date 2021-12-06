package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_등록되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        사용자 = AuthAcceptanceTest.로그인_되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void testManageFavorites() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 강남역.getId(), 양재역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        Long createdFavoritesId = Long.valueOf(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록이_조회된다(createdFavoritesId, findResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createdFavoritesId);
        // then
        즐겨찾기_삭제됨(createdFavoritesId, deleteResponse);
    }

    private void 즐겨찾기_삭제됨(Long createdFavoritesId, ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> findResponse2 = 즐겨찾기_조회_요청(사용자, createdFavoritesId);
        assertThat(findResponse2.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse tokenResponse, Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .when().get("/favorites/{id}", id)
                .then().log().all().extract();
    }

    private void 즐겨찾기_목록이_조회된다(Long createdFavoritesId, ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoritesResponse> favoritesResponses = findResponse.jsonPath().getList(".", FavoritesResponse.class);
        assertThat(favoritesResponses).hasSize(1)
                .contains(new FavoritesResponse(createdFavoritesId, 강남역, 양재역));
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, Long source, Long target) {
        FavoritesRequest request = new FavoritesRequest(source, target);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .body(request)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
