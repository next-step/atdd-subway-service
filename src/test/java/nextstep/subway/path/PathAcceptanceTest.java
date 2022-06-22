package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
import org.junit.jupiter.api.Test;
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
    private StationResponse 남부터미널역;

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

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }


    @Test
    @DisplayName("최단거리 경로 찾기 기능")
    void pathFinder() {
        //when
        ExtractableResponse<Response> response = 최단거리_경로를_구한다(교대역, 양재역);

        //then
        최단경로가_조회됨(response, new PathResponse(Arrays.asList(교대역, 남부터미널역, 양재역) ,5));
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
        //when
        ExtractableResponse<Response> response = 최단거리_경로를_구한다(남부터미널역, 양재역);

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



    private void 최단경로가_조회됨(ExtractableResponse<Response> response, PathResponse pathResponse) {
        PathResponse minPath = response.as(PathResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(minPath.getDistance()).isEqualTo(5),
                () ->  assertThat(minPath.getStations()).containsExactlyElementsOf(pathResponse.getStations())
        );
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
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
