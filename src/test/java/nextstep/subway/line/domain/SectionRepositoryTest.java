package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    Line 일호선;
    Line 신분당선;

    Station 판교역;
    Station 성남역;
    Station 광교역;
    Station 인천역;
    Station 동인천역;
    Station 주안역;

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

    @Test
    @DisplayName("역이 구간을 조회 한다")
    void existSection() {
        List<Section> sections = sectionRepository
                .findAllByUpStationIdOrDownStationId(성남역.getId(), 주안역.getId());

        assertThat(sections).hasSize(3);
    }
}
