package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private LineResponse 신분당선;
    private LineResponse 이호선;

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

        신분당선 = PathAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 0L, 10).as(LineResponse.class);
        이호선 = PathAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 0L, 20).as(LineResponse.class);

        PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 교대역, 3);
        PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = MemberAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 강남역, 교대역);
        //then
        즐겨찾기_생성됨(createResponse);
        //when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);
        //then
        즐겨찾기_목록_조회됨(findResponse);
        즐겨찾기_목록_확인됨(findResponse, createResponse);
        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        //then
        즐겨찾기_삭제됨(deleteResponse);

        //when
        ExtractableResponse<Response> findResponse2 = 즐겨찾기_목록_조회_요청(사용자);
        //then
        즐겨찾기_목록_조회됨(findResponse2);
        즐겨찾기_목록_확인_실패됨(findResponse2, createResponse);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_목록_확인됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createResponse) {
        Long expectedFavoriteId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        List<Long> favoriteIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());
        assertThat(favoriteIds).contains(expectedFavoriteId);
    }

    private void 즐겨찾기_목록_확인_실패됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createResponse) {
        Long expectedFavoriteId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        List<Long> favoriteIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());
        assertThat(favoriteIds).doesNotContain(expectedFavoriteId);
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse user, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all().auth().oauth2(user.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse user) {
        return RestAssured
                .given().log().all().auth().oauth2(user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse user, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured
                .given().log().all().auth().oauth2(user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
