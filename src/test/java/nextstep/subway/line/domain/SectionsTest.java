package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.Fixture.createSection;
import static nextstep.subway.Fixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    @DisplayName("노선에 포함된 지하철역 리스트 조회 작업을 성공한다")
    @Test
    void getStations() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));

        assertThat(sections.getStations())
                .containsExactly(서울역, 종각역);
    }

    @DisplayName("구간리스트에 구간 추가 작업을 성공한다")
    @Test
    void addSection() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));

        sections.add(new Section(서울역, 시청역, 5), (section) -> new Line().addSection(section));

        assertThat(sections.count()).isEqualTo(2);
    }

    @DisplayName("구간리스트의 구간 제거 작업을 성공한다")
    @Test
    void removeSection() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));
        sections.add(createSection(종각역, 시청역, 10), (section -> new Line().addSection(section)));

        sections.remove(시청역.getId(), (section) -> new Line().addSection(section));

        assertThat(sections.count()).isEqualTo(1);
    }

    @DisplayName("구간리스트에서 같은 UpStation 찾는 작업에 성공한다")
    @Test
    void findSameUpStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 신설동역 = createStation("신설동역", 3L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));

        Section section = sections.findSameUpStation(new Section(서울역, 신설동역, 3));

        assertThat(section).isNotNull();
    }

    @DisplayName("구간리스트에서 같은 DownStation 찾는 작업에 성공한다")
    @Test
    void findSameDownStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 신설동역 = createStation("신설동역", 3L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));

        Section section = sections.findSameDownStation(new Section(신설동역, 종각역, 3));

        assertThat(section).isNotNull();
    }

    @DisplayName("구간리스트에서 Station 조회작업에 성공한다")
    @Test
    void findStationById() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);

        Sections sections = new Sections(createSection(서울역, 종각역, 10));

        assertThat(sections.findStationById(1L)).isEqualTo(서울역);
    }

    @DisplayName("구간리스트에서 요청 ID에 해당하는 Station이 없으면 IllegalArgumentException을 반환한다")
    @Test
    void findStationByIdWithException() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Sections sections = new Sections(createSection(서울역, 종각역, 10));
        long findingId = 7L;

        assertThatThrownBy(() -> sections.findStationById(findingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("%d", findingId)
                .hasMessageContaining("에 해당하는 Station을 찾을 수 없습니다.");
    }
}
