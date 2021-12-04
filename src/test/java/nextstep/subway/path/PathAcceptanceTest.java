package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthFactory;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.FavoriteAcceptanceTest;
import nextstep.subway.line.acceptance.LineFactory;
import nextstep.subway.line.acceptance.LineSectionFactory;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationFactory;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;



@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private static String token;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationFactory.지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = StationFactory.지하철역_생성_요청("양재역").as(StationResponse.class);
        교대역 = StationFactory.지하철역_생성_요청("교대역").as(StationResponse.class);
        남부터미널역 = StationFactory.지하철역_생성_요청("남부터미널역").as(StationResponse.class);

        신분당선 = LineFactory.지하철_노선_생성_요청(
                        new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0)
                        ).as(LineResponse.class);
        이호선 = LineFactory.지하철_노선_생성_요청(
                         new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0)
                        ).as(LineResponse.class);
        삼호선 = LineFactory.지하철_노선_생성_요청(
                        new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)
                        ).as(LineResponse.class);

        LineSectionFactory.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        ExtractableResponse<Response> createResponse = AuthFactory.회원_생성을_요청(new MemberRequest(EMAIL, PASSWORD, AGE));
        FavoriteAcceptanceTest.회원_생성됨(createResponse);

        ExtractableResponse<Response> tokenResponse = AuthFactory.토큰을_요청함(new TokenRequest(EMAIL, PASSWORD));
        token = tokenResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void getShortestPath() {

        ExtractableResponse<Response> response = PathFactory.최단_경로를_조회(강남역.getId(), 남부터미널역.getId(), token);
        최단_경로를_조회하여_비교(response, 12);
    }

    private void 최단_경로를_조회하여_비교(ExtractableResponse response, int distance) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

}
