package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.AuthSteps.*;
import static nextstep.subway.line.LineSectionSteps.*;
import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.path.PathSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 경로 찾기 관련 기능 인수 테스트")
public class PathAcceptanceTest extends AcceptanceTest {
    
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 부산역;
    private String accessToken;

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

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        부산역 = 지하철역_등록되어_있음("부산역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음("email@email.com", "password", 20);
        accessToken = 로그인_되어_있음("email@email.com", "password");
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 강남역, 남부터미널역);

        // then
        최단_경로_응답됨(response);
    }

    @Test
    @DisplayName("동일한 역으로 최단 경로를 조회한다.")
    void findShortestPathWithSameStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 강남역, 강남역);

        // then
        최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("연결되어 있지 않은 역으로 최단 경로를 조회한다.")
    void findShortestPathWithNotContainedStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 강남역, 부산역);

        // then
        최단_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않는 역으로 최단 경로를 조회한다.")
    void findShortestPathWithNotExistsStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 100L, 200L);

        // then
        최단_경로_응답_지하철역_조회_실패됨(response);
    }

    private void 최단_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isGreaterThan(0);
        assertThat(response.jsonPath().getInt("fare")).isGreaterThan(0);
    }

    private void 최단_경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 최단_경로_응답_지하철역_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
