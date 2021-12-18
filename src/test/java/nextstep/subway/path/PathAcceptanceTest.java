package nextstep.subway.path;

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
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역 --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "red", 강남역, 양재역, 10, 300);
        이호선 = 지하철_노선_등록되어_있음("이호선", "green", 교대역, 강남역, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "orange", 교대역, 양재역, 5, 500);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        final String email = "test@test.com";
        final String password = "password";
        MemberAcceptanceTest.회원_생성을_요청(email, password, 13); // 13살 청소년에 대해 테스트
        accessToken = AuthAcceptanceTest.로그인_토큰_발급(email, password);
    }

    @Test
    void 최단_경로_조회() {
        // when
        final ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 남부터미널역, 강남역);

        // then
        StatusCodeCheckUtil.ok(response);
        최단_경로_응답됨(response, 남부터미널역, 양재역, 강남역);
        최단_거리_응답됨(response, 12);
        요금_응답됨(response, 1200); // 13살 청소년, 3호선 추가운임 500원, 신분당선 추가운임 300원 -> (1250 + 100 + 500 - 350) * 0.8 = 1200
    }

    private StationResponse 지하철역_등록되어_있음(String name) {
        return StationAcceptanceTest.지하철역_생성_요청(name).as(StationResponse.class);
    }

    private LineResponse 지하철_노선_등록되어_있음(
        String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare
    ) {
        final LineRequest lineRequest =
            new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(
        LineResponse line, StationResponse upStation, StationResponse downStation, int distance
    ) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(
        final String accessToken, final StationResponse source, final StationResponse target
    ) {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("source", source.getId());
        queryParams.put("target", target.getId());

        return RequestUtil.getWithAccessToken("/paths", accessToken, queryParams);
    }

    private void 최단_경로_응답됨(final ExtractableResponse<Response> response, final StationResponse... expectedStations) {
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactly(expectedStations);
    }

    private void 최단_거리_응답됨(final ExtractableResponse<Response> response, final int expectedDistance) {
        final int distance = response.jsonPath().getObject("distance", Integer.class);
        assertThat(distance).isEqualTo(expectedDistance);
    }

    private void 요금_응답됨(final ExtractableResponse<Response> response, final int expectedFare) {
        final Integer fare = response.jsonPath().getObject("fare", Integer.class);
        assertThat(fare).isEqualTo(expectedFare);
    }
}
