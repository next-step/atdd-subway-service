package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {

    @DisplayName("상행역과 하행역 equals 테스트")
    @Test
    void equalsStation() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        Section section = 신분당선.getSections().get(0);

        assertThat(section.equalsUpStation(강남역)).isTrue();
        assertThat(section.equalsDownStation(광교역)).isTrue();
    }
}
