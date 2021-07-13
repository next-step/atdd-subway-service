package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.common.domain.SubwayFare;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import sun.security.x509.OtherName;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
@ExtendWith(MockitoExtension.class)
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
        이호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
        삼호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5));

        지하철역_노선에_지하철역_추가(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findBestPathTest() {
        //given
        Map<String, Long> pathRequestMap = createPathRequestMap(교대역.getId(), 양재역.getId());

        //when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(pathRequestMap);

        //then
        최단_경로_조회됨(response);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회")
    @Test
    void findBestPathWithFareTest() {
        //given
        Map<String, Long> pathRequestMap = createPathRequestMap(교대역.getId(), 양재역.getId());

        //when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(pathRequestMap);

        //then
        최단_경로_조회됨(response);
        요금_조회됨(response);



    }

    private void 요금_조회됨(ExtractableResponse<Response> response) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPaymentFare()).isEqualTo(SubwayFare.BASIC_FARE);

    }


    private Map<String, Long> createPathRequestMap(Long sourceId, Long targetId) {
        Map<String, Long> pathRequestMap = new HashMap<>();
        pathRequestMap.put("source", sourceId);
        pathRequestMap.put("target", targetId);
        return pathRequestMap;
    }

    private static ExtractableResponse<Response> 최단_경로_조회_요청(Map<String, Long> pathRequest) {
        return RestAssured
                .given().log().all()
                .queryParams(pathRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
    }


}
