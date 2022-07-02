package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_발급_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.utils.RestAssuredUtils.delete;
import static nextstep.subway.utils.RestAssuredUtils.get;
import static nextstep.subway.utils.RestAssuredUtils.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String FAVORITES_API_BASE_URL = "/favorites";
    private static String 인증_토큰;
    private StationResponse 선정릉역, 선릉역, 도곡역, 한티역;
    private LineResponse 분당선;

    /**
     * @Given 지하철역 등록되어 있음
     * @And 지하철 노선 등록되어 있음
     * @And 지하철 노선에 지하철역 등록되어 있음
     * @And 회원 등록되어 있음
     * @And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철역_생성();
        노선_생성();
        구간_생성();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        인증_토큰 = 토큰_발급_요청(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨 찾기 관리 기능")
    public void manageFavorites() {
        // When : 즐겨찾기 생성 요청
        ExtractableResponse<Response> createFirstFavoritesResponse = 즐겨찾기_생성_요청(인증_토큰, 선정릉역, 도곡역);
        즐겨찾기_생성_요청(인증_토큰, 선릉역, 도곡역);

        // Then : 즐겨 찾기 생성됨
        즐겨찾기_생성됨(createFirstFavoritesResponse);

        // When : 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(인증_토큰);

        // Then : 즐겨 찾기 목록 조회됨
        즐겨찾기_목록_조회됨(findResponse, 선정릉역, 도곡역, 선릉역, 도곡역);

        // When : 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteFirstFavoritesResponse = 즐겨찾기_삭제_요청(인증_토큰, createFirstFavoritesResponse);

        // Then : 즐겨 찾기 삭제됨
        즐겨찾기_삭제됨(deleteFirstFavoritesResponse);

        // When : 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> retrieveResponse = 즐겨찾기_목록_조회_요청(인증_토큰);

        // Then : 즐겨 찾기 목록 조회됨
        즐겨찾기_목록_조회됨(retrieveResponse, 선릉역, 도곡역);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(
        final String accessToken,
        final StationResponse source,
        final StationResponse target
    ) {
        FavoritesRequest favoritesRequest = new FavoritesRequest(source.getId(), target.getId());
        return post(accessToken, FAVORITES_API_BASE_URL, favoritesRequest);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return get(accessToken, FAVORITES_API_BASE_URL);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
        final String accessToken,
        ExtractableResponse<Response> response
    ) {
        final String uri = response.header("Location");
        return delete(accessToken, uri);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(
        ExtractableResponse<Response> response,
        final StationResponse... stations
    ) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> actualIds = response.jsonPath().getList(".", FavoritesResponse.class).stream()
            .map(it -> Arrays.asList(it.getSource().getId(), it.getTarget().getId()))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        List<Long> expectedIds = Arrays.stream(stations)
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철역_생성() {
        선정릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선정릉역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        한티역 = StationAcceptanceTest.지하철역_등록되어_있음("한티역").as(StationResponse.class);
        도곡역 = StationAcceptanceTest.지하철역_등록되어_있음("도곡역").as(StationResponse.class);
    }

    private void 노선_생성() {
        분당선 = 지하철_노선_등록되어_있음(new LineRequest("분당선", "yellow", 선정릉역.getId(), 도곡역.getId(), 19)).as(LineResponse.class);
    }

    private void 구간_생성() {
        구간_추가(분당선, 선정릉역, 선릉역, 4);
        구간_추가(분당선, 선릉역, 한티역, 9);
    }

    private void 구간_추가(LineResponse 노선, StationResponse 상행역, StationResponse 하행역, int 구간_거리) {
        지하철_노선에_지하철역_등록_요청(노선, 상행역, 하행역, 구간_거리);
    }
}
