package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 대전역;
    private StationResponse 사당역;
    private StationResponse 서초역;
    private StationResponse 신길역;
    private StationResponse 여의도역;
    private Member 어린이;
    private Member 청소년;

    /**
     * 교대역    --- *2호선*(50) ---   강남역   --- *4호선*(20) ---   사당역
     * |                              |
     * *3호선*(3)                   *신분당선*(10)
     * |                              |
     * 남부터미널역  --- *3호선*(37) ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        서초역 = StationAcceptanceTest.지하철역_등록되어_있음("서초역").as(StationResponse.class);
        신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
        여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        대전역 = new StationResponse(10L, "대전역", LocalDateTime.now(),LocalDateTime.now());

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 500);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 50, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 40, 0);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 사당역, 서초역, 30, 0);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 신길역, 여의도역, 30, 0);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록되어_있음(사호선, 강남역, 사당역, 20);

        어린이 = new Member("email@email.com", "password", 12);
        청소년 = new Member("email1@email.com", "password", 18);
        회원_생성을_요청(어린이.getEmail(), 어린이.getPassword(), 어린이.getAge());
        회원_생성을_요청(청소년.getEmail(), 청소년.getPassword(), 청소년.getAge());
    }

    @DisplayName("경로 조회")
    @Test
    void findPath() {
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 강남역, null).body().as(PathResponse.class);
        // Then 지하철 경로 조회됨
        지하철_경로_조회됨(pathResponse, Arrays.asList(남부터미널역, 양재역, 강남역));
        // And 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 47);
        // And 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponse, 2_550);
        // When 회원 생성
        회원_생성을_요청("eamil@email.com", "password", 18);
        // And 로그인 요청
        String token = 로그인_요청("eamil@email.com", "password").body().as(TokenResponse.class).getAccessToken();
        // And 지하철 경로 조회 요청
        PathResponse pathResponseWithLogin = 지하철_경로_조회_요청(남부터미널역, 강남역, token).body().as(PathResponse.class);
        // Then 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponseWithLogin, 1_760);
    }

    @DisplayName("출발역과 도착역 같은 경우")
    @Test
    void findPathSourceEqualsTarget() {
        // When 지하철 경로 조회 요청
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(남부터미널역, 남부터미널역, null);
        // Then 에러가 발생함
        잘못된_요청_응답됨(response, "출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findPathSourceNotConnectedTarget() {
        // When 지하철 경로 조회 요청
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(남부터미널역, 신길, null);
        // Then 에러가 발생함
        잘못된_요청_응답됨(response, "출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathNotExistSourceOrTarget() {
        // When 지하철 경로 조회 요청
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(남부터미널역, 대전역, null);
        // Then 에러가 발생함
        잘못된_요청_응답됨(response, "Station을 조회할 수 없습니다.");
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (어린이 할인 적용, 10km 초과, 50km 까지 추가요금 적용)")
    @Test
    void findShortestPath_어린이_할인_10km_초과_50km_까지() {
        // Given
        String token = 로그인_요청(어린이.getEmail(), 어린이.getPassword()).body().as(TokenResponse.class).getAccessToken();
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 강남역, token).body().as(PathResponse.class);
        // And 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 47);
        // And 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponse, 1_100);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (청소년 할인 적용, 10km 초과, 50km 까지 추가요금 적용)")
    @Test
    void findShortestPath_청소년_할인_10km_초과_50km_까지() {
        // Given
        String token = 로그인_요청(청소년.getEmail(), 청소년.getPassword()).body().as(TokenResponse.class).getAccessToken();
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 강남역, token).body().as(PathResponse.class);
        // And 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 47);
        // And 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponse, 1_760);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (어린이 할인 적용, 50km 초과 추가요금 적용)")
    @Test
    void findShortestPath_어린이_할인_50km_초과() {
        // Given
        String token = 로그인_요청(어린이.getEmail(), 어린이.getPassword()).body().as(TokenResponse.class).getAccessToken();
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 사당역, token).body().as(PathResponse.class);
        // And 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 67);
        // And 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponse, 1_250);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (청소년 할인 적용, 50km 초과 추가요금 적용)")
    @Test
    void findShortestPath_청소년_할인_50km_초과() {
        // Given
        String token = 로그인_요청(청소년.getEmail(), 청소년.getPassword()).body().as(TokenResponse.class).getAccessToken();
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 사당역, token).body().as(PathResponse.class);
        // And 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 67);
        // And 지하철 요금이 예상과 같음
        지하철_요금_조회됨(pathResponse, 2_000);
    }

    private void 지하철_경로_길이_같음(PathResponse pathResponse, int expectedDistance) {
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void 지하철_경로_조회됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        for (int i = 0; i < expectedStations.size(); i++) {
            assertThat(expectedStations.get(i)).isEqualTo(pathResponse.getStations().get(i));
        }
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse 남부터미널역, StationResponse 강남역, String token) {
        RequestSpecification requestSpecification = RestAssured
                .given().log().all();

        if (token != null) {
            requestSpecification = requestSpecification.auth().oauth2(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        }

        return requestSpecification
                .when().get("/paths?source={sourceId}&target={targetId}", 남부터미널역.getId(), 강남역.getId())
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStationResponse,
                                        StationResponse downStationResponse, int distance, int overFare) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(lineName, color, upStationResponse.getId(), downStationResponse.getId(), distance, overFare))
                .body().as(LineResponse.class);
    }

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStationResponse, downStationResponse, distance);
    }

    private void 잘못된_요청_응답됨(ExtractableResponse<Response> response, String expectedMessage) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).isEqualTo(expectedMessage)
        );
    }

    private void 지하철_요금_조회됨(PathResponse pathResponse, int expectedFare) {
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}
