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

    private String token;
    private StationResponse 강남역;
    private StationResponse 광교역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD,28);
        token = 회원_로그인_됨(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createFavorite() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(new FavoriteRequest(강남역.getId(), 광교역.getId()));

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기를 목록을 조회한다.")
    @Test
    void getFavorites() {
        //given
        StationResponse 왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        StationResponse 건대역 = 지하철역_등록되어_있음("건대역").as(StationResponse.class);
        ExtractableResponse<Response> 첫번쨰_즐겨찾기 = 즐겨찾기_생성되어_있음(new FavoriteRequest(강남역.getId(), 광교역.getId()));
        ExtractableResponse<Response> 두번째_즐겨찾기 = 즐겨찾기_생성되어_있음(new FavoriteRequest(왕십리역.getId(), 건대역.getId()));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        즐겨찾기_조회됨(response);
        즐겨찾기_목록_포함됨(response, Arrays.asList(첫번쨰_즐겨찾기, 두번째_즐겨찾기));
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                    .given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                    .when().get("/favorites")
                    .then().log().all().extract();
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성되어_있음(FavoriteRequest request) {
        return  즐겨찾기_생성_요청(request);
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
