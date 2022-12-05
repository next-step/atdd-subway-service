package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 라인 도메인 테스트")
@DataJpaTest
class LineTest {
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void getStationsTest() {
        Line line = new Line("신분당선", "bg-red-600");
        Station 강남 = new Station("강남");
        Station 양재 = new Station("양재");
        Station 양재시민의숲 = new Station("양재시민의숲");

        entityManager.persist(양재);
        entityManager.persist(양재시민의숲);
        entityManager.persist(강남);

        line.addSection(new Section(line, 양재, 양재시민의숲, 10));
        line.addSection(new Section(line, 강남, 양재, 10));

        assertThat(line.getStations().stream().map(it -> it.getName()).collect(Collectors.toList())).containsExactly("강남", "양재", "양재시민의숲");
    }
}