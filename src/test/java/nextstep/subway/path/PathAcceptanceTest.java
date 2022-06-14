package nextstep.subway.path;

import static nextstep.subway.behaviors.MemberBehaviors.회원_생성을_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_등록되어_있음;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철역_등록되어_있음;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

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
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
    }
    @Test
    void 최단경로찾기() {
        노선_추가요금_없는_테스트경로_세팅();

        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 양재역;

        ExtractableResponse<Response> response = 로그인정보없이_최단경로_및_요금을_조회한다(출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathResponse pathResponse = response.as(PathResponse.class);
        최단경로_확인(pathResponse, Lists.newArrayList(출발역, 남부터미널역, 도착역));
    }

    @Test
    void 기본요금_테스트() {
        노선_추가요금_없는_테스트경로_세팅();

        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 양재역;

        ExtractableResponse<Response> response = 로그인정보없이_최단경로_및_요금을_조회한다(출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathResponse pathResponse = response.as(PathResponse.class);
        최단경로거리_확인(pathResponse, 5);
        지하철요금_확인(pathResponse,SubwayFare.DEFAULT_FARE);
        최단경로_확인(pathResponse, Lists.newArrayList(출발역, 남부터미널역, 도착역));
    }



    @Test
    void 노선별_추가요금_테스트() {
        노선_추가요금_있는_테스트경로_세팅();

        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 강남역;

        ExtractableResponse<Response> response = 로그인정보없이_최단경로_및_요금을_조회한다(출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathResponse pathResponse = response.as(PathResponse.class);
        최단경로거리_확인(pathResponse, 9);
        int 노선추가요금 = Math.max(삼호선.getExtraCharge(),신분당선.getExtraCharge());
        지하철요금_확인(pathResponse,SubwayFare.DEFAULT_FARE.plus(노선추가요금));
        최단경로_확인(pathResponse, Lists.newArrayList(출발역, 남부터미널역, 양재역, 도착역));
    }

    @Test
    void 연령별_할인요금_테스트_어린이() {
        노선_추가요금_없는_테스트경로_세팅();
        int age = 11;
        회원_생성을_요청("EMAIL", "PASSWORD", age);
        String 사용자토큰 = 로그인_되어있음("EMAIL", "PASSWORD").getAccessToken();

        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 양재역;

        ExtractableResponse<Response> response =  로그인_상태에서_최단경로_및_요금을_조회한다(사용자토큰, 출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(isChild(age)).isTrue();
        PathResponse pathResponse = response.as(PathResponse.class);
        최단경로거리_확인(pathResponse, 5);
        SubwayFare expectedFare = SubwayFare.DEFAULT_FARE.subtract(350).discountedByPercent(50);
        지하철요금_확인(pathResponse,expectedFare);
        최단경로_확인(pathResponse, Lists.newArrayList(출발역, 남부터미널역, 도착역));
    }

    @Test
    void 연령별_할인요금_테스트_청소년() {
        노선_추가요금_없는_테스트경로_세팅();
        int age = 18;
        회원_생성을_요청("EMAIL", "PASSWORD", age);
        String 사용자토큰 = 로그인_되어있음("EMAIL", "PASSWORD").getAccessToken();

        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 양재역;

        ExtractableResponse<Response> response =  로그인_상태에서_최단경로_및_요금을_조회한다(사용자토큰, 출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(isTeen(age)).isTrue();
        PathResponse pathResponse = response.as(PathResponse.class);
        최단경로거리_확인(pathResponse, 5);
        SubwayFare expectedFare = SubwayFare.DEFAULT_FARE.subtract(350).discountedByPercent(20);
        지하철요금_확인(pathResponse,expectedFare);
        최단경로_확인(pathResponse, Lists.newArrayList(출발역, 남부터미널역, 도착역));
    }

    private ExtractableResponse<Response> 로그인정보없이_최단경로_및_요금을_조회한다(StationResponse source, StationResponse target) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("source", String.valueOf(source.getId()));
        queryParams.put("target", String.valueOf(target.getId()));

        return RestAssured
                .given().log().all()
                .accept(ContentType.JSON)
                .queryParams(queryParams)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_상태에서_최단경로_및_요금을_조회한다(String accessToken, StationResponse source, StationResponse target) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("source", String.valueOf(source.getId()));
        queryParams.put("target", String.valueOf(target.getId()));

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .queryParams(queryParams)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    private void 노선_추가요금_없는_테스트경로_세팅(){
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10,0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5,0);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * 교대역    --- *2호선*(100)  ---   강남역
     * |                                 |
     * *3호선*(3)                      *신분당선*(5)
     * |                                 |
     * 남부터미널역  --- *3호선*(1) ---    양재
     */
    private void 노선_추가요금_있는_테스트경로_세팅(){
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 5,1000);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 100,2000);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 4,500);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    private void 지하철요금_확인(PathResponse pathResponse, SubwayFare fare) {
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
    private void 최단경로거리_확인(PathResponse pathResponse, int expected) {
        int distance = pathResponse.getDistance();
        assertThat(distance).isEqualTo(expected);
    }
    private void 최단경로_확인(PathResponse pathResponse, List<StationResponse> expectedPath) {
        List<StationResponse> stations = pathResponse.getStations();
        assertThat(stations)
                .hasSize(expectedPath.size())
                .containsExactlyElementsOf(expectedPath);
    }

    private boolean isChild(int age) {
        return age >= 6 && age < 13;
    }
    private boolean isTeen(int age) {
        return age >= 13 && age < 19;
    }
}
