package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixtures.이호선;
import static nextstep.subway.line.domain.StationFixtures.강변;
import static nextstep.subway.line.domain.StationFixtures.잠실;
import static nextstep.subway.line.domain.StationFixtures.잠실나루;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("상행 추가 업데이트 신규 구간의 거리가 기존 구간 길이 이상이면 실패")
    void updateUpStation_new_distance_greater_than_distance_fail() {
        // given
        Section section = Section.of(이호선, 잠실, 잠실나루, 100);

        // then
        // when
        assertThrows(RuntimeException.class,
            () -> section.updateUpStation(강변, 1000));
    }

    @Test
    @DisplayName("하행 추가 업데이트 신규 구간의 거리가 기존 구간 길이 이상이면 실패")
    void updateDownStation_new_distance_greater_than_distance_fail() {
        // given
        Section section = Section.of(이호선, 잠실, 잠실나루, 100);

        // then
        // when
        assertThrows(RuntimeException.class,
            () -> section.updateUpStation(강변, 1000));
    }
}
