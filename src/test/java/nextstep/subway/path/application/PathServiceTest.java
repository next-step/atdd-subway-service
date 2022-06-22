package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PathServiceTest {

    Line 일호선;
    Line 신분당선;

    Station 판교역;
    Station 성남역;
    Station 광교역;
    Station 인천역;
    Station 동인천역;
    Station 주안역;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        판교역 = stationRepository.save(new Station("판교역"));
        성남역 = stationRepository.save(new Station("성남역"));
        광교역 = stationRepository.save(new Station("광교역"));

        인천역 = stationRepository.save(new Station("인천역"));
        동인천역 = stationRepository.save(new Station("동인천역"));
        주안역 = stationRepository.save(new Station("주안역"));

        신분당선 = lineRepository.save(new Line("신분당선", "레드", 판교역, 성남역, 5));
        일호선 = lineRepository.save(new Line("일호선", "블루", 인천역, 동인천역, 5));

        sectionRepository.save(new Section(신분당선, 성남역, 판교역, 10));
        sectionRepository.save(new Section(신분당선, 판교역, 주안역, 10));

        일호선.addSection(new Section(일호선, 동인천역, 주안역, 10));
    }


}
