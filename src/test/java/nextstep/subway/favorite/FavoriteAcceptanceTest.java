package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static StationResponse 강남역;
    private static StationResponse 양재역;
    private static StationResponse 정자역;
    private static StationResponse 광교역;
    private static LineResponse 신분당선;
    private static ExtractableResponse<Response> 김석진;
    private static TokenResponse 사용자_토큰;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        //given
        //지하철역 등록되어 있음
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        //지하철 노선이 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        //지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 20);

        //회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //로그인 되어있음
        사용자_토큰 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관린한다. - 성공")
    @Test
    void success() {

        //When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자_토큰, 강남역, 광교역);

        //Then 즐겨찾기 생성됨
        즐겨찾기가_생성됨(createResponse);

        //When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(사용자_토큰);

        //Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록이_조회됨(response);

        //When 즐겨찾기 삭제 요청
        response = 즐겨찾기_삭제_요청(사용자_토큰, createResponse);

        //Then 즐겨찾기 삭제됨
        즐겨찾기가_삭제됨(response);
    }

    @DisplayName("즐겨찾기를 관린한다. - 실패")
    @Test
    void fail() {
        //given
        //유효하지 않는 토큰
        TokenResponse 유효하지_않은_토큰 = new TokenResponse("non");
        //즐겨찾기가 1개 등록되어있다
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자_토큰, 강남역, 광교역);

        //When 유효하지 않는 토큰으로 생성을 요청
        ExtractableResponse<Response> createResponseNo = 즐겨찾기_생성을_요청(유효하지_않은_토큰, 강남역, 광교역);

        //Then 실패함
        권한_없음(createResponseNo);

        //When 유효하지 않는 토큰으로 목록 조회 요청
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(유효하지_않은_토큰);

        //Then 실패함
        권한_없음(response);

        //When 유효하지 않는 토큰으로 삭제요청
        response = 즐겨찾기_삭제_요청(유효하지_않은_토큰, createResponse);

        //Then 실패함
        권한_없음(response);

        //When 등록되지 않은 즐겨찾기를 삭제한다.
        response = 즐겨찾기_삭제_요청(사용자_토큰, "/favorites/10");

        //Then 실패한다.
        응답_실패(response);

    }

    private void 권한_없음(ExtractableResponse<Response> response) {
        // TODO Auto-generated method stub
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 응답_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기가_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse,
            ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, String uri) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록이_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponses.get(0).getId()).isEqualTo(신분당선.getId());
    }

    public static void 즐겨찾기가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse tokenResponse, StationResponse source,
            StationResponse target) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

}