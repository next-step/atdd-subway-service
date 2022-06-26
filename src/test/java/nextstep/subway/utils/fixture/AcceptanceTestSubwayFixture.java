package nextstep.subway.utils.fixture;

import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

public class AcceptanceTestSubwayFixture {
    public final StationResponse 강남역;
    public final StationResponse 양재역;
    public final StationResponse 교대역;
    public final StationResponse 남부터미널역;
    public final StationResponse 여의도역;
    public final StationResponse 샛강역;

    public final LineResponse 신분당선;
    public final LineResponse 이호선;
    public final LineResponse 삼호선;
    public final LineResponse 구호선;

    public AcceptanceTestSubwayFixture() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        샛강역 = StationAcceptanceTest.지하철역_등록되어_있음("샛강역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("삼호선", "orange", 교대역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                        new LineRequest("구호선", "brown", 여의도역.getId(), 샛강역.getId(), 10))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }
}
