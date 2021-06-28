package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TopSectionTest {
    @Test
    @DisplayName("섞여있어도 최상위 구간을 가지고 올 수 있다.")
    void getTopSection() {
        // given
        Station first = new Station("강남역");
        Station second = new Station("양재역");
        Station third = new Station("판교역");
        Station fourth = new Station("정자역");

        Section firstSection = new Section(null, first, second, 3);
        Section secondSection = new Section(null, second, third, 3);
        Section thirdSection = new Section(null, third, fourth, 3);

        List<Section> sections = Arrays.asList(thirdSection, secondSection, firstSection);

        // when
        TopSection topSection = new TopSection(sections);

        // then
        assertThat(topSection.getTopSection())
                .isEqualTo(firstSection);
    }

    @Test
    @DisplayName("Top Section이 없으면 hasTopSection은 false다")
    void Top_Section이_없으면_hasTopSection은_false다() {
        // given
        TopSection topSection = new TopSection(new ArrayList<>());

        // when
        boolean hasTopSection = topSection.hasTopSection();
        
        // then
        assertThat(hasTopSection)
                .isFalse();
    }

    @Test
    @DisplayName("Top Section이 있으면 hasTopSection은 true다")
    void Top_Section이_있으면_hasTopSection은_true다() {
        // given
        Station first = new Station("강남역");
        Station second = new Station("양재역");

        List<Section> sections = Arrays.asList(
                new Section(null, first, second, 3)
        );

        // when
        TopSection topSection = new TopSection(sections);

        // then
        assertThat(topSection.hasTopSection())
                .isTrue();
    }
}