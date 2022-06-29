package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 한국역;
    private StationResponse 광화문역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역 ---  한국역 ----- 광화문역
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

        한국역 = StationAcceptanceTest.지하철역_등록되어_있음("한국역").as(StationResponse.class);
        광화문역 = StationAcceptanceTest.지하철역_등록되어_있음("광화문역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 한국역, 40);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 한국역, 광화문역, 100);
    }


/*    Scenario: 두 역의 최단 거리 경로를 조회
      Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        - 출발역에서 도착역까지의 최단 거리(기본금액) 구한다
            When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
            Then 최단 거리 경로를 응답
            And 총 거리도 함께 응답함
            And 지하철 이용 요금도 함께 응답함
        - 출발역에서 도착역까지의 최단 거리(초과금액) 구한다
            When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
            Then 최단 거리 경로를 응답
            And 총 거리도 함께 응답함
            And 지하철 이용 요금도 함께 응답함
        - 출발역에서 도착역까지의 최단 거리(장거리) 구한다
            When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
            Then 최단 거리 경로를 응답
            And 총 거리도 함께 응답함
            And 지하철 이용 요금도 함께 응답함
*/
    @TestFactory
    @DisplayName("지하철 경로 검색")
    Stream<DynamicTest> subwayPathSearch() {
        return Stream.of(
            dynamicTest("출발역에서 도착역까지의 최단 거리(기본금액) 구한다", () -> {
                //when
                ExtractableResponse<Response> response = 최단거리_경로를_구한다(교대역, 양재역);
                //then
                최단경로와_거리_요금이_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역),5 ,1_550);
            }),
            dynamicTest("출발역에서 도착역까지의 최단 거리(초과금액) 구한다", () -> {
                //when
                ExtractableResponse<Response> response = 최단거리_경로를_구한다(양재역, 한국역);
                //then
                최단경로와_거리_요금이_조회됨(response, Arrays.asList(양재역, 강남역, 한국역), 50, 2_250);
            }),
            dynamicTest("출발역에서 도착역까지의 최단 거리(장거리) 구한다", () -> {
                //when
                ExtractableResponse<Response> response = 최단거리_경로를_구한다(양재역, 광화문역);
                //then
                최단경로와_거리_요금이_조회됨(response, Arrays.asList(양재역, 강남역, 한국역, 광화문역), 150, 3_550);
            })
        );




    }



    /**
     * when 출발역과 도착역이 같은 최단거리를 구하면
     * then 찾을수 없다.
     * */
    @Test
    @DisplayName("출발역과 도착역이 같은 경우 찾을 수 없다.")
    void startLastStationSameNotFound() {
        //when
        ExtractableResponse<Response> response = 최단거리_경로를_구한다(교대역, 교대역);

        //then 찾을수 없다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when 출발역과 도착역이 연결 되어 있지 않은 경우의 최단거리를 구하면
     * then 찾을수 없다.
     * */
    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void notLinkSection() {
        StationResponse 신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
        StationResponse 여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        LineResponse 오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 신길역, 여의도역, 5, 100);


        //when
        ExtractableResponse<Response> response = 최단거리_경로를_구한다(신길역, 남부터미널역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * given 구간에 연결되지 않은 역.
     * when 최단거리 경로를 구하면
     * then 찾을수 없다
     * */
    @Test
    @DisplayName("구간의 역이 존재하지 않은 경우")
    void notFoundStation() {
        //given
        StationResponse 가로숲길 = StationAcceptanceTest.지하철역_등록되어_있음("가로숲길").as(StationResponse.class);
        //when
        ExtractableResponse<Response> response = 최단거리_경로를_구한다(가로숲길, 양재역);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



    private void 최단경로와_거리_요금이_조회됨(ExtractableResponse<Response> response,  List<StationResponse> stations, int distance, int fare) {
        PathResponse minPath = response.as(PathResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(minPath.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(minPath.getDistance()).isEqualTo(distance),
                () -> assertThat(minPath.getFare()).isEqualTo(fare)
        );
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
                                        StationResponse downStation, int distance, int fare) {

        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, fare);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }


    public static ExtractableResponse<Response> 최단거리_경로를_구한다(StationResponse 시작역, StationResponse 도착역) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", 시작역.getId());
        params.put("target", 도착역.getId());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
