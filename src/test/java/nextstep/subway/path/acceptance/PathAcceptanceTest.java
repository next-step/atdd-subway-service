package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceStep.회원_등록되어_있음;
import static nextstep.subway.path.acceptance.PathAcceptanceStep.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 일호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 서울역;
    private StationResponse 용산역;
    private StationResponse 혜화역;
    private TokenResponse 어린이;
    private TokenResponse 청소년;
    private TokenResponse 성인;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * 교대-(10)-강남
     * (3)     (10)
     * 남부-(2)-양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
        용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
        혜화역 = 지하철역_등록되어_있음("혜화역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-red-600", 서울역, 용산역, 20);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음("CHILDREN@nextstep.com", "password", 6);
        어린이 = 로그인_되어_있음("CHILDREN@nextstep.com", "password");

        회원_등록되어_있음("TEENAGER@nextstep.com", "password", 15);
        청소년 = 로그인_되어_있음("TEENAGER@nextstep.com", "password");

        회원_등록되어_있음("ADULT@nextstep.com", "password", 30);
        성인 = 로그인_되어_있음("ADULT@nextstep.com", "password");
    }

    @DisplayName("지하철 최단 경로를 관리")
    @Test
    void path() {
        // When 출발역과 도착역이 같은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외1 = 지하철_최단_경로_조회_요청(강남역.getId(), 강남역.getId(), 성인);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외1);

        // When 출발역과 도착역의 연결이 되어있지 않은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외2 = 지하철_최단_경로_조회_요청(강남역.getId(), 서울역.getId(), 성인);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외2);

        // When 존재하지 않은 출발역이나 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외3 = 지하철_최단_경로_조회_요청(혜화역.getId(), 서울역.getId(), 성인);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외3);

        // When 올바른 출발역과 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_결과 = 지하철_최단_경로_조회_요청(강남역.getId(), 남부터미널역.getId(), 성인);

        // Then 최단 경로 확인
        지하철_최단_경로_응답됨(지하철_최단_경로_조회_요청_결과);
        지하철_최단_경로_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), new PathResponse(Arrays.asList(강남역, 양재역, 남부터미널역), 12));

        // And 지하철 요금 확인
        지하철_요금_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), 1350);

        // When 같은 최단 경로 어린이 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_어린이_조회_요청_결과 = 지하철_최단_경로_조회_요청(강남역.getId(), 남부터미널역.getId(), 어린이);

        // Then 할인된 어린이 지하철 요금 확인
        지하철_요금_확인(지하철_최단_경로_어린이_조회_요청_결과.as(PathResponse.class), 500);

        // When 같은 최단 경로 청소년 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_청소년_조회_요청_결과 = 지하철_최단_경로_조회_요청(강남역.getId(), 남부터미널역.getId(), 청소년);

        // Then 할인된 청소년 지하철 요금 확인
        지하철_요금_확인(지하철_최단_경로_청소년_조회_요청_결과.as(PathResponse.class), 800);
    }
}
