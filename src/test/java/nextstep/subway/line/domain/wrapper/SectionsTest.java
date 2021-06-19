package nextstep.subway.line.domain.wrapper;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    private static final Line 신분당선 = new Line("신분당선", "bg-red-600");
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 양재역 = Station.of(2L, "양재역");
    private static final Station 청계산입구역 = Station.of(3L, "청계산입구역");
    private static final Station 판교역 = Station.of(4L, "판교역");
    private static final Station 수지구청역 = Station.of(5L, "수지구청역");
    private static final Station 광교역 = Station.of(6L, "광교역");

    private Sections sections;

    // 강남 - 양재 - 양재시민의숲 - 청계산입구 - 판교 - 정자 - 미금 - 동천 - 수지구청 - 성복 - 상현 - 광교중앙 - 광교
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

    @DisplayName("이미 등록된 역 구간을 등록할 수 없다.")
    @Test
    void addFail_alreadyExistsSection() {
        // given
        Section given1 = new Section(신분당선, 청계산입구역, 판교역, 8);
        Section given2 = new Section(신분당선, 강남역, 광교역, 8);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> sections.registerNewSection(given1)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> sections.registerNewSection(given2)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("역 사이에 새로운 역을 등록한다.")
    @Test
    void registerNewSection() {
        // given
        Station 양재시민의숲역 = Station.of(7L, "양재시민의숲역");
        Section given = new Section(신분당선, 양재역, 양재시민의숲역, 1);

        // when
        sections.registerNewSection(given);

        // then
        assertThat(sections.getSortedStations()).containsExactly(강남역, 양재역, 양재시민의숲역, 청계산입구역, 판교역, 수지구청역, 광교역);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 때 기존의 역 간격보다 큰 간격을 등록할 수 없다.")
    @Test
    void registerNewSectionFail() {
        // given
        Station 양재시민의숲역 = Station.of(7L, "양재시민의숲역");
        Section given = new Section(신분당선, 양재역, 양재시민의숲역, 10);

        // when & then
        assertThatThrownBy(() -> sections.registerNewSection(given))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
