package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private StationResponse 잠실역;

    /**
     * 잠실 - 20 - 강남 - 100 - 양재 - 10 - 판교
     * └---------- 10---------┘
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 100);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 10);

        LineRequest lineRequest2 = new LineRequest("이호선", "green", 잠실역.getId(), 강남역.getId(), 10);
        이호선 = 지하철_노선_생성_요청(lineRequest2).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 양재역, 10);
    }

    @DisplayName("출발역 도착역 동일할 경우 예외 발생")
    @Test
    void shortPathException1() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(잠실역, 잠실역, tokenResponse);

        출발역_도착역_동일할_경우_예외_발생함(response);
    }

    @DisplayName("출발역 도착역 연결 되어있지 않을 경우 예외 발생")
    @Test
    void shortPathException2() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        StationResponse 구일역 = StationAcceptanceTest.지하철역_등록되어_있음("구일역").as(StationResponse.class);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(잠실역, 구일역, tokenResponse);

        출발역_도착역_미연결시_경우_예외_발생함(response);
    }


    @DisplayName("출발역이나 도착역이 노선에 등록되어있지 않는경우 예외 발생")
    @Test
    void shortPathException3() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        StationResponse 구일역 = StationAcceptanceTest.지하철역_등록되어_있음("구일역").as(StationResponse.class);
        StationResponse 구로역 = StationAcceptanceTest.지하철역_등록되어_있음("구로역").as(StationResponse.class);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(구일역, 구로역, tokenResponse);

        출발역_도착역_노선_미등록시_예외_발생함(response);
    }

    @DisplayName("최단 경로와 거리를 구한다.")
    @Test
    void shortPath() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(잠실역, 판교역, tokenResponse);

        최단_경로와_거리_확인됨(response, Arrays.asList(잠실역, 양재역, 판교역), 20);
    }

    @DisplayName("회원의 최단 경로와 요금을 구한다.")
    @ParameterizedTest
    @CsvSource(value = {"6:550", "18:880", "30:1450"}, delimiter = ':')
    void shortPathFare(int age, int resultFare) {
        회원_생성을_요청(EMAIL, PASSWORD, age);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(잠실역, 판교역, tokenResponse);

        최단_경로와_거리_확인됨(response, Arrays.asList(잠실역, 양재역, 판교역), 20);

        이용요금_확인됨(response, resultFare);
    }

    @DisplayName("노선 추가요금에 따른 회원의 최단 경로와 요금을 구한다.")
    @ParameterizedTest
    @CsvSource(value = {"6:1150", "18:1480", "30:2050"}, delimiter = ':')
    void shortPathFare_노선추가요금(int age, int resultFare) {
        StationResponse 추가요금_강남역 = StationAcceptanceTest.지하철역_등록되어_있음("추가요금_강남역").as(StationResponse.class);
        StationResponse 추가요금_양재역 = StationAcceptanceTest.지하철역_등록되어_있음("추가요금_양재역").as(StationResponse.class);
        StationResponse 추가요금_판교역 = StationAcceptanceTest.지하철역_등록되어_있음("추가요금_판교역").as(StationResponse.class);
        StationResponse 추가요금_잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("추가요금_잠실역").as(StationResponse.class);

        LineResponse 추가요금_신분당선 = 추가요금_부과된_노선_생성됨("신분당선_추가요금", "red", 추가요금_강남역, 추가요금_판교역, 10, 300).as(LineResponse.class);
        LineResponse 추가요금_이호선 = 추가요금_부과된_노선_생성됨("이호선_추가요금", "green", 추가요금_잠실역, 추가요금_강남역, 10, 600).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(추가요금_신분당선, 추가요금_강남역, 추가요금_양재역, 100);
        지하철_노선에_지하철역_등록_요청(추가요금_신분당선, 추가요금_양재역, 추가요금_판교역, 10);
        지하철_노선에_지하철역_등록_요청(추가요금_이호선, 추가요금_잠실역, 추가요금_양재역, 10);

        회원_생성을_요청(EMAIL, PASSWORD, age);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 회원_최단_경로_요청(추가요금_잠실역, 추가요금_판교역, tokenResponse);

        최단_경로와_거리_확인됨(response, Arrays.asList(추가요금_잠실역, 추가요금_양재역, 추가요금_판교역), 20);

        이용요금_확인됨(response, resultFare);
    }

    private ExtractableResponse<Response> 추가요금_부과된_노선_생성됨(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance, int fare) {
        LineRequest params = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance, fare);
        return 지하철_노선_생성_요청(params);
    }

    private ExtractableResponse<Response> 회원_최단_경로_요청(StationResponse source, StationResponse target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={stationId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    private void 최단_경로와_거리_확인됨(ExtractableResponse<Response> response, List<StationResponse> resultStations, int resultDistance) {
        PathResponse pathResponse =  response.as(PathResponse.class);
        List<StationResponse> stationResponseList = pathResponse.getStations();

        for (int i = 0; i < stationResponseList.size() ; i++) {
            assertThat(stationResponseList.get(i).getId()).isEqualTo(resultStations.get(i).getId());
        }

        최단_거리값_확인됨(pathResponse.getDistance(), resultDistance);

    }

    private void 최단_거리값_확인됨(int distance, int resultDistance) {
        assertThat(distance).isEqualTo(resultDistance);
    }

    private void 출발역_도착역_동일할_경우_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 출발역_도착역_미연결시_경우_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 출발역_도착역_노선_미등록시_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 이용요금_확인됨(ExtractableResponse<Response> response, int resultFare) {
        PathResponse pathResponse =  response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(resultFare);
    }
}
