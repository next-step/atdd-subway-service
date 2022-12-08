package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인을_요청;
import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.line.acceptance.LineSectionTestFixture.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.LineTestFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberTestFixture.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathTestFixture.경로_조회_요청_성공됨;
import static nextstep.subway.path.acceptance.PathTestFixture.경로_조회_요청_실패됨;
import static nextstep.subway.path.acceptance.PathTestFixture.지하철_이용_요금에_할인_적용_응답됩;
import static nextstep.subway.path.acceptance.PathTestFixture.지하철_이용_요금이_응답됩;
import static nextstep.subway.path.acceptance.PathTestFixture.총_거리가_응답됨;
import static nextstep.subway.path.acceptance.PathTestFixture.최단_거리_경로가_응답됨;
import static nextstep.subway.path.acceptance.PathTestFixture.출발역에서_도착역_경로_조회됨;
import static nextstep.subway.path.acceptance.PathTestFixture.회원_출발역에서_도착역_경로_조회됨;

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
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역과 도착역의 최단거리를 조회된다.")
    @Test
    void find_path_none() {
        //when
        ExtractableResponse<Response> response = 출발역에서_도착역_경로_조회됨(교대역, 강남역);

        //then
        경로_조회_요청_성공됨(response);
        최단_거리_경로가_응답됨(response);
        총_거리가_응답됨(response);
        지하철_이용_요금이_응답됩(response);
    }


    @DisplayName("회원이 출발역과 도착역의 최단거리를 조회하면 회원 유형별 할인 정책이 적용된다.")
    @Test
    void find_path_member() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse token = 로그인을_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        //when
        ExtractableResponse<Response> response = 회원_출발역에서_도착역_경로_조회됨(교대역, 강남역, token);

        //then
        경로_조회_요청_성공됨(response);
        최단_거리_경로가_응답됨(response);
        총_거리가_응답됨(response);
        지하철_이용_요금에_할인_적용_응답됩(response, 450);
    }

    @DisplayName("출발역과 도착역이 다른 경우를 조회환다.")
    @Test
    void different_source_target() {
        //when
        ExtractableResponse<Response> response = 출발역에서_도착역_경로_조회됨(교대역, 교대역);

        //then
        경로_조회_요청_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결된 경우를 조회한다.")
    @Test
    void linked_source_target() {
        //given
        StationResponse 캠퍼스타운역 = StationAcceptanceTest.지하철역_등록되어_있음("캠퍼스타운역").as(StationResponse.class);
        StationResponse 지식정보단지역 = StationAcceptanceTest.지하철역_등록되어_있음("지식정보단지역").as(StationResponse.class);
        LineResponse 인천일호선 = 지하철_노선_등록되어_있음(new LineRequest("인천일호선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(인천일호선, 캠퍼스타운역, 지식정보단지역, 8);

        //when
        ExtractableResponse<Response> response = 출발역에서_도착역_경로_조회됨(교대역, 캠퍼스타운역);

        //then
        경로_조회_요청_실패됨(response);
    }

    @DisplayName("존재하는 출발역과 도착역을 조회한다.")
    @Test
    void exist_source_target() {
        //given
        StationResponse 캠퍼스타운역 = StationAcceptanceTest.지하철역_등록되어_있음("캠퍼스타운역").as(StationResponse.class);

        //when
        ExtractableResponse<Response> response = 출발역에서_도착역_경로_조회됨(교대역, 캠퍼스타운역);

        //then
        경로_조회_요청_실패됨(response);

    }
}
