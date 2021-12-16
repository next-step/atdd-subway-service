package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        final String email = "mslim@naver.com";
        final String password = "password";
        MemberAcceptanceTest.회원_등록되어_있음(email, password, 20);

        accessToken = AuthAcceptanceTest.로그인_토큰_발급(email, password);
    }

    @Test
    void 즐겨찾기를_관리() {
        // when
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 양재역.getId());

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        final ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        final List<FavoriteResponse> favorites = 즐겨찾기_목록_조회됨(findResponse, 강남역, 양재역);

        // when
        final ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, favorites.get(0).getId());

        // then
        즐겨찾기_삭제됨(accessToken, deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(
        final String accessToken, final Long source, final Long target
    ) {
        final Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RequestUtil.postWithAccessToken("/favorites", accessToken, params);
    }

    public static void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/favorites", accessToken);
    }

    public static List<FavoriteResponse> 즐겨찾기_목록_조회됨(
        final ExtractableResponse<Response> response, final StationResponse source, final StationResponse target
    ) {
        StatusCodeCheckUtil.ok(response);
        final List<FavoriteResponse> favorites = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites.get(0).getId()).isNotNull();
        assertThat(favorites.get(0).getSource()).isEqualTo(source);
        assertThat(favorites.get(0).getTarget()).isEqualTo(target);

        return favorites;
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken, final Long id) {
        return RequestUtil.deleteWithAccessToken("/favorites/{favoriteId}", accessToken, id);
    }

    public static void 즐겨찾기_삭제됨(final String accessToken, final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.noContent(response);
        final List<FavoriteResponse> favorites =
            즐겨찾기_목록_조회_요청(accessToken).jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorites).isEmpty();
    }
}
