package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 판교역.getId(), 10);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);

        MemberAcceptanceTest.회원_생성_요청(EMAIL, PASSWORD, AGE);

        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다.")
    public void manageFavorite() throws Exception {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성을_요청함(강남역.getId(), 판교역.getId());

        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청함();

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(즐겨찾기_생성_응답);

        // then
        즐겨찾기_삭제됨(response);
    }

    private void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(final ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(HttpHeaders.LOCATION);

        return delete(uri, accessToken);
    }

    private void 즐겨찾기_목록_조회됨(final ExtractableResponse<Response> 즐겨찾기_목록_조회_응답) {
        assertThat(즐겨찾기_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청함() {
        return get("/favorites", accessToken);
    }

    private void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청함(final Long sourceStationId, final Long targetStationId) {
        return post("/favorites", new FavoriteRequest(sourceStationId, targetStationId), accessToken);
    }

    private String 로그인_되어_있음(final String email, final String password) {
        ExtractableResponse<Response> 로그인_응답 = AuthAcceptanceTest.로그인_요청(new TokenRequest(email, password));
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse 로그인_정보 = 로그인_응답.as(TokenResponse.class);

        return 로그인_정보.getAccessToken();
    }
}
