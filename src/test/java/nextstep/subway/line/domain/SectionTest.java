package nextstep.subway.line.domain;

import nextstep.subway.line.application.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 노선에 포함된 구간 기능")
class SectionTest {

    private Station 강남;
    private Station 양재;
    private Station 양재시민의숲;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        양재 = new Station(2L, "양재");
        양재시민의숲 = new Station(3L, "양재시민의숲");
    }

    @Test
    @DisplayName("중복된 구간인 경우 예외가 발생한다.")
    void validateDuplication() {
        // given
        Section section = new Section(강남, 양재, 5);

        // when then
        assertThatThrownBy(() -> section.isConnectable(section))
                .isInstanceOf(InvalidSectionException.class);
    }

    @Test
    @DisplayName("종착 구간에 새로운 역을 연결할 수 있다.")
    void isTerminusExtend() {
        // given
        Section section = new Section(강남, 양재, 5);
        Section downTerminus = new Section(양재, 양재시민의숲, 5);

        // when then
        assertThat(section.isConnectable(downTerminus)).isTrue();
    }

    @Test
    @DisplayName("구간 사이에 새로운 역을 연결할 수 있다.")
    void isBetweenStations() {
        // given
        Section section = new Section(강남, 양재시민의숲, 7);
        Section betweenSection = new Section(강남, 양재, 4);

        // when then
        assertThat(section.isConnectable(betweenSection)).isTrue();
    }

    @Test
    @DisplayName("구간 사이에 새로운 역을 추가하면 기존 구간의 거리가 나눠진다.")
    void merge() {
        // given
        Section section = new Section(강남, 양재시민의숲, 7);
        Section newSection = new Section(강남, 양재, 4);

        // when
        Section result = section.merge(newSection);

        // then
        assertThat(section.getDistance()).isEqualTo(new Distance(3));
        assertThat(result.getDistance()).isEqualTo(new Distance(4));
    }
}
