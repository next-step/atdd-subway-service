package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 양재역;
    private StationResponse 강남역;
    private StationResponse 교대역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);

        LineRequest 신분당선_노선등록 = new LineRequest("신분당선", "bg-blue-600", 강남역.getId(), 양재역.getId(), 3);
        LineRequest 이호선_노선등록 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 1);
        LineRequest 삼호선_노선등록 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_노선등록).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_노선등록).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_노선등록).as(LineResponse.class);
    }

    @DisplayName("지하철 최단 경로가 조회된다.")
    @Test
    void acceptanceManageSection() {
        // when
        ExtractableResponse<Response> response = 출발역_도착역_검색(양재역, 교대역);

        // then
        최단거리_조회됨(response);
    }

    private void 최단거리_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathStationDto 양재역_pathStaion = new PathStationDto(양재역.getId(), 양재역.getName(), 양재역.getCreatedDate());
        PathStationDto 강남역_pathStaion = new PathStationDto(강남역.getId(), 강남역.getName(), 강남역.getCreatedDate());
        PathStationDto 교대역_pathStaion = new PathStationDto(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate());

        assertAll(
            () -> Assertions.assertThat(response.as(PathResponse.class).getStations()).isEqualTo(List.of(양재역_pathStaion, 강남역_pathStaion, 교대역_pathStaion)),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(4)
        );
    }

    private ExtractableResponse<Response> 출발역_도착역_검색(StationResponse sourceStation, StationResponse targetStation) {
        return RestAssured.given().log().all()
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/paths?source={sourceStationId}&target={targetStationId}", sourceStation.getId(), targetStation.getId())
                            .then().log().all()
                            .extract();
    }
}
