package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {
    @Test
    @DisplayName("섞여있어도 정렬하여 가져올 수 있다.")
    void 섞여있어도_정렬하여_가져올_수_있다() {
        // given
        Station first = new Station("강남역");
        Station second = new Station("양재역");
        Station third = new Station("판교역");
        Station fourth = new Station("정자역");

        Section firstSection = new Section(null, first, second, 3);
        Section secondSection = new Section(null, second, third, 3);
        Section thirdSection = new Section(null, third, fourth, 3);

        // when
        Line line = new Line("신분당", "RED", secondSection.getUpStation(), secondSection.getDownStation(), 3);
        line.getSections().add(thirdSection);
        line.getSections().add(firstSection);

        List<Station> stations = line.sortedStation();

        // then
        assertThat(stations)
                .containsExactly(first, second, third, fourth);
    }
}