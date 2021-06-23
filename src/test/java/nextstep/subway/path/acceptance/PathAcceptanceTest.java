package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_및_전체_검증;
import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.등록된_계정_토큰_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_생성_요청_및_검증;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_등록_요청_및_확인;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
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

        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));

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
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken)),
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
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken)),
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
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken)),
                dynamicTest("강남역과 쿄잉역 찾는다", 지하철_최단거리_요청_및_실패(authToken, 강남역, 쿄잉역))
        );
    }

    @TestFactory
    @DisplayName("존재하지 않는 출발역이나 도착역으로 조회시 실패한다")
    Stream<DynamicTest> 존재하지_않는_출발역이나_도착역으로_조회시_실패한다() {
        AuthToken authToken = new AuthToken();

        StationResponse 쿄잉역 = new StationResponse(1000L, "쿄잉역", LocalDateTime.now(), LocalDateTime.now());
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken)),
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 쿄잉역을 찾는다", 지하철_최단거리_요청_및_실패(authToken, 정자역, 쿄잉역)),
                dynamicTest("쿄잉역과 정자역 찾는다", 지하철_최단거리_요청_및_실패(authToken, 쿄잉역, 정자역))
        );
    }
}
