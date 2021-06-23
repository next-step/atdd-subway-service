package nextstep.subway.line.domain;

import nextstep.subway.exception.LineHasNotExistSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SectionsTest {
    private Station first = new Station("FIRST");
    private Station second = new Station("SECOND");
    private Station third = new Station("THIRD");
    private Station fourth = new Station("FOURTH");

    private Section firstSecond = new Section(first, second, new Distance(10));
    private Section secondThird = new Section(second, third, new Distance(10));
    private Section thirdFourth = new Section(third, fourth, new Distance(10));

    @Test
    @DisplayName("Section의 존재여부를 확인할 수 있디")
    void Section의_존재여부를_확인할_수_있다() {
        Sections sections = new Sections();
        sections.add(firstSecond);
        sections.add(secondThird);
        sections.add(thirdFourth);

        assertThat(sections.containsSection(new StationPair(second, third)))
                .isTrue();

        assertThat(sections.containsSection(new StationPair(third, second)))
                .isTrue();

        assertThat(sections.containsSection(new StationPair(second, fourth)))
                .isFalse();
    }

    @Test
    @DisplayName("SectionPair를 통해 Section을 가져올 수 있다.")
    void SectionPair를_통해_Section을_가져올_수_있다() {
        Sections sections = new Sections();
        sections.add(firstSecond);
        sections.add(secondThird);
        sections.add(thirdFourth);

        assertThat(sections.findSectionBy(new StationPair(second, third)))
                .isEqualTo(secondThird);

        assertThat(sections.findSectionBy(new StationPair(third, second)))
                .isEqualTo(secondThird);
    }

    @Test
    @DisplayName("SectionPair를 통해 Section을 가져올 수 없으면 LineHasNotExistSectionException이 발생한다.")
    void SectionPair를_통해_Section을_가져올_수_없으면_LineHasNotExistSectionException이_발생한다() {
        Sections sections = new Sections();
        sections.add(firstSecond);
        sections.add(secondThird);
        sections.add(thirdFourth);

        assertThatExceptionOfType(LineHasNotExistSectionException.class)
                .isThrownBy(() -> sections.findSectionBy(new StationPair(first, fourth)));
    }

    @Test
    void getSectionDistanceBy() {
        Sections sections = new Sections();
        sections.add(firstSecond);
        sections.add(secondThird);
        sections.add(thirdFourth);

        assertThat(sections.getSectionDistanceBy(new StationPair(second, third)))
                .isEqualTo(new Distance(10));

    }
}