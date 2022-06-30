package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
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

    private String email = "lcjltj@gmail.com";
    private String password = "password";
    private int age = 15;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5, 1000)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 남부터미널역, 15);
        삼호선 = LineAcceptanceTest.지하철_노선_조회_요청(삼호선).as(LineResponse.class);

        MemberAcceptanceTest.회원_생성_되어_있음(email, password, age);
    }

    @Test
    @DisplayName("즐겨 찾기 관리")
    void addFavorite() {
        // when
        final ExtractableResponse<Response> 로그인_요청 = AuthAcceptanceTest.로그인_요청(TokenRequest.of(email, password));
        // then
        final String accessToken = AuthAcceptanceTest.로그인_성공됨(로그인_요청);

        // when
        final ExtractableResponse<Response> 지하철_최단거리_조회 = PathAcceptanceTest.회원_지하철_최단거리_조회(강남역.getId(), 남부터미널역.getId(), accessToken);
        // then
        PathAcceptanceTest.회원_지하철_최단거리_조회됨(지하철_최단거리_조회);

        // when
        final ExtractableResponse<Response> 즐겨찾기_추가_요청 = 즐겨찾기_추가_요청(accessToken, 강남역.getId(), 남부터미널역.getId());
        // then
        즐겨찾기_추가됨(즐겨찾기_추가_요청);

        // when
        final ExtractableResponse<Response> 즐겨찾기_조회_요청 = 즐겨찾기_조회_요청(accessToken);
        // then
        final List<FavoriteResponse> 즐겨찾기_조회됨 = 즐겨찾기_조회됨(즐겨찾기_조회_요청);

        // given
        final FavoriteResponse favoriteResponse = 즐겨찾기_조회됨.get(0);
        // when
        final ExtractableResponse<Response> 즐겨찾기_삭제_요청 = 즐겨찾기_삭제_요청(accessToken, favoriteResponse.getId());
        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청);

        // given
        final List<FavoriteResponse> 삭제_조회 = 즐겨찾기_조회_요청(accessToken).jsonPath().getList(".", FavoriteResponse.class);
        // then
        assertThat(삭제_조회).hasSize(0);
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(final String accessToken, final Long sourceStationId, final Long targetStationId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(FavoriteRequest.of(sourceStationId, targetStationId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_추가됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then()
                .extract();
    }

    public static List<FavoriteResponse> 즐겨찾기_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<FavoriteResponse> list = response.jsonPath()
                .getList(".", FavoriteResponse.class);
        assertThat(list).hasSize(1);
        return list;
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken, final Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", id)
                .then()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
