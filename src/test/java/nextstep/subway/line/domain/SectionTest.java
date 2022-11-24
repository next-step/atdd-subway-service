package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionTest {
    private Station 서초역;
    private Station 강남역;
    private Section existSection;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        강남역 = new Station("강남역");
        existSection = new Section(서초역, 강남역, 10);
    }

    @DisplayName("구간 재조정을 통해 상행역을 변경할 수 있다.")
    @Test
    void reorganizeUpStation() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, 5);

        existSection.reorganize(newSection);

        assertThat(existSection.getUpStation()).isEqualTo(교대역);
    }

    @DisplayName("구간 재조정을 통해 하행역을 변경할 수 있다.")
    @Test
    void reorganizeDownStation() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(교대역, 강남역, 5);

        existSection.reorganize(newSection);

        assertThat(existSection.getDownStation()).isEqualTo(교대역);
    }

    @DisplayName("구간 재조정 시 기존 역 사이의 거리보다 크거나 같을 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void reorganizeException(int distance) {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, distance);
        assertThatThrownBy(() -> existSection.reorganize(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0이하의 값일 수 없습니다.");
    }

}
