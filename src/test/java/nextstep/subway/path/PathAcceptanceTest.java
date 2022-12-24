package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineRestAssured;
import nextstep.subway.line.acceptance.LineSectionRestAssured;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.path.PathRestAssured.지하철_경로_조회_요청;
import static nextstep.subway.path.PathRestAssured.지하철_경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Feature 지하철 경로 관련 기능
 * Background
 * Given 지하철역(Station)이 여러개 등록되어 있음
 * And 지하철노선(Line) 여러개 등록되어 있음
 * And 지하철노선에 지하철역(Section) 여러개 등록되어 있음
 * AND 회원 등록되어 있음(성인, 로그인, 청소년)
 * <p>
 * Scenario 출발역과 도착역 사이의 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 출발역과 도착역 사이의 최단 경로 조회됨
 * AND 총 거리 응답함
 * AND 요금 응답함(성인, 로그인, 청소년)
 * <p>
 * Scenario 출발역과 도착역이 같을 때 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 최단 경로 조회 실패
 * <p>
 * Scenario 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
 * When 지하철 최단 경로 조회 요청
 * Then 최단 경로 조회 실패
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 이수역;
    private String 성인회원;
    private String 청소년회원;
    private String 어린이회원;
    private String adultEmail = "adult@gmail.com";
    private String adolescentEmail = "adolescent@gmail.com";
    private String childEmail = "child@gmail.com";


    /**
     * 교대역   --- *2호선 10* ---   강남역
     * |                        |
     * *삼호선 3*                   *신분당선 10*
     * |                        |
     * 남부터미널역 --- *삼호선 3* ---   양재
     * <p>
     * 사당역----4호선(5)------이수역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = StationAcceptanceTest.지하철역_등록되어_있음("이수역").as(StationResponse.class);

        신분당선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 100)).as(LineResponse.class);
        이호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 200)).as(LineResponse.class);
        삼호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 100)).as(LineResponse.class);
        사호선 = LineRestAssured.지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-blue-600", 사당역.getId(), 이수역.getId(), 5, 150)).as(LineResponse.class);

        LineSectionRestAssured.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        성인_회원_등록되어_있음();
        성인_회원_로그인_됨();
        청소년_회원_등록되어_있음();
        청소년_회원_로그인_됨();
        어린이_회원_등록되어_있음();
        어린이_회원_로그인_됨();
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로 조회")
    @Test
    void findTheShortestPath() {
        ExtractableResponse<Response> response = PathRestAssured.지하철_경로_조회_요청(성인회원, 교대역.getId(), 강남역.getId());
        지하철_최단_경로_조회_조회됨(response,10);
    }

    @DisplayName("예외발생 - 출발역과 도착역이 같은데 조회하는 경우")
    @Test
    void makeExceptionWhenSourceStationAndTargetStationIsEqual() {
        ExtractableResponse<Response> response = PathRestAssured.지하철_경로_조회_요청(성인회원, 교대역.getId(), 교대역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 연결되어 있지 않은 출발역과 도착역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenSourceStationAndTargetStationIsNotBelongToSameLine() {
        ExtractableResponse<Response> response = PathRestAssured.지하철_경로_조회_요청(성인회원, 교대역.getId(), 사당역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 존재하지 않은 출발역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenSourceStationIsNotExist() {
        ExtractableResponse<Response> response = PathRestAssured.지하철_경로_조회_요청(성인회원, 교대역.getId(), 10L);
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("예외발생 - 존재하지 않은 도착역으로 최단 경로 조회")
    @Test
    void makeExceptionWhenTargetStationIsNotExist() {
        ExtractableResponse<Response> response = PathRestAssured.지하철_경로_조회_요청(10L, 교대역.getId());
        지하철_최단_경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로 조회")
    @TestFactory
    Stream<DynamicTest> findShortestPath() {
        return Stream.of(
                dynamicTest("성인회원 경로 조회", () -> {
                    ExtractableResponse<Response> 성인_회원_경로조회_결과 = 지하철_경로_조회_요청(성인회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(성인_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(성인_회원_경로조회_결과, 10);
                    지하철_이용_요금_조회됨(성인_회원_경로조회_결과, 1450);
                }),
                dynamicTest("성인회원 경로 조회 거리 초과", () -> {
                    ExtractableResponse<Response> 성인_회원_경로조회_결과 = 지하철_경로_조회_요청(성인회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(성인_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(성인_회원_경로조회_결과, 11);
                    지하철_이용_요금_조회됨(성인_회원_경로조회_결과, 1550);
                }),
                dynamicTest("청소년회원 경로 조회", () -> {
                    ExtractableResponse<Response> 청소년_회원_경로조회_결과 = 지하철_경로_조회_요청(청소년회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(청소년_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(청소년_회원_경로조회_결과, 10);
                    지하철_이용_요금_조회됨(청소년_회원_경로조회_결과, 880);
                }),
                dynamicTest("청소년회원 경로 조회", () -> {
                    ExtractableResponse<Response> 청소년_회원_경로조회_결과 = 지하철_경로_조회_요청(청소년회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(청소년_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(청소년_회원_경로조회_결과, 11);
                    지하철_이용_요금_조회됨(청소년_회원_경로조회_결과, 960);
                }),
                dynamicTest("어린이회원 경로 조회", () -> {
                    ExtractableResponse<Response> 어린이_회원_경로조회_결과 = 지하철_경로_조회_요청(어린이회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(어린이_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(어린이_회원_경로조회_결과, 10);
                    지하철_이용_요금_조회됨(어린이_회원_경로조회_결과, 550);
                }),
                dynamicTest("어린이회원 경로 조회", () -> {
                    ExtractableResponse<Response> 어린이_회원_경로조회_결과 = 지하철_경로_조회_요청(어린이회원, 교대역.getId(), 강남역.getId());
                    지하철_최단_경로_조회됨(어린이_회원_경로조회_결과);
                    지하철_최단_경로_총_거리_조회됨(어린이_회원_경로조회_결과, 11);
                    지하철_이용_요금_조회됨(어린이_회원_경로조회_결과, 600);
                })
        );
    }

    public static void 지하철_최단_경로_조회_조회됨(ExtractableResponse<Response> response, int expectDistance) {
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(expectDistance);
        });
    }

    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 지하철_최단_경로_총_거리_조회됨(ExtractableResponse<Response> response, int distance) {
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    private void 지하철_이용_요금_조회됨(ExtractableResponse<Response> response, int fare) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    private void 성인_회원_등록되어_있음() {
        ExtractableResponse<Response> createAdultResponse = 회원_생성을_요청(adultEmail, PASSWORD, 19);
        회원_생성됨(createAdultResponse);
    }

    private void 성인_회원_로그인_됨() {
        ExtractableResponse<Response> loginAdultResponse = 로그인_요청(new TokenRequest(adultEmail, PASSWORD));
        로그인_됨(loginAdultResponse);

        성인회원 = loginAdultResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 청소년_회원_등록되어_있음() {
        ExtractableResponse<Response> createTeenagerResponse = 회원_생성을_요청(adolescentEmail, PASSWORD, 13);
        회원_생성됨(createTeenagerResponse);
    }

    private void 청소년_회원_로그인_됨() {
        ExtractableResponse<Response> loginTeenagerResponse = 로그인_요청(new TokenRequest(adolescentEmail, PASSWORD));
        로그인_됨(loginTeenagerResponse);

        청소년회원 = loginTeenagerResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 어린이_회원_등록되어_있음() {
        ExtractableResponse<Response> createChildResponse = 회원_생성을_요청(childEmail, PASSWORD, 6);
        회원_생성됨(createChildResponse);
    }

    private void 어린이_회원_로그인_됨() {
        ExtractableResponse<Response> loginChildResponse = 로그인_요청(new TokenRequest(childEmail, PASSWORD));
        로그인_됨(loginChildResponse);

        어린이회원 = loginChildResponse.as(TokenResponse.class).getAccessToken();
    }
}
