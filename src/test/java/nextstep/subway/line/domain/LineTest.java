package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    void create_noSection() {
        // given
        final Line line = new Line("신분당선", "bg-red-600");

        // when
        lineRepository.save(line);

        // then
        assertAll(
            () -> assertThat(line.getId()).isNotNull(),
            () -> assertThat(line.getName()).isEqualTo("신분당선"),
            () -> assertThat(line.getColor()).isEqualTo("bg-red-600"),
            () -> assertThat(line.getSections()).isEmpty()
        );
    }

    @Test
    void create_withSection() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 12);

        // when
        lineRepository.save(line);

        // then
        assertAll(
            () -> assertThat(line.getId()).isNotNull(),
            () -> assertThat(line.getName()).isEqualTo("신분당선"),
            () -> assertThat(line.getColor()).isEqualTo("bg-red-600"),
            () -> assertThat(line.getSections().size()).isEqualTo(1)
        );
    }
}
