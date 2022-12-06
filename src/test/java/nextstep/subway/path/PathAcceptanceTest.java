package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthFixture.로그인_요청_후_토큰_가져오기;
import static nextstep.subway.line.acceptance.LineFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionFixture.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberFixture.EMAIL;
import static nextstep.subway.member.MemberFixture.PASSWORD;
import static nextstep.subway.member.MemberFixture.TEENAGER_AGE;
import static nextstep.subway.member.MemberFixture.회원_생성을_요청;
import static nextstep.subway.path.PathFixture.지하철_출발역에서_도착역_최단경로_조회;
import static nextstep.subway.station.StationFixture.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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

    /*
     * Feature: 지하철 경로 관련 기능
     * Background
     * Given 지하철역 등록되어 있음
     * AND 지하철 노선 등록되어 있음
     * AND 지하철 노선에 지하철 역 등록되어 있음
     * AND 청소년 로그인 중
     * Scenario
     * WHEN 지하철 출발역에서 도착역 최단경로 조회
     * THEN 지하철 출발역에서 도착역 최단경로 목록 조회됨
     * And 총 거리도 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, TEENAGER_AGE);
        accessToken = 로그인_요청_후_토큰_가져오기(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("시나리오1 - 지하철 출발역에서 도착역 최단경로 조회")
    void scenario1() {
        //WHEN
        PathResponse response = 지하철_출발역에서_도착역_최단경로_조회(accessToken, 강남역, 남부터미널역)
            .as(PathResponse.class);

        //then
        지하철_출발역에서_도착역_최단경로_목록_조회됨(response);
        지하철_출발역에서_도착역_총거리_조회됨(response);
        지하철_출발역에서_도착역_이용_요금도_조회됨(response);
    }

    private void 지하철_출발역에서_도착역_최단경로_목록_조회됨(PathResponse response) {
        assertThat(response.getStations()
            .stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList())
        )
            .hasSize(3)
            .containsExactly("강남역", "양재역", "남부터미널역");
        assertThat(response.getDistance())
            .isEqualTo(12);
    }

    private void 지하철_출발역에서_도착역_총거리_조회됨(PathResponse response) {
        assertThat(response.getDistance())
            .isEqualTo(12);
    }

    private void 지하철_출발역에서_도착역_이용_요금도_조회됨(PathResponse response) {
        assertThat(response.getFare())
            .isEqualTo(1520);
    }
}
