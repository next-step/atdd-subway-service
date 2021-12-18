package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final TokenRequest TOKEN_REQUEST = new TokenRequest(
        "email@email.com",
        "password"
    );

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 토큰;

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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword(), 20);
        토큰 = 로그인_되어_있음(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword())
            .as(TokenResponse.class)
            .getAccessToken();
    }

    @DisplayName("지하철 최단 경로를 조회한다.")
    @Test
    void getPath() {
        // given
        final PathRequest pathRequest = new PathRequest(강남역.getId(), 양재역.getId());

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(토큰, pathRequest);

        // then
        지하철_최단_경로_응답됨(response);

        final PathResponse pathResponse = response.jsonPath()
            .getObject(".", PathResponse.class);
        지하철_최단_경로_일치함(pathResponse);
        지하철_최단_거리_일치함(pathResponse);
        지하철_이용_요금_일치함(pathResponse);
    }

    private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(
        final String token,
        final PathRequest params
    ) {
        return RestAssuredUtil.getWithAuth(token, params, "/paths");
    }

    private void 지하철_최단_경로_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_최단_경로_일치함(final PathResponse pathResponse) {
        final List<String> stationNames = pathResponse.getStations()
            .stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(강남역.getName(), 양재역.getName());
    }

    private void 지하철_최단_거리_일치함(final PathResponse pathResponse) {
        assertThat(pathResponse.getDistance()).isEqualTo(10);
    }

    private void 지하철_이용_요금_일치함(final PathResponse pathResponse) {
        assertThat(pathResponse.getFare()).isEqualTo(BigDecimal.valueOf(2_500L));
    }
}
