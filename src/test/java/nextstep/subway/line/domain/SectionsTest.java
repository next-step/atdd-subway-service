package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    private Station 서초역;
    private Station 강남역;
    private Sections sections;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        강남역 = new Station("강남역");
        sections = new Sections(Collections.singletonList(new Section(서초역, 강남역, 10)));
    }

    @DisplayName("구간 목록에 할당된 지하철 역을 조회할 수 있다.")
    @Test
    void assignedStations() {
        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 강남역);
    }
}
