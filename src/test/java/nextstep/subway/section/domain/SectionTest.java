package nextstep.subway.section.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionTest {
    private Station givenUpStation;
    private Station givenDownStation;
    private int givenDistance;
    private Line givenLine;
    private Section givenSection;

    @BeforeEach
    void setUp() {
        givenUpStation = new Station("강남역");
        givenDownStation = new Station("판교역");
        givenDistance = 30;
        givenLine = new Line("신분당선", "red", givenUpStation, givenDownStation, givenDistance);
        givenSection = new Section(givenLine, givenUpStation, givenDownStation, givenDistance);
    }

    @Test
    void 상행역을_수정할_수_있다() {
        // given
        final Station newUpStation = new Station("양재역");

        // when
        givenSection.updateUpStation(newUpStation, givenDistance / 2);

        // then
        assertThat(givenSection.getUpStation()).isEqualTo(newUpStation);
        assertThat(givenSection.getDistance()).isEqualTo(givenDistance / 2);
    }

    @Test
    void 하행역을_수정할_수_있다() {
        // given
        final Station newDownStation = new Station("양재역");

        // when
        givenSection.updateDownStation(newDownStation, givenDistance / 2);

        // then
        assertThat(givenSection.getDownStation()).isEqualTo(newDownStation);
        assertThat(givenSection.getDistance()).isEqualTo(givenDistance / 2);
    }

    @Test
    void 수정된_구간의_길이가_기존보다_짧지_않으면_에러가_발생해야_한다() {
        // given
        final Station newUpStation = new Station("양재역");

        // when and then
        assertThatThrownBy(() -> givenSection.updateUpStation(newUpStation, givenDistance))
                .isInstanceOf(RuntimeException.class);
    }
}
