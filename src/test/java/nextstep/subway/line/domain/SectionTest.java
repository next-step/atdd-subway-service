package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 테스트")
public class SectionTest {

    private static final Station 충정로역 = new Station("충정로역");
    private static final Station 서대문역 = new Station("서대문역");
    private static final Station 광화문역 = new Station("광화문역");
    private static final Line 오호선 = new Line("5호선", "purple");

    @DisplayName("구간의 거리가 0 이하인 경우 예외 발생")
    @Test
    void createSectionWrong_minDistance() {
        assertThatThrownBy(() -> new Section(오호선, 충정로역, 광화문역, 0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행선과 하행선이 같을경우 예외 발생")
    @Test
    void createSectionWrong_sameUpAndDown() {
        assertThatThrownBy(() -> new Section(오호선, 충정로역, 충정로역, 7))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이전 구간 확인")
    @Test
    void isBefore() {
        Section before = new Section(오호선, 충정로역, 서대문역, 7);
        Section after = new Section(오호선, 서대문역, 광화문역, 4);
        assertThat(before.isBefore(after)).isTrue();
    }

    @DisplayName("다음 구간 확인")
    @Test
    void isAfter() {
        Section before = new Section(오호선, 충정로역, 서대문역, 7);
        Section after = new Section(오호선, 서대문역, 광화문역, 4);
        assertThat(after.isAfter(before)).isTrue();
    }

    @DisplayName("상행선 하행선이 모두 동일한지 확인")
    @Test
    void isEqualUpAndDownStation() {
        Section preSection = new Section(오호선, 충정로역, 서대문역, 7);
        Section newSection = new Section(오호선, 충정로역, 서대문역, 4);
        assertThat(newSection.isEqualAllStation(preSection)).isTrue();
    }
}
