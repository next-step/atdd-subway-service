package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @Test
    @DisplayName("구간들의 포함된 역들을 가져온다")
    void getStations() {

        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Section 구간 = new Section(잠실역, 문정역, 10);
        Sections sections = new Sections(Arrays.asList(구간));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).contains(잠실역, 문정역);
    }

    @Test
    @DisplayName("구간을 받아 추가")
    void addSection() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Section 구간 = new Section(잠실역, 문정역, 10);
        Sections sections = new Sections(Arrays.asList(구간));

        // given
        Station 가락시장역 = new Station("가락시장역");
        Section section = new Section(가락시장역, 문정역, 1);

        // when
        sections.add(section);

        // then
        assertThat(sections.get()).contains(section);
    }

    @Test
    @DisplayName("이미 존재하는 구간을 받아 추가하면 에러 발생")
    void addSection_이미_존재_에러_발생() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Section section = new Section(잠실역, 문정역, 10);
        Sections sections = new Sections(Arrays.asList(section));

        // expect
        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("기존 구간의 거리 이상의 구간을 받아 추가하면 에러 발생")
    void addSection_구간_거리_에러_발생() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Section section = new Section(잠실역, 문정역, 10);
        Sections sections = new Sections(Arrays.asList(section));

        // given
        Station 가락시장역 = new Station("가락시장역");
        Section newSection = new Section(가락시장역, 문정역, 11);

        // expect
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(RuntimeException.class);
    }

}
