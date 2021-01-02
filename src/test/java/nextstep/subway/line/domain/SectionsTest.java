package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 구간들에 대한 테스트")
class SectionsTest {

    private Sections sections;

    private Line 일호선;
    private Station 청량리역;
    private Station 신도림역;
    private Station 금정역;
    private Station 당정역;

    @BeforeEach
    void setUp() {
        청량리역 = new Station("청량리역");
        신도림역 = new Station("신도림역");
        금정역 = new Station("금정역");
        당정역 = new Station("당정역");

        일호선 = Line.builder()
                .name("일호선")
                .color("blue")
                .upStation(청량리역)
                .downStation(당정역)
                .distance(100)
                .build();

        sections = new Sections(Arrays.asList(
                new Section(일호선, 금정역, 당정역, 10),
                new Section(일호선, 신도림역, 금정역, 10),
                new Section(일호선, 청량리역, 신도림역, 10)
        ));
    }

    @DisplayName("지하철 구간을 상행 구간 기준으로 정렬한다.")
    @Test
    void orderedStations() {
        // when
        List<Station> orderedStations = sections.getOrderedStations();

        // then
        assertThat(orderedStations).containsExactly(청량리역, 신도림역, 금정역, 당정역);
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void add() {
        // given
        Station 관악역 = new Station("관악역");
        Section section = new Section(일호선, 신도림역, 관악역, 10);

        // when
        sections.add(section);

        // then
        assertThat(sections.getOrderedStations()).containsExactly(청량리역, 신도림역, 관악역, 금정역, 당정역);
    }

    @DisplayName("이미 등록된 지하철 노선 구간은 추가할 수 없다.")
    @Test
    void addDuplicated() {
        // given
        Section duplicated = new Section(일호선, 청량리역, 신도림역, 5);
        Section duplicated2 = new Section(일호선, 청량리역, 금정역, 5);

        // when // then
        assertThrows(RuntimeException.class, () -> sections.add(duplicated));
        assertThrows(RuntimeException.class, () -> sections.add(duplicated2));
    }

    @DisplayName("등록된 지하철 노선 구간과 연결되어 있지 않은 구간은 추가할 수 없다.")
    @Test
    void addNotConnect() {
        // given
        Station 구로역 = new Station("구로역");
        Station 금천구청역 = new Station("금천구청역");
        Section notConnected = new Section(일호선, 구로역, 금천구청역, 5);

        // when // then
        assertThrows(RuntimeException.class, () -> sections.add(notConnected));
    }

    @DisplayName("기존 지하철 구간 거리와 같거나 큰 구간은 추가할 수 없다.")
    @Test
    void addOverDistance() {
        // given
        Station 구로역 = new Station("구로역");
        Section overDistance = new Section(일호선, 신도림역, 구로역, 1000);

        // when // then
        assertThrows(RuntimeException.class, () -> sections.add(overDistance));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void remove() {
        // when
        sections.remove(청량리역);
        sections.remove(금정역);

        // then
        assertThat(sections.getOrderedStations()).containsExactly(신도림역, 당정역);
    }

    @DisplayName("지하철역 제거 시 구간이 하나이면 제거할 수 없다.")
    @Test
    void removeFail1() {
        // given
        sections.remove(청량리역);
        sections.remove(금정역);

        // when / then
        assertThrows(RuntimeException.class, () -> sections.remove(청량리역));
    }
}
