package nextstep.subway.line.domain.wrapper;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private static final Line 신분당선 = new Line("신분당선", "bg-red-600");
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 양재역 = Station.of(2L, "양재역");
    private static final Station 청계산입구역 = Station.of(3L, "청계산입구역");
    private static final Station 판교역 = Station.of(4L, "판교역");
    private static final Station 수지구청역 = Station.of(5L, "수지구청역");
    private static final Station 광교역 = Station.of(6L, "광교역");

    private Sections sections;

    @BeforeEach
    void setUp() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 청계산입구역, 판교역, 2));
        sections.add(new Section(신분당선, 강남역, 양재역, 2));
        sections.add(new Section(신분당선, 수지구청역, 광교역, 2));
        sections.add(new Section(신분당선, 양재역, 청계산입구역, 2));
        sections.add(new Section(신분당선, 판교역, 수지구청역, 2));
        this.sections = sections;
    }

    @DisplayName("상행역 -> 하행역으로 정렬된 역목록을 반환한다.")
    @Test
    void sortedStations() {
        // when
        Set<Station> sortedStations = sections.getSortedStations();

        // then
        assertThat(sortedStations).containsExactly(강남역, 양재역, 청계산입구역, 판교역, 수지구청역, 광교역);
    }
}
