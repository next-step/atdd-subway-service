package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_생성이_요청됨;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String FAVORITES_PATH = "/favorites";
    private StationResponse 교대역;
    private StationResponse 양재역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_생성이_요청됨(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
    }

    @DisplayName("즐겨찾기 정보를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, 교대역, 양재역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(accessToken);
        // then
        즐겨찾기_정보_조회됨(findResponse, 교대역, 양재역);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, findResponse.as(FavoriteResponse.class).getId());
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource()).isEqualTo(source);
        assertThat(favoriteResponse.getTarget()).isEqualTo(target);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
        return delete(accessToken, FAVORITES_PATH, id);
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return get(accessToken, FAVORITES_PATH);
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, StationResponse sourceStation,
        StationResponse targetStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        return post(accessToken, favoriteRequest, FAVORITES_PATH);
    }

}
