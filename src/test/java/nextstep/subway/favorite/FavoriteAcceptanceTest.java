package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteResponses;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String FAVORITES_BASE_URL = "/favorites";
    public static final String ANOTHER_EMAIL = "another@github.com";

    private String token;
    private StationResponse 강남역;
    private StationResponse 광교역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, 28);
        token = 회원_로그인_됨(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createFavorite() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역, 광교역);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기를 목록을 조회한다.")
    @Test
    void getFavorites() {
        //given
        StationResponse 왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        StationResponse 건대역 = 지하철역_등록되어_있음("건대역").as(StationResponse.class);
        ExtractableResponse<Response> firstCreateFavoriteResponse = 즐겨찾기_생성되어_있음(강남역, 광교역);
        ExtractableResponse<Response> secondCreateFavoriteResponse = 즐겨찾기_생성되어_있음(왕십리역, 건대역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        즐겨찾기_조회됨(response);
        즐겨찾기_목록_포함됨(response, Arrays.asList(firstCreateFavoriteResponse, secondCreateFavoriteResponse));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        //given
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성되어_있음(강남역, 광교역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createFavoriteResponse, token);

        // then
        즐겨찾기_삭제됨(response);
    }

    @DisplayName("즐겨찾기를 삭제 시 회원이 일치하지 않는 경우 실패한다.")
    @Test
    void deleteFavoriteWithWrongMember() {
        //given
        회원_생성을_요청(ANOTHER_EMAIL, PASSWORD, 28);
        String anotherToken = 회원_로그인_됨(ANOTHER_EMAIL, PASSWORD);
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성되어_있음(강남역, 광교역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createFavoriteResponse, anotherToken);

        // then
        즐겨찾기_삭제_인증_실패(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .body(new FavoriteRequest(source.getId(), target.getId()))
                .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_BASE_URL)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                    .given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                    .when().get(FAVORITES_BASE_URL)
                    .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createFavoriteResponse, String token) {
        String uri = createFavoriteResponse.header("Location");

        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성되어_있음(StationResponse source, StationResponse target) {
        return  즐겨찾기_생성_요청(source, target);
    }

    public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedFavoriteIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultFavoriteIds = response.body().as(FavoriteResponses.class).getFavorites().stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultFavoriteIds).containsAll(expectedFavoriteIds);
    }
}
