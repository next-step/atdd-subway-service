package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PahTest {
    private Path path;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        source = new Station("강남역");
        target = new Station("양재역");

        path = new Path(source, target);
    }

    @Test
    void 같은_경로인지_확인한다() {
        // when
        boolean result = path.isSame(source, target);

        // then
        assertThat(result).isTrue();
    }
}
