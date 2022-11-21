package nextstep.subway.sections;

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

        Section section1 = new Section(newStation, downStation, 10);
        Section section2 = new Section(downStation, upStation, 5);

        Sections sections = new Sections(Arrays.asList(section1, section2));

        // when
        List<String> stations = sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stations).containsExactly("강남역", "양재역", "판교역");
    }

    @DisplayName("추가하려는 구간의 지하철 역이 모두 존재할 경우 예외가 발생한다")
    @Test
    void addBothExistStationException() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("양재역");

        Sections sections = new Sections(new ArrayList<>());
        Section section = new Section(upStation, downStation, 10);
        sections.add(section);

        Section newSection = new Section(upStation, downStation, 10);

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

        Section section = new Section(upStation, downStation, 10);
        sections.add(section);
        Section newSection = new Section(newUpStation, newDownStation, 10);

        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }
}
