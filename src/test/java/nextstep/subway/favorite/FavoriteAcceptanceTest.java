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
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    StationResponse 소요산역;
    StationResponse 서울역;
    StationResponse 인천역;
    LineResponse 일호선;
    String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        MemberAcceptanceTest.회원_생성을_요청("justhis@gmail.com", "1234", 30);
        ExtractableResponse<Response> response = AuthAcceptanceTest.로그인_요청(new TokenRequest("justhis@gmail.com", "1234"));
        accessToken = MemberAcceptanceTest.getAccessToken(response);

        소요산역 = StationAcceptanceTest.지하철역_등록되어_있음("소요산역").as(StationResponse.class);
        서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
        인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);

        일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("일호선", "파란색", 소요산역.getId(), 인천역.getId(), 10)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 소요산역, 서울역, 5);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        ExtractableResponse<Response> insertResponse = 즐겨찾기_생성_요청(accessToken, 소요산역, 인천역);
        응답_코드_확인(insertResponse, HttpStatus.CREATED);
        즐겨찾기_등록_확인(insertResponse);

        ExtractableResponse<Response> selectResponse = 즐겨찾기_조회_요청(accessToken);
        응답_코드_확인(selectResponse, HttpStatus.OK);
        즐겨찾기_목록_확인(selectResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, insertResponse.header("Location"));
        응답_코드_확인(deleteResponse, HttpStatus.NO_CONTENT);
    }

    @DisplayName("Not Null 작동 확인")
    @Test
    void beanValidationTest() {
        인천역.setId(null);
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 소요산역, 인천역);
        응답_코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    private void 즐겨찾기_등록_확인(ExtractableResponse<Response> insertResponse) {
        assertThat(insertResponse.header("Location")).isEqualTo("/favorites/1");
    }

    private void 즐겨찾기_목록_확인(ExtractableResponse<Response> selectResponse) {
        List<FavoriteResponse> favoriteResponses = selectResponse.body().jsonPath().getList("$", FavoriteResponse.class);
        assertThat(favoriteResponses)
                .map(favoriteResponse -> asList(favoriteResponse.getSource().getName(), favoriteResponse.getTarget().getName()))
                .asList()
                .contains(asList("소요산역","인천역"));
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String location) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(location)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse 소요산역, StationResponse 인천역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(소요산역.getId(), 인천역.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all().extract();
    }

    private void 응답_코드_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
