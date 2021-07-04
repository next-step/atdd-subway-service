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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 1500);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 1200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 1000);
        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-red-600", 서울역, 용산역, 20, 800);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원_등록되어_있음("CHILDREN@nextstep.com", "password", 6);
        회원_등록되어_있음("TEENAGER@nextstep.com", "password", 15);
        회원_등록되어_있음("ADULT@nextstep.com", "password", 30);
    }

    @DisplayName("로그인한 유저의 지하철 최단 경로를 관리")
    @ParameterizedTest
    @CsvSource({
            "CHILDREN@nextstep.com, password, 1250",    // 어린이 = (기본요금(1250) + 노선추가요금(1500) + 거리추가요금(100) - 어린이공제(350)) * 0.5
            "TEENAGER@nextstep.com, password, 2000",    // 청소년 = (기본요금(1250) + 노선추가요금(1500) + 거리추가요금(100) - 청소년공제(350)) * 0.2
            "ADULT@nextstep.com, password, 2850"        // 성인 = 기본요금(1250) + 노선추가요금(1500) + 거리추가요금(100)
    })
    void loginMemberPath(String email, String password, int fare) {
        // Given 사용자 로그인
        TokenResponse 사용자 = 로그인_되어_있음(email, password);

        // When 출발역과 도착역이 같은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외1 = 로그인_유저의_지하철_최단_경로_조회_요청(강남역.getId(), 강남역.getId(), 사용자);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외1);

        // When 출발역과 도착역의 연결이 되어있지 않은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외2 = 로그인_유저의_지하철_최단_경로_조회_요청(강남역.getId(), 서울역.getId(), 사용자);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외2);

        // When 존재하지 않은 출발역이나 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외3 = 로그인_유저의_지하철_최단_경로_조회_요청(혜화역.getId(), 서울역.getId(), 사용자);

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외3);

        // When 올바른 출발역과 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_결과 = 로그인_유저의_지하철_최단_경로_조회_요청(강남역.getId(), 남부터미널역.getId(), 사용자);

        // Then 최단 경로 확인
        지하철_최단_경로_응답됨(지하철_최단_경로_조회_요청_결과);
        지하철_최단_경로_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), new PathResponse(Arrays.asList(강남역, 양재역, 남부터미널역), 12));

        // And 지하철 요금 확인
        지하철_요금_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), fare);
    }

    @DisplayName("로그인하지 않은 유저의 지하철 최단 경로를 관리")
    @Test
    void guestPath() {
        // When 출발역과 도착역이 같은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외1 = 비로그인_지하철_최단_경로_조회_요청(강남역.getId(), 강남역.getId());

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외1);

        // When 출발역과 도착역의 연결이 되어있지 않은 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외2 = 비로그인_지하철_최단_경로_조회_요청(강남역.getId(), 서울역.getId());

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외2);

        // When 존재하지 않은 출발역이나 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외3 = 비로그인_지하철_최단_경로_조회_요청(혜화역.getId(), 서울역.getId());

        // Then 경로 조회 예외 발생됨
        지하철_최단_경로_예외_응답됨(지하철_최단_경로_조회_요청_예외3);

        // When 올바른 출발역과 도착역의 경로 조회 요청
        ExtractableResponse<Response> 지하철_최단_경로_조회_요청_결과 = 비로그인_지하철_최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // Then 최단 경로 확인
        지하철_최단_경로_응답됨(지하철_최단_경로_조회_요청_결과);
        지하철_최단_경로_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), new PathResponse(Arrays.asList(교대역, 남부터미널역, 양재역), 5));

        // And 지하철 요금 확인
        지하철_요금_확인(지하철_최단_경로_조회_요청_결과.as(PathResponse.class), 2250); // 기본요금(1250) + 3호선요금(1000)
    }
}
