package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인되어있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.springframework.http.HttpStatus.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 방화역;
    private StationResponse 청구역;
    private StationResponse 미사역;
    private StationResponse 하남검단산역;
    private LineResponse 오호선;
    private TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        방화역 = 지하철역_등록되어_있음("방화역").as(StationResponse.class);
        청구역 = 지하철역_등록되어_있음("청구역").as(StationResponse.class);
        미사역 = 지하철역_등록되어_있음("미사역").as(StationResponse.class);
        하남검단산역 = 지하철역_등록되어_있음("하남검단산역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("오호선", "보라색", 방화역.getId(), 하남검단산역.getId(), 10);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(오호선, 방화역, 청구역, 3);
        지하철_노선에_지하철역_등록되어_있음(오호선, 청구역, 미사역, 3);

        회원_생성_되어있음(EMAIL, PASSWORD, AGE);
        토큰 = 로그인되어있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    public void manageFavorite() throws Exception {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청();

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> favoritesResponse = 즐겨찾기목록_조회_요청();

        // then
        즐겨찾기목록_조회됨(favoritesResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse);

        // then
        즐겨찾기_삭제됨(response);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, NO_CONTENT);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse) {
        return delete(createResponse.header("Location"), 토큰.getAccessToken());
    }

    private void 즐겨찾기목록_조회됨(ExtractableResponse<Response> favoritesResponse) {
        assertHttpStatus(favoritesResponse, OK);
    }

    private ExtractableResponse<Response> 즐겨찾기목록_조회_요청() {
        return get("/favorites", 토큰.getAccessToken());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertHttpStatus(createResponse, CREATED);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청() {
        return post("/favorites", new FavoriteRequest(방화역.getId(), 하남검단산역.getId()), 토큰.getAccessToken());
    }
}