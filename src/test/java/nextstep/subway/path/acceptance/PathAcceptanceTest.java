package nextstep.subway.path.acceptance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ExtractableResponse;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;

    private StationResponse 시청역;
    private StationResponse 을지로입구역;
    private StationResponse 을지로3가역;
    private StationResponse 서울역;
    private StationResponse 회현역;
    private StationResponse 명동역;
    private StationResponse 종로3가역;
    private StationResponse 충무로역;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 동작역;

    /*
           (*출발*)종로3가역
                    |
                   (10) 
                    |
     시청역ㅡ(10)ㅡ을지로입구역ㅡㅡㅡ(100)ㅡㅡㅡ을지로3가역
         |                                      |
        (10)                                  (10)
         |                                      |
      서울역-(10)-회현역-(10)-명동역-(10)-(*도착*)충무로역

                            교대역ㅡ(10)ㅡ강남역
    *
    *
    */
    @BeforeEach
    public void setUp() {
        super.setUp();

        //지하철역 등록되어 있음
        시청역 = 지하철역_등록되어_있음("시청역");
        을지로입구역 = 지하철역_등록되어_있음("을지로입구역");
        을지로3가역 = 지하철역_등록되어_있음("을지로3가역");
        서울역 = 지하철역_등록되어_있음("서울역");
        회현역 = 지하철역_등록되어_있음("회현역");
        명동역 = 지하철역_등록되어_있음("명동역");
        종로3가역 = 지하철역_등록되어_있음("종로3가역");
        충무로역 = 지하철역_등록되어_있음("충무로역");
        강남역 = 지하철역_등록되어_있음("강남역");
        교대역 = 지하철역_등록되어_있음("교대역");
        동작역 = 지하철역_등록되어_있음("동작역");

        //지하철 노선 등록되어 있음
        일호선 = 지하철_노선_등록되어_있음("일호선", "남색", 시청역, 서울역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "초록색", 시청역, 을지로입구역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "주황색", 종로3가역, 을지로3가역, 10);
        사호선 = 지하철_노선_등록되어_있음("사호선", "파란색", 서울역, 회현역, 10);

        //지하철 노선에 지하철역 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 을지로입구역, 을지로3가역, 100);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 을지로3가역, 충무로역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 회현역, 명동역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 명동역, 충무로역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 교대역, 10);
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void 최단경로_조회() {
        //when : 최단 경로 조회 요청
        ExtractableResponse<Response> 최단경로 = 최단_경로_조회_요청함(종로3가역, 충무로역);
        //then : 최단 경로 지하철 목록이 반환됨
        최단_경로_지하철_목록_반환됨(최단경로);

        //when : (출발역과 도착역이 같은 경우) 최단 경로 조회 요청
        ExtractableResponse<Response> 출발도착_동일_최단경로 = 최단_경로_조회_요청함(종로3가역, 종로3가역);
        //then : 최단 경로 조회 실패함
        최단_경로_조회_실패함(출발도착_동일_최단경로);

        //when : (출발역과 도착역이 연결되어 있지 않은 경우) 최단 경로 조회 요청
        ExtractableResponse<Response> 출발도착_연결안됨_최단경로 = 최단_경로_조회_요청함(종로3가역, 강남역);
        //then : 최단 경로 조회 실패함
        최단_경로_조회_실패함(출발도착_연결안됨_최단경로);

        //when : (출발역이나 도착역이 존재하지 않는 경우) 최단 경로 조회 요청
        ExtractableResponse<Response> 도착_등록안됨_최단경로 = 최단_경로_조회_요청함(종로3가역, 동작역);
        //then : 최단 경로 조회 실패함
        최단_경로_조회_실패함(도착_등록안됨_최단경로);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청함(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .queryParam("start", 출발역.getId())
                .queryParam("end", 도착역.getId())
                .when().get("/path")
                .then().log().all()
                .extract();
    }

    private void 최단_경로_지하철_목록_반환됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private StationResponse 지하철역_등록되어_있음(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }
}
