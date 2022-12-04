package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.로그인된_회원;
import static nextstep.subway.line.acceptance.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceStep.생성된_회원;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.path.acceptance.PathAcceptanceStep.*;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 명동역;
    private StationResponse 사당역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse tokenResponse;
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        명동역 = StationAcceptanceTest.지하철역_등록되어_있음("명동역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-red-600", 명동역.getId(), 사당역.getId(), 30)).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        생성된_회원(EMAIL, PASSWORD, AGE);
        tokenResponse = 로그인된_회원(EMAIL, PASSWORD).as(TokenResponse.class);
    }


    @DisplayName("최단 경로를 조회한다")
    @Test
    void getLines() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(tokenResponse, 강남역.getId(), 남부터미널역.getId());

        // then
        최단경로_목록_응답됨(response);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 같으면 예외를 반환한다.")
    @Test
    void getLinesWithException() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(tokenResponse, 강남역.getId(), 강남역.getId());

        // then
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 반환한다.")
    @Test
    void getLinesWithException2() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(tokenResponse, 강남역.getId(), 사당역.getId());

        // then
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단 경로를 조회 시, 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외를 반환한다.")
    @Test
    void getLinesWithException3() {
        // given
        long 없는역_ID = 100L;

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(tokenResponse, 강남역.getId(), 없는역_ID);

        // then
        최단경로_목록_조회_실패(response);
    }
}
