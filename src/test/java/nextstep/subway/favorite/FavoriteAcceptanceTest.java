package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 사호선;
    private StationResponse 안산역;
    private StationResponse 서울역;
    private StationResponse 중앙역;
    private String 회원;
    private String 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        String email = "member@member.com";
        String password = "password";

        안산역 = 지하철역_등록되어_있음("안산역").as(StationResponse.class);
        서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
        중앙역 = 지하철역_등록되어_있음("중앙역").as(StationResponse.class);
        사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-blue-600", 안산역.getId(), 서울역.getId(), 100)).as(LineResponse.class);
        회원 = 회원_등록되어_있음(email, password, 20).header("Location").split("/")[2];
        토큰 = 로그인_되어_있음(new TokenRequest(email, password));
        지하철_노선에_지하철역_등록되어_있음(사호선, 서울역, 중앙역, 80);
    }

    @Test
    @DisplayName("즐겨찾기 관리")
    public void manageFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(Long.parseLong(회원), 서울역.getId(), 안산역.getId());
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(favoriteRequest, 토큰);
        // then
        즐겨찾기_생성_됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(favoriteRequest, 토큰);
        // then
        즐겨찾기_목록_조회_됨(listResponse);

        // given
        String deleteId = createResponse.header("Location").split("/")[2];
        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_목록_삭제_요청(deleteId, 토큰);
        // then
        즐겨찾기_목록_삭제_됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(FavoriteRequest favoriteRequest, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(FavoriteRequest favoriteRequest, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(String deleteId, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().delete("/favorites/" + deleteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_목록_삭제_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}