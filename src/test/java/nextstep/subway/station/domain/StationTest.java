package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StationTest {

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    EntityManager em;

    Station 강남역 = new Station("강남역");
    Station 모란역 = new Station("모란역");
    Station 광교역 = new Station("광교역");
    Station 판교역 = new Station("판교역");
    Line line = null;
    Line line2 = null;

    @BeforeEach
    void setUp() {
        line = new Line("노선1", "red", 강남역, 모란역, 10);
        line2 = new Line("노선2", "yellow", 광교역, 판교역, 15);
        lineRepository.save(line);
        lineRepository.save(line2);
        line.addSection(강남역, 판교역, 5);
        flushAndClear();
        강남역 = stationRepository.findById(강남역.getId()).get();
        모란역 = stationRepository.findById(모란역.getId()).get();
        광교역 = stationRepository.findById(광교역.getId()).get();
        판교역 = stationRepository.findById(판교역.getId()).get();
    }

    @DisplayName("해당 역이 속한 구간들을 탐색하여 중복을 제거한 모든 노선을 조회한다")
    @Test
    void getLinesInSections() {
        List<Station> stations = Arrays.asList(강남역, 모란역, 광교역, 판교역);
        Set<Line> lines = new HashSet<>();

        stations.forEach(station -> lines.addAll(station.getLinesInSections()));

        assertThat(lines).hasSize(2);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
