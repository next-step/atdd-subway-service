package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.station.domain.Station;

class SectionsTest {

    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;

    private Line 신분당선;
    private Station 판교역;
    private Station 정자역;
    private Station 미금역;
    private Station 동천역;
    private Distance distance10;
    private Distance distance5;
    private Section 판교_정자_구간;
    private Section 정자_미금_구간;
    private Section 미금_동천_구간;

    @BeforeEach
    void setUp() {
        신분당선 = Line.from(1L);
        판교역 = Station.of(1L, "판교역");
        정자역 = Station.of(2L, "정자역");
        미금역 = Station.of(3L, "미금역");
        동천역 = Station.of(4L, "동천역");
        distance10 = Distance.from(DISTANCE_10);
        distance5 = Distance.from(DISTANCE_5);
        판교_정자_구간 = Section.of(신분당선, 판교역, 정자역, distance10);
        정자_미금_구간 = Section.of(신분당선, 정자역, 미금역, distance10);
        미금_동천_구간 = Section.of(신분당선, 미금역, 동천역, distance10);
    }

    @DisplayName("Sections 을 Section 목록으로 생성한다.")
    @Test
    void create() {
        // when & then
        assertThatNoException().isThrownBy(
            () -> Sections.from(Arrays.asList(판교_정자_구간, 정자_미금_구간, 미금_동천_구간)));
    }

    @DisplayName("상행에서 하행순으로 지하철 역 목록을 반환한다.")
    @Test
    void sortStations() {
        // given
        Sections sections = Sections.from(Arrays.asList(정자_미금_구간, 미금_동천_구간, 판교_정자_구간));

        // when
        List<Station> stations = sections.getSortedStations();

        // then
        assertEquals(stations, Arrays.asList(판교역, 정자역, 미금역, 동천역));
    }

    @DisplayName("상행 종점역을 추가한다.")
    @Test
    void addSection1() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Section newSection = Section.of(신분당선, 서울역, 판교역, distance10);
        sections.add(newSection);

        // then
        assertEquals(sections.findFirstSection(), newSection);
    }

    @DisplayName("하행 종점역을 추가한다.")
    @Test
    void addSection2() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Section newSection = Section.of(신분당선, 동천역, 서울역, distance10);
        sections.add(newSection);

        // then
        assertEquals(sections.findLastSection(), newSection);
    }

    @DisplayName("중간역을 추가한다.")
    @Test
    void addSection3() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Section newSection = Section.of(신분당선, 정자역, 서울역, distance5);
        sections.add(newSection);

        // then
        assertEquals(sections.findMiddleSection(newSection), newSection);
    }

    @DisplayName("이미 존재하는 상/하행역으로 구성된 구간은 추가할 수 없다.")
    @Test
    void addSection4() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);

        Sections sections = Sections.from(sectionsList);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.add(판교_정자_구간))
                                            .withMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("기존 구간에 포함이 안되는 상/하행역으로 구성된 구간은 추가할 수 없다.")
    @Test
    void addSection5() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Section newSection = Section.of(신분당선, 동천역, 서울역, distance5);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.add(newSection))
                                            .withMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("추가하는 구간의 길이가 기존 구간보다 크면 추가할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 20, 30})
    void addSection6(int distanceValue) {
        // given
        Distance distance = Distance.from(distanceValue);
        Station 서울역 = Station.of(5L, "서울역");
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Section newSection = Section.of(신분당선, 정자역, 서울역, distance);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.add(newSection))
                                            .withMessageContaining("새로운 구간의 길이가 기존 구간길이보다 크거나 같습니다.");
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void removeMiddleStation() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        sections.removeMiddleStation(정자역);

        // then
        assertFalse(sections.contains(정자역));
    }

    @DisplayName("상행 종점역을 제거한다.")
    @Test
    void removeFirstEndStation() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        sections.removeEndStation(판교역);

        // then
        assertFalse(sections.contains(판교역));
    }

    @DisplayName("하행 종점역을 제거한다.")
    @Test
    void removeLastEndStation() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        sections.removeEndStation(동천역);

        // then
        assertFalse(sections.contains(동천역));
    }

    @DisplayName("구간이 1개만 존재할 때 역을 삭제할 수 없다.")
    @Test
    void removeStationWhenOnlySection() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);

        Sections sections = Sections.from(sectionsList);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.removeEndStation(판교역))
                                            .withMessageContaining("노선의 구간이 1개인 경우, 지하철 역을 삭제 할 수 없습니다.");
    }

    @DisplayName("존해하지 않는 종점역을 삭제할 수 없다.")
    @Test
    void removeNonExistentEndStation() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Station 수원역 = Station.of(10L, "수원역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.removeEndStation(수원역))
                                            .withMessageContaining("존재하지 않는 지하철 역입니다.");
    }

    @DisplayName("존해하지 않는 중간역을 삭제할 수 없다.")
    @Test
    void removeNonExistentMiddleStation() {
        // given
        List<Section> sectionsList = new ArrayList<>();
        sectionsList.add(판교_정자_구간);
        sectionsList.add(정자_미금_구간);
        sectionsList.add(미금_동천_구간);

        Sections sections = Sections.from(sectionsList);

        // when
        Station 수원역 = Station.of(10L, "수원역");

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> sections.removeEndStation(수원역))
                                            .withMessageContaining("존재하지 않는 지하철 역입니다.");
    }
}