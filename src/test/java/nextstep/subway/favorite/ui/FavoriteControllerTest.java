package nextstep.subway.favorite.ui;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteControllerTest extends AcceptanceTest {
    private StationResponse 양재역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 교대역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private MemberRequest 테스트계정;
    private String accessJwt;
    private FavoriteRequest favoriteRequest;
    private FavoriteResponse expectedfavoriteResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);

        LineRequest 신분당선_노선등록 = new LineRequest("신분당선", "bg-blue-600", 강남역.getId(), 양재역.getId(), 30);
        LineRequest 이호선_노선등록 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);
        LineRequest 삼호선_노선등록 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 50);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_노선등록).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_노선등록).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_노선등록).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 100);
        테스트계정 = new MemberRequest("probitanima11@gmail.com", "11", 10);
        MemberAcceptanceTest.회원_생성을_요청(테스트계정.getEmail(), 테스트계정.getPassword(), 테스트계정.getAge());

        ExtractableResponse<Response> correctAccountResponse = AuthAcceptanceTest.JWT_요청(테스트계정);
        accessJwt = MemberAcceptanceTest.JWT_받음(correctAccountResponse);

        favoriteRequest = FavoriteRequest.of(교대역.getId(), 양재역.getId());

        expectedfavoriteResponse = FavoriteResponse.of(1L, 교대역 , 양재역);
    }

    @DisplayName("로그인된 계정에 즐겨찾기가 추가된다.")
    @Test
    void create_favoriteForLoginUser() {
        // when
        ExtractableResponse<Response> createdFavoriteResponse = 즐겨찾기_생성_요청(accessJwt, favoriteRequest);

        // then
        즐겨찾기_생성됨(createdFavoriteResponse);
    }

    @DisplayName("로그인된 계정에 즐겨찾기가 조회된다.")
    @Test
    void search_favoriteForLoginUser() {
        // when
        ExtractableResponse<Response> createdFavoriteResponse = 즐겨찾기_생성_요청(accessJwt, favoriteRequest);
        // then
        즐겨찾기_생성됨(createdFavoriteResponse);

        // when
        ExtractableResponse<Response> searchedFavoriteResponse = 즐겨찾기_목록조회_요청(accessJwt);

        // then
        즐겨찾기_목록조회됨(searchedFavoriteResponse, expectedfavoriteResponse);
    }

    @DisplayName("로그인된 계정의 즐겨찾기가 삭제된다.")
    @Test
    void TotalFavoriteAcceptance() {
        // when
        ExtractableResponse<Response> createdFavoriteResponse = 즐겨찾기_생성_요청(accessJwt, favoriteRequest);
        // then
        즐겨찾기_생성됨(createdFavoriteResponse);

        // given
        ExtractableResponse<Response> searchedFavoriteResponse = 즐겨찾기_목록조회_요청(accessJwt);
        FavoriteResponse[] favoriteResponse = searchedFavoriteResponse.as(FavoriteResponse[].class);

        // when
        ExtractableResponse<Response> deletedFavoriteResponse = 즐겨찾기_삭제_요청(accessJwt, favoriteResponse[0]);

        // then
        즐겨찾기_삭제됨(deletedFavoriteResponse);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessJwt, FavoriteResponse favoriteResponse) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .when().delete("/favorites/" + favoriteResponse.getId().toString())
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록조회됨(ExtractableResponse<Response> response, FavoriteResponse expectedfavoriteResponse) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> Assertions.assertThat(response.as(FavoriteResponse[].class)[0]).isEqualTo(expectedfavoriteResponse)
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록조회_요청(String accessJwt) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> Assertions.assertThat(response.header("Location")).isEqualTo("/favorites/1")
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessJwt, FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
