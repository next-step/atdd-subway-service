package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    StationResponse 삼전역;
    StationResponse 잠실새내역;
    StationResponse 종합운동장역;

    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;
    LineResponse 구호선;

    /**
     * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
     * <p>
     * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
     * |                        |                                                        |
     * |                        |                                                        |
     * 이호선(10)                구호선(1)                                                  삼호선(100)
     * |                        |                                                        |
     * V                        V                                                        V
     * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        삼전역 = StationAcceptanceTest.지하철역_등록되어_있음("삼전역").as(StationResponse.class);
        잠실새내역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실새내역").as(StationResponse.class);
        종합운동장역 = StationAcceptanceTest.지하철역_등록되어_있음("종합운동장역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 1);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-700", 강남역, 잠실새내역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-800", 교대역, 남부터미널역, 1);
        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-900", 양재역, 종합운동장역, 1);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역, 1);
        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내역, 종합운동장역, 10);
        지하철_노선에_지하철역_등록_요청(이호선, 종합운동장역, 삼전역, 1);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 삼전역, 100);
        지하철_노선에_지하철역_등록_요청(구호선, 종합운동장역, 삼전역, 1);
    }


    @DisplayName("최단 경로 조회 성공")
    @Test
    void 최단_경로_조회() {
        //when
        PathResponse response = 최단_경로_조회_요청(강남역, 삼전역).as(PathResponse.class);

        assertThat(response.getStations().stream().map(station -> station.getId())).containsSequence(
                강남역.getId(), 양재역.getId(), 종합운동장역.getId(), 삼전역.getId());
    }

    @DisplayName("최단 경로 조회 실패 - 출발역과 도착역이 같은 경우")
    @Test
    void 최단_경로_조회_출발역과_도착역이_같은_경우() {
        //when, then:
        assertThat(최단_경로_조회_요청(강남역, 강남역).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("최단 경로 조회 실패 - 존재하지 않는 지하철역을 조회할 경우")
    @Test
    void 최단_경로_조회_존재하지_않는_지하철역을_조회할_경우() {
        //given
        Station 등록되지_않은_역 = new Station(9999L, "등록되지 않은 역");

        //when,then
        assertThat(최단_경로_조회_요청(강남역.getId(), 등록되지_않은_역.getId()).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("최단 경로 조회 실패 - 출발역과 도착역이 연결되지 않은 경우")
    @Test
    void 최단_경로_조회_출발역과_도착역이_연결되지_않은_경우() {
        //when, then:
        assertThat(최단_경로_조회_요청(잠실새내역, 남부터미널역).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse 출발역, StationResponse 도착역) {
        return 최단_경로_조회_요청(출발역.getId(), 도착역.getId());

    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(long 출발역_id, long 도착역_id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", 출발역_id)
                .param("target", 도착역_id)
                .when().get("paths")
                .then().log().all()
                .extract();

    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest params = new LineRequest(name,color,upStation.getId(),downStation.getId(),distance);
        return 지하철_노선_생성_요청(params).as(LineResponse.class);
    }
}
