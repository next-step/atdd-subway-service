package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}
