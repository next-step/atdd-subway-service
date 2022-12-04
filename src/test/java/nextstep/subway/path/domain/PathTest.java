package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import javax.persistence.EntityManager;
import nextstep.subway.auth.domain.Age;
import nextstep.subway.auth.domain.discount.DiscountPolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PathTest {

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
        line = new Line("노선1", "red", 강남역, 모란역, 10, 900);
        line2 = new Line("노선2", "yellow", 광교역, 판교역, 15, 1000);
        lineRepository.save(line);
        lineRepository.save(line2);
        line.addSection(강남역, 판교역, 5);
        flushAndClear();
        강남역 = stationRepository.findById(강남역.getId()).get();
        모란역 = stationRepository.findById(모란역.getId()).get();
        광교역 = stationRepository.findById(광교역.getId()).get();
        판교역 = stationRepository.findById(판교역.getId()).get();
    }

    @DisplayName("최단 경로에 속한 역들을 받아 해당 역들이 속한 구간들을 탐색하여 중복을 제거한 모든 노선을 조회한다")
    @Test
    void createNoDuplicateLinesInSections() {
        Path path = new Path(Arrays.asList(강남역, 모란역, 광교역, 판교역), 10);

        Set<Line> 중복이_없는_노선 = path.createNoDuplicateLinesInSections();

        assertThat(중복이_없는_노선).hasSize(2);
    }

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다")
    @Test
    void addMaxExtraCostInLines() {
        Path path = new Path(Arrays.asList(강남역, 모란역, 광교역, 판교역), 12);

        path.addMaxExtraCostInLines();

        assertThat(path.getCost()).isEqualTo(2350);
    }

    @DisplayName("나이별로 적용되는 할인율이 달라진다")
    @ParameterizedTest
    @CsvSource(value = {"13:1150", "6:850", "20:1350"}, delimiter = ':')
    void applyDiscountPolicy(int ageParam, int expectedCost) {
        Path path = new Path(Arrays.asList(강남역, 모란역, 광교역, 판교역), 12);
        DiscountPolicy discountPolicy = new Age(ageParam).createDiscountPolicy();

        path.applyDiscountPolicy(discountPolicy);

        assertThat(path.getCost()).isEqualTo(expectedCost);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
