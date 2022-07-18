package nextstep.subway.favorite;

import static nextstep.subway.line.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.path.PathAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private String 사용자_토큰;


    @BeforeEach
    public void setUp() {
        //given 회원 등록
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 10);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(사용자_토큰, 강남역, 정자역);
        //then
        즐겨찾기_생성_성공(response);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void getFavorites() {
        //given
        즐겨찾기_생성_요청(사용자_토큰, 강남역, 정자역);
        //when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(사용자_토큰);
        //then
        즐겨찾기_조회_성공(response);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        //given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자_토큰, 강남역, 정자역);
        //when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(사용자_토큰, createResponse);
        //then
        즐겨찾기_삭제_성공(response);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }


    public static void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}