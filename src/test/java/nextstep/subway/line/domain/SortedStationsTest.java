package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SortedStationsTest {
    @Test
    @DisplayName("섞여있어도 정렬이 되어야 한다")
    void 섞여있어도_정렬이_되어야_한다() {
        // given
        Station first = new Station("강남역");
        Station second = new Station("양재역");
        Station third = new Station("판교역");
        Station fourth = new Station("정자역");

        Section firstSection = new Section(null, first, second, 3);
        Section secondSection = new Section(null, second, third, 3);
        Section thirdSection = new Section(null, third, fourth, 3);

        List<Section> sections = Arrays.asList(thirdSection, firstSection, secondSection);

        // when
        SortedStations sortedStations = new SortedStations(sections);

        // then
        assertThat(sortedStations.toCollection())
                .containsExactly(first, second, third, fourth);
    }
}