package nextstep.subway.section;

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

class SectionTest {
    @DisplayName("상행선을 바꿀 수 있다.")
    @Test
    void updateUpStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, 10);

        Station newStation = new Station("양재역");
        Section newSection = new Section(upStation, newStation, 10);

        // when
        section.update(newSection);

        // then
        assertThat(section.getUpStation()).isEqualTo(newStation);
    }

    @DisplayName("하행선을 바꿀 수 있다.")
    @Test
    void updateDownStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, 10);

        Station newStation = new Station("양재역");
        Section newSection = new Section(newStation, downStation, 10);

        // when
        section.update(newSection);

        // then
        assertThat(section.getDownStation()).isEqualTo(newStation);
    }
}
