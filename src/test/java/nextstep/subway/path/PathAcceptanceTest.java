package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    StationResponse 광교역;
    StationResponse 수지구청역;
    StationResponse 미금역;
    StationResponse 정자역;
    StationResponse 판교역;
    StationResponse 강남역;
    StationResponse 역삼역;
    StationResponse 종합운동장역;
    StationResponse 잠실새내역;
    StationResponse 잠실역;
    LineResponse 신분당선;
    LineResponse _2호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        수지구청역 = 지하철역_등록되어_있음("수지구청역").as(StationResponse.class);
        미금역 = 지하철역_등록되어_있음("미금역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        종합운동장역 = 지하철역_등록되어_있음("종합운동장역").as(StationResponse.class);
        잠실새내역 = 지하철역_등록되어_있음("잠실새내역").as(StationResponse.class);
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 30, 500)).as(LineResponse.class);
        _2호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-green-600", 강남역.getId(), 잠실역.getId(), 30, 200)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 판교역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 판교역, 정자역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 미금역, 3);
        지하철_노선에_지하철역_등록_요청(신분당선, 미금역, 수지구청역, 4);

        지하철_노선에_지하철역_등록_요청(_2호선, 강남역, 역삼역, 4);
        지하철_노선에_지하철역_등록_요청(_2호선, 역삼역, 종합운동장역, 5);
        지하철_노선에_지하철역_등록_요청(_2호선, 종합운동장역, 잠실새내역, 4);
    }

    /**
     * given 다수의 역과 노선 정보가 등록되어 있고,
     * when 출발역과 도착역을 지정해 경로를 조회하면
     * then 최단 거리로 이동할 수 있는 구간이 조회된다.
     */
    @Test
    @DisplayName("역과 역 간 최단경로 조회 - 환승포함")
    void shortPathTest(){
        // given - beforeEach

        // when
        ExtractableResponse<Response> 경로_요청_결과 = 경로_요청(광교역, 잠실역);

        // then
        PathResponse path = 경로_요청_결과.as(PathResponse.class);
        List<String> stationNames = 경로_지하철_역_이름(path);
        assertThat(stationNames).containsExactly(
                "광교역", "수지구청역", "미금역", "정자역", "판교역", "강남역", "역삼역", "종합운동장역", "잠실새내역", "잠실역"
        );
        assertThat(path.getDistance()).isEqualTo(60);
    }

    /**
     * given 다수의 역과 노선 정보가 등록되어 있고,
     * when 출발역과 도착역을 지정해 경로를 조회하면
     * then 최단 거리로 이동할 수 있는 구간이 조회된다.
     */
    @Test
    @DisplayName("역과 역 간 최단경로 조회 - 환승 미 포함")
    void shortPathTest2(){
        // given - beforeEach

        // when
        ExtractableResponse<Response> 경로_요청_결과 = 경로_요청(광교역, 미금역);

        // then
        PathResponse path = 경로_요청_결과.as(PathResponse.class);
        List<String> stationNames = 경로_지하철_역_이름(path);
        assertThat(stationNames).containsExactly(
                "광교역", "수지구청역", "미금역"
        );
        assertThat(path.getDistance()).isEqualTo(20);
    }

    /**
     * given: 지하철역/노선/구간 정보가 등록되어 있고,
     * when: 지하철 구간 최단경로를 조회하면
     * then: 구간 정보와 함께 요금도 조회된다.
     */
    @Test
    @DisplayName("역과 역 간 최단경로 조회 - 요금포함")
    void shortPathWithChargeTest(){
        // given - beforeEach

        // when
        ExtractableResponse<Response> 경로_요청_결과 = 경로_요청(광교역, 잠실역);

        // then
        PathResponse path = 경로_요청_결과.as(PathResponse.class);
        List<String> stationNames = 경로_지하철_역_이름(path);
        assertThat(stationNames).containsExactly(
                "광교역", "수지구청역", "미금역", "정자역", "판교역", "강남역", "역삼역", "종합운동장역", "잠실새내역", "잠실역"
        );
        assertThat(path.getDistance()).isEqualTo(60);
        assertThat(path.getCharge()).isEqualTo(2750);
    }

    private ExtractableResponse<Response> 경로_요청(StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    private List<String> 경로_지하철_역_이름(PathResponse path) {
        return path.getStations().stream()
                .map(
                        StationResponse::getName
                ).collect(Collectors.toList());
    }
}
