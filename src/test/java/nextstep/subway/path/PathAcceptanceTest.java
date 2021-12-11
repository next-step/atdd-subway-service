package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


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
   * 남부터미널역  --- *3호선* ---   양재역
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
  }

  @DisplayName("출발역부터 도착역까지 최단 경로를 조회한다.")
  @Test
  void 최단_경로_조회() {
    // when
    ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

    // then
    최단_경로_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역));
  }

  private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
    return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", sourceStation.getId())
            .queryParam("target", targetStation.getId())
            .when()
            .get("/paths")
            .then().log().all()
            .extract();
  }

  private void 최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    최단_경로_데이터_확인됨(response.as(PathResponse.class).getStations(), expectedStations);
  }

  private void 최단_경로_데이터_확인됨(List<StationResponse> actualStations, List<StationResponse> expectedStations) {
    assertThat(actualStations).isEqualTo(expectedStations);
  }
}
