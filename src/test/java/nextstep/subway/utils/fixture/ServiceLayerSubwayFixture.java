package nextstep.subway.utils.fixture;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

public class ServiceLayerSubwayFixture {
    public final Station 강남역;
    public final Station 양재역;
    public final Station 교대역;
    public final Station 남부터미널역;
    public final Station 여의도역;
    public final Station 샛강역;
    public final Line 신분당선;
    public final Line 이호선;
    public final Line 삼호선;
    public final Line 구호선;
    public final int 강남역_양재역_간_거리 = 20;
    public final int 교대역_강남역_간_거리 = 10;
    public final int 교대역_남부터미널역_간_거리 = 10;
    public final int 남부터미널역_양재역_간_거리 = 5;
    public final int 여의도역_샛강역_간_거리 = 15;

    public ServiceLayerSubwayFixture(final StationRepository stationRepository, final LineRepository lineRepository) {
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        교대역 = stationRepository.save(new Station("교대역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        여의도역 = stationRepository.save(new Station("여의도역"));
        샛강역 = stationRepository.save(new Station("샛강역"));

        신분당선 = lineRepository.save(new Line("신분당선", "red", 강남역, 양재역, 강남역_양재역_간_거리));
        이호선 = lineRepository.save(new Line("이호선", "green", 교대역, 강남역, 교대역_강남역_간_거리));
        삼호선 = lineRepository.save(new Line("삼호선", "orange", 교대역, 양재역, 교대역_남부터미널역_간_거리 + 남부터미널역_양재역_간_거리));
        구호선 = lineRepository.save(new Line("구호선", "brown", 여의도역, 샛강역, 여의도역_샛강역_간_거리));
    }
}
