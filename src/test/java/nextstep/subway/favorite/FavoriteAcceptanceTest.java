package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.AcceptanceTestUtil.delete;
import static nextstep.subway.utils.AcceptanceTestUtil.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.AcceptanceTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;
    private TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
            10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        회원_생성을_요청("email@email.com", "password", 20);
        토큰 = 로그인_요청("email@email.com", "password").as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void favorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(토큰);
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(토큰, 즐겨찾기_생성_응답);
        // then
        즐겨찾기_목록_삭제됨(즐겨찾기_삭제_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제후_목록_조회_응답 = 즐겨찾기_목록_조회_요청(토큰);
        // then
        삭제후_즐겨찾기_목록_비어있음(즐겨찾기_삭제후_목록_조회_응답);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse 토큰,
        StationResponse 강남역, StationResponse 양재역) {
        return AcceptanceTestUtil.post("/favorites",
            토큰.getAccessToken(), new FavoriteRequest(강남역.getId(), 양재역.getId()));
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse 토큰) {
        return get("/favorites", 토큰.getAccessToken());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 토큰,
        ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri, 토큰.getAccessToken());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = Lists.newArrayList(
            response.as(FavoriteResponse[].class));

        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses).extracting(r -> r.getSource().getId(),
                r -> r.getTarget().getId())
            .contains(tuple(강남역.getId(), 양재역.getId()));
    }

    private void 즐겨찾기_목록_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 삭제후_즐겨찾기_목록_비어있음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(FavoriteResponse[].class)).isEmpty();
    }

}