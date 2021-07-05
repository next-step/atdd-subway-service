package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.auth.acceptance.AuthSteps.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성을_요청;
import static nextstep.subway.path.PathSteps.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

// Feature
@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "oper912@naver.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 32;
    private static final String TEEN_EMAIL = "teen@naver.com";
    private static final Integer TEEN_AGE = 16;
    private static final String CHILD_EMAIL = "child@naver.com";
    private static final Integer CHILD_AGE = 10;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선*(10) ---   강남역
     * |                             |
     * *3호선*(3)                   *신분당선*(10)
     * |                             |
     * 남부터미널역  --- *3호선*(2) ---   양재
     *
     * 방이역    --- *5호선*(10) ---   오금역
     */
    // Background
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given: 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // and: 지하철 노선 등록되어 있음
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        // and: 지하철 노선에 추가로 구간 등록
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    // Scenario
    @DisplayName("지하철 최단 경로를 조회한다.")
    @Test
    void scenario1() {
        // when: 최단 경로 조회 요청
        ExtractableResponse<Response> 조회된_최단_경로 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        // and: 지하철 이용 요금도 함께 조회됨
        지하철_이용_요금_함께_조회됨(조회된_최단_경로, 1_250);

        // when: 최단 경로를 가지는 신규 노선 구간 등록 되어있음
        지하철_노선_등록되어_있음(new LineRequest("최단경로노선", "bg-black-600", 교대역.getId(), 양재역.getId(), 2)).as(LineResponse.class);
        // and: 최단 경로 조회 요청
        ExtractableResponse<Response> 조회된_신규_최단_경로 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(조회된_신규_최단_경로, Arrays.asList(교대역, 양재역), 2);
        // and: 지하철 이용 요금도 함께 조회됨
        지하철_이용_요금_함께_조회됨(조회된_신규_최단_경로, 1_250);

        // when: 새로운 노선 등록됨
        StationResponse 방이역 = 지하철역_등록되어_있음("방이역").as(StationResponse.class);
        StationResponse 오금역 = 지하철역_등록되어_있음("오금역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("5호선", "bg-보라색-600", 방이역.getId(), 오금역.getId(), 10)).as(LineResponse.class);
        // and: 연결된 구간으로 갈 수 없는 목적지 최단 경로 조회
        ExtractableResponse<Response> 목적지_역으로_이동_불가능한_최단_경로_조회됨 = 최단_경로_조회_요청(교대역.getId(), 오금역.getId());
        // then: 최단 경로 조회에 실패
        연결되지_않은_구간으로_최단_경로_조회_실패(목적지_역으로_이동_불가능한_최단_경로_조회됨);

        // when: 존재하지 않는 역을 목적지로 최단 경로 조회
        Long 존재하지않는_역_ID = 10000L;
        ExtractableResponse<Response> 존재하지_않는_목적지_역으로_최단_경로_조회됨 = 최단_경로_조회_요청(교대역.getId(), 존재하지않는_역_ID);
        // then: 최단 경로 조회에 실패
        등록되지_않은_역들로_최단_경로_조회_실패(존재하지_않는_목적지_역으로_최단_경로_조회됨);
    }

    @DisplayName("지하철 최단 경로를 조회한다. (추가 요금이 있는 노선 이용)")
    @Test
    void scenario2() {
        // given: 지하철역 등록되어 있음
        StationResponse 시작역 = 지하철역_등록되어_있음("시작역").as(StationResponse.class);
        StationResponse 중간역 = 지하철역_등록되어_있음("중간역").as(StationResponse.class);
        StationResponse 끝역 = 지하철역_등록되어_있음("끝역").as(StationResponse.class);
        // and: 지하철 노선 등록되어 있음 (추가요금 있는 노선, 없는 노선)
        지하철_노선_등록되어_있음(new LineRequest("추가요금노선", "bg-black-600", 시작역.getId(), 중간역.getId(), 5, 1_000)).as(LineResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("노선", "bg-white-600", 중간역.getId(), 끝역.getId(), 5)).as(LineResponse.class);

        // when: 추가요금이 있는 노선과 없는 노선을 경유하는 경로 조회
        ExtractableResponse<Response> 조회된_최단_경로 = 최단_경로_조회_요청(시작역.getId(), 끝역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(조회된_최단_경로, Arrays.asList(시작역, 중간역, 끝역), 10);
        // and: 지하철 이용 요금도 함께 조회됨 (이용요금 1,250원에 노선 추가요금 1,000원이 더해진 요금이 조회됨)
        지하철_이용_요금_함께_조회됨(조회된_최단_경로,2_250);

        // given: 지하철역 등록되어 있음
        StationResponse 새끝역 = 지하철역_등록되어_있음("새끝역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("저렴한추가요금노선", "bg-grey-600", 중간역.getId(), 새끝역.getId(), 5, 500)).as(LineResponse.class);

        // when: 추가요금이 모두 있는 노선을 경유하는 경로 조회
        ExtractableResponse<Response> 조회된_모든_노선_추가요금_최단_경로 = 최단_경로_조회_요청(시작역.getId(), 새끝역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(조회된_모든_노선_추가요금_최단_경로, Arrays.asList(시작역, 중간역, 새끝역), 10);
        // and: 지하철 이용 요금도 함께 조회됨 (이용요금 1,250 원에 추가요금이 더 비싼 노선의 추가요금 1,000원이 더해진 요금이 조회됨)
        지하철_이용_요금_함께_조회됨(조회된_모든_노선_추가요금_최단_경로,2_250);
    }

    @DisplayName("지하철 최단 경로를 조회한다. (로그인 사용자의 경우 연령별 요금 할인 적용)")
    @Test
    void scenario3() {
        // given: 성인 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // and: 성인 회원 로그인 되어 있음
        TokenResponse 성인_회원 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        // when: 성인 회원으로 로그인 후 최단 경로 조회
        ExtractableResponse<Response> 조회된_최단_경로 = 로그인_후_최단_경로_조회_요청(성인_회원, 교대역.getId(), 양재역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        // and: 지하철 이용 요금도 함께 조회됨 (기본적인 요금 1,250 원이 조회됨)
        지하철_이용_요금_함께_조회됨(조회된_최단_경로,1_250);

        // given: 청소년 회원 등록되어 있음
        회원_생성을_요청(TEEN_EMAIL, PASSWORD, TEEN_AGE);
        // and: 청소년 회원 로그인 되어 있음
        TokenResponse 청소년_회원 = 로그인_요청(TEEN_EMAIL, PASSWORD).as(TokenResponse.class);

        // when: 청소년 회원으로 로그인 후 최단 경로 조회
        ExtractableResponse<Response> 청소년_회원_조회된_최단_경로 = 로그인_후_최단_경로_조회_요청(청소년_회원, 교대역.getId(), 양재역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(청소년_회원_조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        // and: 지하철 이용 요금도 함께 조회됨 (기본 요금에서 350원을 공제한 금액에 20%를 할인한 금액이 조회됨)
        지하철_이용_요금_함께_조회됨(청소년_회원_조회된_최단_경로,720);

        // given: 어린이 회원 등록되어 있음
        회원_생성을_요청(CHILD_EMAIL, PASSWORD, CHILD_AGE);
        // and: 어린이 회원 로그인 되어 있음
        TokenResponse 어린이_회원 = 로그인_요청(CHILD_EMAIL, PASSWORD).as(TokenResponse.class);

        // when: 어린이 회원으로 로그인 후 최단 경로 조회
        ExtractableResponse<Response> 어린이_회원_조회된_최단_경로 = 로그인_후_최단_경로_조회_요청(어린이_회원, 교대역.getId(), 양재역.getId());
        // then: 최단 경로가 조회됨
        최단_경로_조회됨(어린이_회원_조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        // and: 지하철 이용 요금도 함께 조회됨 (기본 요금에서 350원을 공제한 금액에 50%를 할인한 금액이 조회됨)
        지하철_이용_요금_함께_조회됨(어린이_회원_조회된_최단_경로,450);
    }

    @DisplayName("최단 경로를 조회한다. (목적지로 가는 역을 순서대로 반환한다)")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> 조회된_최단_경로 = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        최단_경로_조회됨(조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        지하철_이용_요금_함께_조회됨(조회된_최단_경로, 1_250);
    }

    @DisplayName("연결되어있지 않은 출발역과 도착역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notConnectedSection() {
        // given
        StationResponse 방이역 = 지하철역_등록되어_있음("방이역").as(StationResponse.class);
        StationResponse 오금역 = 지하철역_등록되어_있음("오금역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("5호선", "bg-보라색-600", 방이역.getId(), 오금역.getId(), 10)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 조회된_최단_경로 = 최단_경로_조회_요청(교대역.getId(), 방이역.getId());

        // then
        연결되지_않은_구간으로_최단_경로_조회_실패(조회된_최단_경로);
    }

    @DisplayName("존재하지 않는 역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notFoundStation() {
        // given
        Long 존재하지않는_역_ID = 10000L;

        // when
        ExtractableResponse<Response> 조회된_최단_경로 = 최단_경로_조회_요청(교대역.getId(), 존재하지않는_역_ID);

        // then
        등록되지_않은_역들로_최단_경로_조회_실패(조회된_최단_경로);
    }

    private void 연결되지_않은_구간으로_최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 등록되지_않은_역들로_최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations, int expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> actualStationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(toList());

        List<Long> expectedStationIds = expectStations.stream()
                .map(StationResponse::getId)
                .collect(toList());

        assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void 지하철_이용_요금_함께_조회됨(ExtractableResponse<Response> response, int expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}
