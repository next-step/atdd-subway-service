package nextstep.subway.utils;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class PathTestUtils {

    @Autowired
    protected StationRepository stationRepository;

    @Autowired
    protected SectionRepository sectionRepository;
    
    @Autowired
    protected LineRepository lineRepository;

    protected Station 교대역;
    protected Station 남부터미널역;
    protected Station 강남역;
    protected Station 양재역;

    protected Line 신분당선;
    protected Line 이호선;
    protected Line 삼호선;

    @BeforeEach
    public void setUp() {
        createStation();
        createLine();
    }

    protected void createStation() {
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        stationRepository.saveAll(Arrays.asList(교대역, 남부터미널역, 강남역, 양재역));

    }

    protected void createLine() {
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);

        lineRepository.saveAll(Arrays.asList(신분당선, 이호선, 삼호선));
        addSection();
    }

    protected void addSection() {
        삼호선.addSection(교대역, 남부터미널역, 3);
    }
}
