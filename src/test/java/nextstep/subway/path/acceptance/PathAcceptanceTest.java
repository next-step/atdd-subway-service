package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTestMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestMethod.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인_성공_이후_토큰;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.로그인후_최단_경로_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.지하철_최단_경로_조회_요청_응답됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.지하철_최단_경로_조회됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.지하철_최단경로_조회_실패함;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.최단_경로_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.최단경로_거리_조회됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestMethod.최단경로_요금_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역      --- *2호선* ---   강남역
     * |                            |
     * *3호선*                    *신분당선*
     * |                            |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900);
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 400);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 500);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("교대에서 양재역으로 가는 최단 경로요청에 응답한다.")
    void pathTest01() {

        //when : 교대에서 양재역으로 가는 최단 거리를 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then : 요청에 응답한다.
        지하철_최단_경로_조회_요청_응답됨(최단_경로_조회_요청);
    }

    @Test
    @DisplayName("교대에서 양재역으로 가는 최단 경로를 조회한다.")
    void pathTest02() {

        //when : 교대에서 양재역으로 가는 최단 거리를 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then : 최단 거리가 조회된다.
        지하철_최단_경로_조회됨(최단_경로_조회_요청, Arrays.asList(교대역, 남부터미널역, 양재역));
    }

    @Test
    @DisplayName("교대에서 양재역으로 가는 최단 경로의 거리와 요금을 조회한다.")
    void pathTest03() {

        //when : 교대에서 양재역으로 가는 최단 거리를 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then : 최단 거리와 요금이 조회된다.
        지하철_최단_경로_조회됨(최단_경로_조회_요청, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단경로_거리_조회됨(최단_경로_조회_요청, 5);
        최단경로_요금_조회됨(최단_경로_조회_요청, 1750);
    }

    @Test
    @DisplayName("어린이가 교대에서 양재역으로 가는 최단 경로의 거리와 요금을 조회한다.")
    void pathTest04() {

        //given: 어린이 회원 등록
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 10;
        // 회원 등록
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // 로그인됨
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        String 로그인_토큰 = 로그인_성공_이후_토큰(로그인_요청_응답);

        //when : 교대에서 양재역으로 가는 최단 거리를 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 로그인후_최단_경로_조회_요청(로그인_토큰, 교대역.getId(), 양재역.getId());

        //then : 최단 거리와 운임 요금이 조회된다.
        지하철_최단_경로_조회됨(최단_경로_조회_요청, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단경로_거리_조회됨(최단_경로_조회_요청, 5);
        최단경로_요금_조회됨(최단_경로_조회_요청, 700);
    }

    @Test
    @DisplayName("청소년이 교대에서 양재역으로 가는 최단 경로의 거리와 요금을 조회한다.")
    void pathTest05() {

        //given: 청소년 회원 등록
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 15;
        // 회원 등록
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // 로그인됨
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        String 로그인_토큰 = 로그인_성공_이후_토큰(로그인_요청_응답);

        //when : 교대에서 양재역으로 가는 최단 거리를 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 로그인후_최단_경로_조회_요청(로그인_토큰, 교대역.getId(), 양재역.getId());

        //then : 최단 거리와 운임 요금이 조회된다.
        지하철_최단_경로_조회됨(최단_경로_조회_요청, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단경로_거리_조회됨(최단_경로_조회_요청, 5);
        최단경로_요금_조회됨(최단_경로_조회_요청, 1120);
    }

    @Test
    @DisplayName("같은 출발역과 도착역을 이용해 최단 경로를 조회한다.")
    void pathFailTest1() {

        //when : 양재역에서 양재역으로 가는 최단거리 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(양재역.getId(), 양재역.getId());

        //then : 조회 실패
        지하철_최단경로_조회_실패함(최단_경로_조회_요청);
    }

    @Test
    @DisplayName("노선에 존재 하지 않는 출발역을 이용해 경로를 조회한다.")
    void pathFailTest2() {

        //given
        StationResponse 도쿄역 = StationAcceptanceTest.지하철역_등록되어_있음("도쿄역").as(StationResponse.class);

        //when : 도쿄역에서 양재역으로 가는 최단거리 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(도쿄역.getId(), 양재역.getId());

        //then : 조회 실패
        지하철_최단경로_조회_실패함(최단_경로_조회_요청);
    }

    @Test
    @DisplayName("노선에 존재 하지 않는 도착역을 이용해 경로를 조회한다.")
    void pathFailTest3() {

        //given
        StationResponse 도쿄역 = StationAcceptanceTest.지하철역_등록되어_있음("도쿄역").as(StationResponse.class);

        //when : 교대역에서 도쿄역으로 가는 최단거리 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(교대역.getId(), 도쿄역.getId());

        //then : 조회 실패
        지하철_최단경로_조회_실패함(최단_경로_조회_요청);
    }

    @Test
    @DisplayName("연결이 되어 있지 않은 지하철역을 이용해 경로를 조회한다.")
    void pathFailTest4() {

        //given
        StationResponse 도쿄역 = StationAcceptanceTest.지하철역_등록되어_있음("도쿄역").as(StationResponse.class);
        StationResponse 쿄토역 = StationAcceptanceTest.지하철역_등록되어_있음("쿄토역").as(StationResponse.class);
        LineRequest 신칸센_요청 = new LineRequest("신칸센", "bg-blck-600", 도쿄역.getId(), 쿄토역.getId(), 40, 5000);

        //when : 교대역에서 도쿄역으로 가는 최단거리 조회한다.
        ExtractableResponse<Response> 최단_경로_조회_요청 = 최단_경로_조회_요청(교대역.getId(), 도쿄역.getId());

        //then : 조회 실패
        지하철_최단경로_조회_실패함(최단_경로_조회_요청);
    }


}
