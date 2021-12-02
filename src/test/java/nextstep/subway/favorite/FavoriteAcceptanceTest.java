package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthFactory;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineFactory;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationFactory;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private static String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationFactory.지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = StationFactory.지하철역_생성_요청("양재역").as(StationResponse.class);
        정자역 = StationFactory.지하철역_생성_요청("정자역").as(StationResponse.class);
        광교역 = StationFactory.지하철역_생성_요청("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0);
        신분당선 = LineFactory.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> createResponse = AuthFactory.회원_생성을_요청(new MemberRequest(EMAIL, PASSWORD, AGE));
        회원_생성됨(createResponse);

        ExtractableResponse<Response> tokenResponse = AuthFactory.토큰을_요청함(new TokenRequest(EMAIL, PASSWORD));
        token = tokenResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("시나리오 테스트")
    @Test
    void scenarioTest() {
        ExtractableResponse<Response> createResponse = FavoriteFactory.즐겨찾기_생성_요청(강남역.getId(), 광교역.getId(), token);
        즐겨찾기_생성_확인(createResponse);

        ExtractableResponse<Response> getResponse = FavoriteFactory.즐겨찾기_목록_조회_요청(token);
        즐겨찾기_목록_조회_확인(getResponse);

        ExtractableResponse<Response> deleteResponse = FavoriteFactory.즐겨찾기_삭제_요청(createResponse, token);
        즐겨찾기_삭제_확인(deleteResponse);

    }

    @DisplayName("즐겨찾기 생성요청 에러 - 없는 역일 때")
    @Test
    void addFavoritesError() {
        Long source = 99L;
        Long target = 98L;
        ExtractableResponse<Response> createResponse = FavoriteFactory.즐겨찾기_생성_요청(source, target, token);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_삭제_확인(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_목록_조회_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 즐겨찾기_생성_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}