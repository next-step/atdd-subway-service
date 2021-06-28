package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.auth.acceptance.AuthRequestFixture.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_생성_요청_및_검증;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_등록_요청_및_확인;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestRequest.지하철_최단거리_요청_및_실패;
import static nextstep.subway.path.acceptance.PathAcceptanceTestRequest.지하철_최단거리_요청_및_확인;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성됨(회원_생성을_요청(성인_계정));
        회원_생성됨(회원_생성을_요청(청소년_계정));
        회원_생성됨(회원_생성을_요청(어린이_계정));

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = 지하철_노선_생성_요청_및_검증(new LineRequest("신분당선", "빨간색", 강남역.getId(), 양재역.getId(), 3))
                .as(LineResponse.class);
        이호선 = 지하철_노선_생성_요청_및_검증(new LineRequest("2호선", "초록색", 강남역.getId(), 정자역.getId(), 7))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_생성_요청_및_검증(new LineRequest("3호선", "주황색", 강남역.getId(), 광교역.getId(), 11))
                .as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("최단거리를 검색한다")
    Stream<DynamicTest> 최단거리를_검색한다() {
        // 신분당선 -> 강남역 -3- 양재역 -2- 정자역
        // 2호선   -> 강남역 -7- 정자역 -5- 광교역
        // 3호선   -> 강남역 -11- 광교역

        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("신분당선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 강남역, 양재역, 정자역)),
                dynamicTest("2호선에 광교역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(이호선, 정자역, 광교역, 5)),
                dynamicTest("2호선에 광교역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(이호선, 강남역, 정자역, 광교역)),
                dynamicTest("3호선에 강남역, 광교역을 확인한다", 지하철_노선에_지하철역_순서_정렬됨(삼호선, 강남역, 광교역)),
                dynamicTest("강남역과 광교역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 광교역, Arrays.asList(강남역, 양재역, 정자역, 광교역), 10))
        );
    }

    @TestFactory
    @DisplayName("같은 역끼리의 최단거리는 찾지 못한다")
    Stream<DynamicTest> 같은_역끼리의_최단거리는_찾지_못한다() {
        AuthToken authToken = new AuthToken();

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 정자역을 찾는다", 지하철_최단거리_요청_및_실패(authToken, 정자역, 정자역))
        );
    }

    @TestFactory
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않을경우 실패한다")
    Stream<DynamicTest> 출발역과_도착역이_연결이_되어_있지_않을경우_실패한다() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = StationAcceptanceTest.지하철역_등록되어_있음("쿄잉역").as(StationResponse.class);
        StationResponse 코잉역 = StationAcceptanceTest.지하철역_등록되어_있음("코잉역").as(StationResponse.class);
        지하철_노선_생성_요청_및_검증(new LineRequest("4호선", "보라색", 쿄잉역.getId(), 코잉역.getId(), 20));

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("강남역과 쿄잉역 찾는다", 지하철_최단거리_요청_및_실패(authToken, 강남역, 쿄잉역))
        );
    }

    @TestFactory
    @DisplayName("존재하지 않는 출발역이나 도착역으로 조회시 실패한다")
    Stream<DynamicTest> 존재하지_않는_출발역이나_도착역으로_조회시_실패한다() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = new StationResponse(1000L, "쿄잉역", LocalDateTime.now(), LocalDateTime.now());
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 쿄잉역을 찾는다", 지하철_최단거리_요청_및_실패(authToken, 정자역, 쿄잉역)),
                dynamicTest("쿄잉역과 정자역 찾는다", 지하철_최단거리_요청_및_실패(authToken, 쿄잉역, 정자역))
        );
    }

    @TestFactory
    @DisplayName("기본 요금이 부과된다 10km이하")
    Stream<DynamicTest> 기본_요금이_부과된다_10km이하() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = StationAcceptanceTest.지하철역_등록되어_있음("쿄잉역").as(StationResponse.class);

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 쿄잉역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 쿄잉역, 7)),
                dynamicTest("강남역과 쿄잉역 최단거리 및 성인 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 쿄잉역, Arrays.asList(강남역, 양재역, 쿄잉역), 10, 1250))
        );
    }

    @TestFactory
    @DisplayName("거리에 따라_과금이 된다 10km이상 50km이하")
    Stream<DynamicTest> 거리에_따라_과금이_된다_10km이상_50km이하() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = StationAcceptanceTest.지하철역_등록되어_있음("쿄잉역").as(StationResponse.class);

        int fare = 1250 + (40 / 5 * 100);

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 쿄잉역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 쿄잉역, 47)),
                dynamicTest("강남역과 쿄잉역 최단거리 및 성인 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 쿄잉역, Arrays.asList(강남역, 양재역, 쿄잉역), 50, fare))
        );
    }

    @TestFactory
    @DisplayName("거리에 따라 과금이 된다 50km초과")
    Stream<DynamicTest> 거리에_따라_과금이_된다_50km초과() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = StationAcceptanceTest.지하철역_등록되어_있음("쿄잉역").as(StationResponse.class);

        int fare = 1250 + (40 / 5 * 100) + (30 / 8 * 100);

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("신분당선에 쿄잉역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 쿄잉역, 77)),
                dynamicTest("강남역과 쿄잉역 최단거리 및 성인 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 쿄잉역, Arrays.asList(강남역, 양재역, 쿄잉역), 80, fare))
        );
    }

    @TestFactory
    @DisplayName("성인은 할인을 받지 않는다")
    Stream<DynamicTest> 성인은_할인을_받지_않는다() {
        AuthToken authToken = new AuthToken();

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("강남역과 정자역 최단거리 및 성인 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 정자역, Arrays.asList(강남역, 정자역), 7, 1250))
        );
    }

    @TestFactory
    @DisplayName("청소년은 운임 할인을 받는다 ((운임 - 350) * 0.8 ")
    Stream<DynamicTest> 청소년은_운임_할인을_받는다() {
        AuthToken authToken = new AuthToken();
        int fare = (int) ((1250 - 350) * 0.8);

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(청소년_계정_토큰, authToken)),
                dynamicTest("강남역과 정자역 최단거리 및 청소년 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 정자역, Arrays.asList(강남역, 정자역), 7, fare))
        );
    }

    @TestFactory
    @DisplayName("어린이는 운임 할인을 받는다 ((운임 - 350) * 0.5 ")
    Stream<DynamicTest> 어린이는_운임_할인을_받는다() {
        AuthToken authToken = new AuthToken();
        int fare = (int) ((1250 - 350) * 0.5);

        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(어린이_계정_토큰, authToken)),
                dynamicTest("강남역과 정자역 최단거리 및 어린이 운임을 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 정자역, Arrays.asList(강남역, 정자역), 7, fare))
        );
    }
}
