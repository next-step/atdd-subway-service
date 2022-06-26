package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.fixture.AcceptanceTestSubwayFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private AcceptanceTestSubwayFixture 지하철;
    private String 로그인_인증_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성됨(
                MemberAcceptanceTest.회원_생성을_요청(
                        MemberAcceptanceTest.EMAIL,
                        MemberAcceptanceTest.PASSWORD,
                        MemberAcceptanceTest.AGE
                ));
        지하철 = new AcceptanceTestSubwayFixture();
        로그인_인증_토큰 = AuthAcceptanceTest
                .로그인_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD)
                .as(TokenResponse.class)
                .getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성_요청(
                로그인_인증_토큰, 지하철.강남역.getId(), 지하철.남부터미널역.getId());
        // then
        즐겨찾기_생성됨(createResponse1);

        // when
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성_요청(
                로그인_인증_토큰, 지하철.여의도역.getId(), 지하철.샛강역.getId());
        // then
        즐겨찾기_생성됨(createResponse2);

        // when
        ExtractableResponse<Response> findAllResponse1 = 즐겨찾기_목록_조회_요청(로그인_인증_토큰);
        // then
        즐겨찾기_목록_조회됨(findAllResponse1);
        // then
        즐겨찾기_목록_포함됨(findAllResponse1, Arrays.asList(createResponse1, createResponse2));

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(로그인_인증_토큰, createResponse1);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response,
                                   List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedFavoriteIds = createResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultFavoriteIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultFavoriteIds).containsAll(expectedFavoriteIds);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
