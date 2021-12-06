package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 관리한다.")
    @Test
    void lineSectionManagement() {
        // when
        final ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        // then
        assertThat(지하철_노선에_지하철역_등록_요청.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        final ExtractableResponse<Response> 등록한_지하철_구간이_반영된_역_목록_조회 = 지하철_노선에_등록된_역_목록_조회_요청();
        final List<LineResponse> 등록한_지하철_구간이_반영된_역_목록 = 등록한_지하철_구간이_반영된_역_목록_조회.body().jsonPath().getList(".", LineResponse.class);
        // then
        assertAll(() -> {
            assertThat(등록한_지하철_구간이_반영된_역_목록_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(등록한_지하철_구간이_반영된_역_목록.get(0).getStations()).extracting("name").containsExactly("강남역","양재역","광교역");
        });

        // when
        ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        // then
        assertThat(지하철_노선에_지하철역_제외_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        final ExtractableResponse<Response> 삭제한_지하철_구간이_반영된_역_목록_조회 = 지하철_노선에_등록된_역_목록_조회_요청();
        final List<LineResponse> 삭제한_지하철_구간이_반영된_역_목록 = 삭제한_지하철_구간이_반영된_역_목록_조회.body().jsonPath().getList(".", LineResponse.class);
        // then
        assertAll(() -> {
            assertThat(삭제한_지하철_구간이_반영된_역_목록_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(삭제한_지하철_구간이_반영된_역_목록.get(0).getStations()).extracting("name").containsExactly("강남역","광교역");
        });
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .get("/lines")
                .then().log().all()
                .extract();
    }
}
