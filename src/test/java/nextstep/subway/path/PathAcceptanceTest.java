package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
    private LineResponse 구호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 여의도역;
    private StationResponse 샛강역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        샛강역 = StationAcceptanceTest.지하철역_등록되어_있음("샛강역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("삼호선", "orange", 교대역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("구호선", "brown", 여의도역.getId(), 샛강역.getId(), 10))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("경로를 조회한다")
    @Test
    void getPath() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 13);
    }

    @DisplayName("출발역과 도착역이 같을 때 경로를 조회한다")
    @Test
    void getPathWithSameStations() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 강남역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("존재하지 않는 경로를 조회한다")
    @Test
    void getNotExistedPath() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 여의도역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("없는 역에 대해 경로를 조회한다")
    @Test
    void getPathWithNotExistedStation() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 0L);

        // then
        경로_조회_실패됨(response);
    }

    private ExtractableResponse<Response> 경로_조회_요청(final long sourceId, final long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private PathResponse 경로가_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final PathResponse path = response.as(PathResponse.class);
        assertThat(path).isNotNull();

        return path;
    }

    private void 경로의_길이가_일치됨(final PathResponse path, final int distance) {
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    private void 경로_조회_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
