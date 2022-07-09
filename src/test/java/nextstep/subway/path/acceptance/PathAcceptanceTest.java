package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberRequests.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathRequests.로그인_후_최단경로_조회_요청;
import static nextstep.subway.path.acceptance.PathRequests.최단경로_조회_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final Integer INFANT_AGE = 5;
    private static final String INFANT_EMAIL = "infant@google.com";
    private static final Integer CHILD_AGE = 10;
    private static final String CHILD_EMAIL = "child@google.com";
    private static final Integer ADOLESCENT_AGE = 15;
    private static final String ADOLESCENT_EMAIL = "adolescent@google.com";
    private static final Integer ADULT_AGE = 20;
    private static final String ADULT_EMAIL = "adult@google.com";
    private static final String PASSWORD = "password";

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
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 25, 1000)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10, 500)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("3호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 20)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 5);
    }

    @Test
    @DisplayName("최단경로 조회 시 출발역부터 도착지까지의 역을 순서대로 출력한다.")
    void findShortestPath() {
        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(양재역.getId(), 교대역.getId());

        // then
        최단경로_조회됨(최단경로, Arrays.asList(양재역, 남부터미널역, 교대역));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 최단경로 조회를 할 수 없다.")
    void findShortestPathFailWhenSameSourceTargetStation() {
        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(양재역.getId(), 양재역.getId());

        // then
        같은_역_최단경로_조회_실패됨(최단경로);
    }

    @Test
    @DisplayName("출발역이나 도착역이 존재하지 않는 경우 최단경로 조회를 할 수 없다")
    void findShortestPathFailWhenNotExistingStation() {
        // given
        StationResponse 미등록역 = StationResponse.of(new Station("미등록역"));

        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(미등록역.getId(), 양재역.getId());

        // then
        존재하지_않는_역_최단경로_조회_실패됨(최단경로);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않는 경우 최단경로 조회를 할 수 없다.")
    void findShortestPathFailWhenNotConnected() {
        // given
        StationResponse 정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        StationResponse 서현역 = 지하철역_등록되어_있음("서현역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("분당선", "bg-yellow-600", 정자역.getId(), 서현역.getId(), 1000)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 최단경로 = 최단경로_조회_요청(정자역.getId(), 양재역.getId());

        // then
        연결되지_않은_역_최단경로_조회_실패됨(최단경로);
    }

    @Test
    @DisplayName("노선의 추가요금이 계산되어 조회된다.(미로그인)")
    void calculateFareWithLineAdditionalFare() {
        // when
        ExtractableResponse<Response> 이호선_이용_경로 = 최단경로_조회_요청(교대역.getId(), 강남역.getId());

        // then
        요금_조회됨(이호선_이용_경로, 1750);       // 1,250(기본요금) + 500(2호선 추가요금)
    }

    @Test
    @DisplayName("첫번째 추가요금 구간 요금이 계산되어 조회된다.(미로그인)")
    void calculateFareWithFirstAdditionalFareDistance() {
        // when
        ExtractableResponse<Response> 첫번째_추가요금_경로_20Km = 최단경로_조회_요청(양재역.getId(), 교대역.getId());

        // then
        // 10km 초과시 첫번째 추가요금 구간 : 5km 당 100원
        요금_조회됨(첫번째_추가요금_경로_20Km, 1450);         // 1,250(기본요금) + 200 (거리별 추가요금)
    }

    @Test
    @DisplayName("두번째 추가요금 구간 요금이 계산되어 조회된다.(미로그인)")
    void calculateFareWithSecondAdditionalFareDistance() {
        // given
        StationResponse 대화역 = 지하철역_등록되어_있음("대화역").as(StationResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 대화역, 교대역, 60);

        // when
        ExtractableResponse<Response> 두번째_추가요금_경로_65Km = 최단경로_조회_요청(대화역.getId(), 남부터미널역.getId());

        // then
        // 10km 초과시 첫번째 추가요금 구간 : 5km 당 100원
        // 50km 초과시 두번째 추가요금 구간 : 8km 당 100원
        요금_조회됨(두번째_추가요금_경로_65Km, 2250);         // 1,250(기본요금) + 1,000 (거리별 추가요금)
    }

    @Test
    @DisplayName("로그인 된 경우 연령별로 요금이 계산되어 조회된다: 유아")
    void calculateFareOfInfantWhenLoggedIn() {
        // given
        회원_생성을_요청(INFANT_EMAIL, PASSWORD, INFANT_AGE);
        TokenResponse 유아_회원 = 로그인_요청(INFANT_EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 유아_회원_최단경로 = 로그인_후_최단경로_조회_요청(유아_회원, 교대역.getId(), 남부터미널역.getId());

        // then (유아: 0원)
        요금_조회됨(유아_회원_최단경로, 0);
    }

    @Test
    @DisplayName("로그인 된 경우 연령별로 요금이 계산되어 조회된다: 어린이")
    void calculateFareOfChildWhenLoggedIn() {
        // given
        회원_생성을_요청(CHILD_EMAIL, PASSWORD, CHILD_AGE);
        TokenResponse 어린이_회원 = 로그인_요청(CHILD_EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 어린이_회원_최댠경로 = 로그인_후_최단경로_조회_요청(어린이_회원, 교대역.getId(), 남부터미널역.getId());

        // then (어린이 : 일반 운임에서 350원을 제하고 50% 할인)
        요금_조회됨(어린이_회원_최댠경로, 450);
    }

    @Test
    @DisplayName("로그인 된 경우 연령별로 요금이 계산되어 조회된다: 청소년")
    void calculateFareOfAdolescentWhenLoggedIn() {
        // given
        회원_생성을_요청(ADOLESCENT_EMAIL, PASSWORD, ADOLESCENT_AGE);
        TokenResponse 청소년_회원 = 로그인_요청(ADOLESCENT_EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 청소년_회원_최댠경로 = 로그인_후_최단경로_조회_요청(청소년_회원, 교대역.getId(), 남부터미널역.getId());

        // then (청소년: 일반 운임에서 350원을 제하고 20% 할인)
        요금_조회됨(청소년_회원_최댠경로, 720);
    }

    @Test
    @DisplayName("로그인 된 경우 연령별 거리별 노선별로 요금이 계산되어 조회된다: 어른")
    void calculateFareOfAdultWhenLoggedIn() {
        // given
        회원_생성을_요청(ADULT_EMAIL, PASSWORD, ADULT_AGE);
        TokenResponse 어른_회원 = 로그인_요청(ADULT_EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 어른_회원_최댠경로 = 로그인_후_최단경로_조회_요청(어른_회원, 교대역.getId(), 남부터미널역.getId());

        // then
        요금_조회됨(어른_회원_최댠경로, 1250);
    }

    private static void 최단경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations) {
        List<StationResponse> stations = response.as(PathResponse.class).getStations();
        List<Long> actualIds = stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = expectStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds)
                .containsExactlyElementsOf(expectedIds);
    }

    private static void 같은_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 존재하지_않는_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private static void 연결되지_않은_역_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 요금_조회됨(ExtractableResponse<Response> response, int expectedFare) {
        int fare = response.as(PathResponse.class).getFare();

        assertThat(fare)
                .isEqualTo(expectedFare);
    }
}
