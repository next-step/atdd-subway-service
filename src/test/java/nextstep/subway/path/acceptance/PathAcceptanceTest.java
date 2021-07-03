package nextstep.subway.path.acceptance;

import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.TestFixture.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.fare.domain.Fare.*;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private List<StationResponse> stationResponses;
    private List<String> 예상최단경로_지하철역_이름;
    private TokenResponse 사용자토큰;

    /*
           (*출발*)(삼호선)종로3가역
                            |
                           (10)
                            |
     (일,이호선)시청역ㅡ(10)ㅡ을지로입구역ㅡㅡㅡ(100)ㅡㅡㅡㅡㅡㅡ(이호선,삼호선)을지로3가역
         |                                                               |
        (10)                                                            (10)
         |                                                               |
      (일호선,사호선)서울역-(10)-회현역-(10)-명동역-(10)-(*도착*)(사호선, 삼호선)충무로역

                            교대역ㅡ(10)ㅡ강남역
    *
    *
    */
    @BeforeAll
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
        일호선 = 지하철_노선_등록되어_있음("일호선", "남색", 시청역, 서울역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "초록색", 시청역, 을지로입구역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "주황색", 종로3가역, 을지로입구역, 10, 300);
        사호선 = 지하철_노선_등록되어_있음("사호선", "파란색", 서울역, 회현역, 10, 400);

        //지하철 노선에 지하철역 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 을지로입구역, 을지로3가역, 100);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 을지로3가역, 충무로역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 회현역, 명동역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 명동역, 충무로역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 교대역, 10);

        stationResponses = new ArrayList<>(Arrays.asList(종로3가역, 을지로입구역, 시청역, 서울역, 회현역, 명동역, 충무로역));
        예상최단경로_지하철역_이름 = stationResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void 최단경로_조회() {

        int 예상최단거리 = 60;
        int 예상요금 = BASE_FARE +
                (60 - 50) / DISTANCE_SECOND_INTERVAL_DIVIDER * DISTANCE_EXTRA_CHARGE_UNIT +
                (50 - 10) / DISTANCE_FIRST_INTERVAL_DIVIDER * DISTANCE_EXTRA_CHARGE_UNIT +
                사호선.getExtraCharge();

        ExtractableResponse<Response> 최단경로 = 최단_경로_조회_요청함(종로3가역, 충무로역);

        최단_경로_조회_성공함(최단경로);
        최단_경로_지하철_목록_반환됨(최단경로, 예상최단경로_지하철역_이름);
        최단_경로_거리_반환됨(최단경로, 예상최단거리);
        최단_경로_요금_반환됨(최단경로, 예상요금);

        ExtractableResponse<Response> 출발도착_동일_최단경로 = 최단_경로_조회_요청함(종로3가역, 종로3가역);
        최단_경로_조회_실패함(출발도착_동일_최단경로);

        ExtractableResponse<Response> 출발도착_연결안됨_최단경로 = 최단_경로_조회_요청함(종로3가역, 강남역);
        최단_경로_조회_실패함(출발도착_연결안됨_최단경로);

        ExtractableResponse<Response> 도착_등록안됨_최단경로 = 최단_경로_조회_요청함(종로3가역, 동작역);
        최단_경로_조회_실패함(도착_등록안됨_최단경로);
    }

    @DisplayName("청소년 로그인 사용자 : 최단경로를 조회한다")
    @Test
    void 최단경로_조회_청소년_로그인사용자() {
        회원_생성을_요청(TEENAGER_EMAIL, PASSWORD, TEENAGER_AGE);
        사용자토큰 = 로그인_요청(TEENAGER_EMAIL, PASSWORD).as(TokenResponse.class);
        int 예상최단거리 = 60;
        int 예상요금 = (int) ((2150 + 사호선.getExtraCharge() - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_TEENAGER_DISCOUNT_RATE));

        ExtractableResponse<Response> 최단경로 = 최단_경로_조회_요청함(사용자토큰, 종로3가역, 충무로역);

        최단_경로_조회_성공함(최단경로);
        최단_경로_지하철_목록_반환됨(최단경로, 예상최단경로_지하철역_이름);
        최단_경로_거리_반환됨(최단경로, 예상최단거리);
        최단_경로_요금_반환됨(최단경로, 예상요금);
    }

    @DisplayName("어린이 로그인 사용자 : 최단경로를 조회한다")
    @Test
    void 최단경로_조회_어린이_로그인사용자() {
        회원_생성을_요청(CHILD_EMAIL, PASSWORD, CHILD_AGE);
        사용자토큰 = 로그인_요청(CHILD_EMAIL, PASSWORD).as(TokenResponse.class);
        int 예상최단거리 = 60;
        int 예상요금 = (int) ((2150 + 사호선.getExtraCharge() - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_CHILD_DISCOUNT_RATE));

        ExtractableResponse<Response> 최단경로 = 최단_경로_조회_요청함(사용자토큰, 종로3가역, 충무로역);

        최단_경로_조회_성공함(최단경로);
        최단_경로_지하철_목록_반환됨(최단경로, 예상최단경로_지하철역_이름);
        최단_경로_거리_반환됨(최단경로, 예상최단거리);
        최단_경로_요금_반환됨(최단경로, 예상요금);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청함(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .queryParam("source", 출발역.getId())
                .queryParam("target", 도착역.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청함(TokenResponse 토큰, StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰.getAccessToken())
                .queryParam("source", 출발역.getId())
                .queryParam("target", 도착역.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단_경로_조회_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_지하철_목록_반환됨(ExtractableResponse<Response> response, List<String> expectedStations) {
        List<String> stations = response.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(stations).hasSameElementsAs(expectedStations);
    }

    private void 최단_경로_거리_반환됨(ExtractableResponse<Response> response, int expectedDistance) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(expectedDistance);
    }

    private void 최단_경로_요금_반환됨(ExtractableResponse<Response> response, int expectedFare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(expectedFare);
    }

    private void 최단_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation,
                                        int distance, int extraCharge) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance, extraCharge);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private StationResponse 지하철역_등록되어_있음(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }
}
