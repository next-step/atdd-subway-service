package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {
    private Station 인천역 = new Station("인천역");
    private Station 용산역 = new Station("용산역");
    private Line 일호선 = new Line("1호선", "indigo", 인천역, 용산역, 10);
    private Section 인천_용산역_구간 = new Section(일호선, 인천역, 용산역, 10);

    @Test
    void divideBy() {
        // given
        final Station 신도림역 = new Station("신도림역");
        final Section section = new Section(일호선, 인천역, 신도림역, 3);

        // when
        인천_용산역_구간.divideBy(section);

        // then
        assertThat(인천_용산역_구간.getUpStation()).isEqualTo(신도림역);
        assertThat(인천_용산역_구간.getDistance()).isEqualTo(7);
    }

    @Test
    void isOverlapped() {
        // given
        final Station 의정부역 = new Station("의정부역");
        final Section section1 = new Section(일호선, 용산역, 의정부역, 5);
        final Section section2 = new Section(일호선, 인천역, 의정부역, 15);

        // when, then
        assertThat(인천_용산역_구간.isOverlapped(section1)).isFalse();
        assertThat(인천_용산역_구간.isOverlapped(section2)).isTrue();
    }

    @Test
    void connectWith() {
        // given
        final Station 의정부역 = new Station("의정부역");
        final Section section = new Section(일호선, 용산역, 의정부역, 5);

        // when
        인천_용산역_구간.connectWith(section);

        // then
        assertThat(인천_용산역_구간.getUpStation()).isEqualTo(인천역);
        assertThat(인천_용산역_구간.getDownStation()).isEqualTo(의정부역);
        assertThat(인천_용산역_구간.getDistance()).isEqualTo(15);
    }

    @Test
    void isNextSection() {
        // given
        final Station 의정부역 = new Station("의정부역");
        final Section section = new Section(일호선, 용산역, 의정부역, 5);

        // when, then
        assertThat(section.isNextSection(인천_용산역_구간)).isTrue();
        assertThat(인천_용산역_구간.isNextSection(section)).isFalse();
    }
}