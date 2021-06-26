package nextstep.subway.path.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathApiTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("경로 찾기 요청")
    @Test
    void findRequest() {
        //given
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(강남역, 양재역);
        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청 = 지하철_경로_조회_요청(경로_조회_요청_내용);
        //then
        //지하철 경로 조회 응답 확인
        //임시로 응답 상태 코드만 체크
        assertThat(지하철_경로_조회_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("경로 찾기 실패 - 출발과 도착을 같은 역")
    @Test
    void findRequestFailedBySameStation() {
        //given
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(강남역, 강남역);
        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청 = 지하철_경로_조회_요청(경로_조회_요청_내용);
        //then
        //지하철 경로 조회 응답 확인
        //임시로 응답 상태 코드만 체크
        assertThat(지하철_경로_조회_요청.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("경로 찾기 실패 - 역에 대해 잘못 된 값")
    @Test
    void findRequestFailedInvalidStationId() {
        //given
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(0L, 0L);
        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청 = 지하철_경로_조회_요청(경로_조회_요청_내용);
        //then
        //지하철 경로 조회 응답 확인
        //임시로 응답 상태 코드만 체크
        assertThat(지하철_경로_조회_요청.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청(PathRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    private PathRequest 경로_조회_요청_내용(StationResponse 출발역, StationResponse 도착역) {
        return new PathRequest(역_번호_추출(출발역), 역_번호_추출(도착역));
    }

    private PathRequest 경로_조회_요청_내용(long 출발역, long 도착역) {
        return new PathRequest(출발역, 도착역);
    }

    private static Long 역_번호_추출(StationResponse 역) {
        return 역.getId();
    }
}