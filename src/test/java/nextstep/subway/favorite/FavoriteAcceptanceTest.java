package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
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

    private StationResponse 강남역;
    private StationResponse 광교역;

    private LineRequest lineRequest;

    private String 인증토큰;
    private String 다른사람의_인증토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        LineAcceptanceTest.지하철_노선_생성_요청(lineRequest);

        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        TokenRequest 로그인_요청 = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        ExtractableResponse<Response> 로그인_요청_결과 = AuthAcceptanceTest.로그인_요청(로그인_요청);

        인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(로그인_요청_결과);

        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD, MemberAcceptanceTest.AGE);
        TokenRequest 다른사람의_로그인_요청 = new TokenRequest(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD);
        ExtractableResponse<Response> 다른사람의_로그인_요청_결과 = AuthAcceptanceTest.로그인_요청(다른사람의_로그인_요청);

        인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(로그인_요청_결과);
        다른사람의_인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(다른사람의_로그인_요청_결과);
    }

    @DisplayName("즐겨찾기를 추가해보자")
    @Test
    void createFavorite() {
        //When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(인증토큰, 강남역.getId(), 광교역.getId());
        //Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);
    }

    @DisplayName("즐겨찾기 목록을 조회해보자")
    @Test
    void findFavorites() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(인증토큰, 강남역.getId(), 광교역.getId());
        즐겨찾기_생성됨(createResponse);

        //When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(인증토큰);
        //Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(listResponse);
    }

    @DisplayName("즐겨찾기를 삭제해보자")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(인증토큰, 강남역.getId(), 광교역.getId());
        즐겨찾기_생성됨(createResponse);

        //When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(인증토큰, createResponse);
        //Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("다른사람의 즐겨찾기는 삭제가 불가능하다")
    @Test
    void unableDeleteFavoriteOtherUser() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(인증토큰, 강남역.getId(), 광교역.getId());
        즐겨찾기_생성됨(createResponse);
        ExtractableResponse<Response> otherCreateResponse = 즐겨찾기_생성_요청(다른사람의_인증토큰, 강남역.getId(), 광교역.getId());
        즐겨찾기_생성됨(otherCreateResponse);

        //When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(인증토큰, otherCreateResponse);
        //Then 즐겨찾기 삭제실패
        즐겨찾기_삭제실패됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("favorites")
                .then().log().all()
                .extract();

    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }



    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}