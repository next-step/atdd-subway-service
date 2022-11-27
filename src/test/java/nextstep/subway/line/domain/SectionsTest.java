package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private Line 호선;
    private Station 강남역;
    private Station 잠실역;
    private Sections 구간;

    @BeforeEach
    void setUp() {
        호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        구간 = new Sections(Collections.singletonList(new Section(호선, 강남역, 잠실역, 5)));
    }

    @Test
    void 지하철역_구간_조회() {
        assertThat(구간.stations()).containsExactly(강남역, 잠실역);
    }
}
