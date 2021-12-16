package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final TokenRequest TOKEN_REQUEST = new TokenRequest(
        "email@email.com",
        "password"
    );

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 판교역;
    private StationResponse 정자역;
    private String 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 판교역, 4);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 판교역, 정자역, 1);

        회원_등록되어_있음(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword(), 20);
        토큰 = 로그인_되어_있음(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword())
            .as(TokenResponse.class)
            .getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(
            토큰,
            강남역.getId(),
            정자역.getId()
        );
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        final ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(토큰);
        // then
        즐겨찾기_목록_조회됨(listResponse);

        // when
        final ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(
            토큰,
            createResponse.as(FavoriteResponse.class).getId()
        );
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성_요청(
        final String token,
        final Long sourceId,
        final Long targetId
    ) {
        final FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        return 즐겨찾기_생성_요청(token, favoriteRequest);
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성_요청(
        final String token,
        final FavoriteRequest favoriteRequest
    ) {
        return RestAssuredUtil.postWithAuth(token, favoriteRequest, "/favorites");
    }

    private static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String token) {
        return RestAssuredUtil.getWithAuth(token, "/favorites");
    }

    private static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
        final String token,
        final Long id
    ) {
        return RestAssuredUtil.deleteWithAuth(token, "/favorites/" + id);
    }

    private void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
