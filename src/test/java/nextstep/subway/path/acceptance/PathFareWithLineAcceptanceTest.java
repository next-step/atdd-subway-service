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

import java.util.Arrays;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_생성_요청_및_검증;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestRequest.지하철_최단거리_요청_및_확인;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("지하철 경로 조회")
public class PathFareWithLineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private MemberRequest 성인_계정 = new MemberRequest("A@A.com", "qwe123", 30);

    private TokenRequest 성인_계정_토큰 = new TokenRequest("A@A.com", "qwe123");

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성됨(회원_생성을_요청(성인_계정));

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        지하철_노선_생성_요청_및_검증(new LineRequest("신분당선", "빨간색", 강남역.getId(), 양재역.getId(), 3, 2000))
                .as(LineResponse.class);
        지하철_노선_생성_요청_및_검증(new LineRequest("2호선", "초록색", 양재역.getId(), 정자역.getId(), 3, 1000))
                .as(LineResponse.class);
        지하철_노선_생성_요청_및_검증(new LineRequest("3호선", "주황색", 정자역.getId(), 광교역.getId(), 2, 500))
                .as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("가장 비싼 라인의 가격으로 계산이 된다.")
    Stream<DynamicTest> 가장_비싼_라인의_가격으로_계산이_된다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(성인_계정_토큰, authToken)),
                dynamicTest("강남역과 광교역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(authToken, 강남역, 광교역, Arrays.asList(강남역, 양재역, 정자역, 광교역), 8, 3250)),
                dynamicTest("광교역과 강남역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(authToken, 광교역, 강남역, Arrays.asList(광교역, 정자역, 양재역, 강남역), 8, 3250))
        );
    }
}
