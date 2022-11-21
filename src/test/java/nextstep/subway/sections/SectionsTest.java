package nextstep.subway.sections;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    @DisplayName("정렬된 지하철 역 정보를 반환할 수 있다")
    @Test
    void sortStations() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");
        Station newStation = new Station("강남역");

        Section section1 = new Section(newStation, downStation, new Distance(10));
        Section section2 = new Section(downStation, upStation, new Distance(5));

        Sections sections = new Sections(Arrays.asList(section1, section2));

        // when
        List<String> stations = sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stations).containsExactly("강남역", "양재역", "판교역");
    }

    @DisplayName("노선의 구간을 추가할 수 있다")
    @Test
    void addStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");

        Sections sections = new Sections(new ArrayList<>());
        Section section = new Section(upStation, downStation, new Distance(10));
        sections.add(section);

        Station newStation = new Station("강남역");
        Section newSection = new Section(upStation, newStation, new Distance(5));

        // when
        sections.add(newSection);

        // then
        assertThat(sections.get()).contains(section, newSection);
    }

    @DisplayName("추가하려는 구간의 지하철 역이 모두 존재할 경우 예외가 발생한다")
    @Test
    void addBothExistStationException() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");

        Sections sections = new Sections(new ArrayList<>());
        Section section = new Section(upStation, downStation, new Distance(10));
        sections.add(section);

        Section newSection = new Section(upStation, downStation, new Distance(10));

        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("추가하려는 구간의 지하철 역이 모두 존재하지 않으면 예외가 발생한다")
    @Test
    void addBothNotExistStationException() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");
        Station newUpStation = new Station("존재하지않는역1");
        Station newDownStation = new Station("존재하지않는역2");

        Sections sections = new Sections(new ArrayList<>());

        Section section = new Section(upStation, downStation, new Distance(10));
        sections.add(section);
        Section newSection = new Section(newUpStation, newDownStation, new Distance(10));

        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상행성 하행선 모두 존재하지 않습니다.");
    }

    @DisplayName("노선의 지하철을 삭제할 수 있다")
    @Test
    void deleteStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");
        Station newStation = new Station("강남역");

        Section section1 = new Section(upStation, downStation, new Distance(10));
        Section section2 = new Section(newStation, downStation, new Distance(5));

        Sections sections = new Sections(new ArrayList<>());
        sections.add(section1);
        sections.add(section2);

        // when
        sections.delete(newStation);

        // then
        assertThat(sections.getStations()).containsOnly(upStation, downStation);
    }

    @DisplayName("노선에 존재하지 않는 지하철을 삭제할 경우 예외가 발생한다")
    @Test
    void deleteNotExistStationException() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");
        Station newStation = new Station("강남역");

        Section section1 = new Section(newStation, downStation, new Distance(10));
        Section section2 = new Section(downStation, upStation, new Distance(5));

        Sections sections = new Sections(new ArrayList<>());
        sections.add(section1);
        sections.add(section2);

        Station station = new Station("존재하지 않는 지하철 역");

        // then
        assertThatThrownBy(() -> sections.delete(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제하려는 지하철이 노선에 존재하지 않습니다");
    }

    @DisplayName("노선의 마지막 구간의 지하철을 삭제할 경우 예외가 발생한다")
    @Test
    void deleteOneSectionException() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");

        Section section = new Section(upStation, downStation, new Distance(10));

        Sections sections = new Sections(new ArrayList<>());
        sections.add(section);

        // then
        assertThatThrownBy(() -> sections.delete(upStation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("마지막 구간은 삭제할 수 없습니다.");
    }
}
