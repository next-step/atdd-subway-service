package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixtures.이호선;
import static nextstep.subway.line.domain.StationFixtures.강변;
import static nextstep.subway.line.domain.StationFixtures.잠실;
import static nextstep.subway.line.domain.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("신규 구간의 거리가 기존 구간 길이 보다 같거나 크면 실패")
    void updateUpStation_new_distance_greater_than_distance_fail() {
        // given
        Section section = Section.of(이호선, 잠실, 잠실나루, 100);

        // then
        // when
        assertThrows(InvalidParameterException.class, () -> section.updateUpStation(강변, 1000));
        assertThrows(InvalidParameterException.class, () -> section.updateUpStation(강변, 100));
        assertThrows(InvalidParameterException.class, () -> section.updateDownStation(강변, 1000));
        assertThrows(InvalidParameterException.class, () -> section.updateDownStation(강변, 100));
    }

    @Test
    @DisplayName("신규 구간의 거리가 기존 구간 길이 보다 작으면 성공")
    void addSection_minus_distance() {
        // given
        Section section = Section.of(이호선, 잠실, 잠실나루, 100);

        // when
        section.updateDownStation(강변, 10);
        Integer actual = section.getDistance();

        // then
        assertThat(actual).isEqualTo(90);
    }
}
