package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.domain.line.acceptance.LineAcceptanceTest;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.member.MemberAcceptanceTest;
import nextstep.subway.domain.path.PathAcceptanceTest;
import nextstep.subway.domain.station.StationAcceptanceTest;
import nextstep.subway.domain.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String accessToken;

    @Override
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-400", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-200", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        MemberAcceptanceTest.회원_생성을_요청("hongji3354@gmail.com", "hongji3354", 25);
        accessToken = MemberAcceptanceTest.토큰_발급("hongji3354", "hongji3354");
    }
}