package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixtures.잠실;
import static nextstep.subway.station.StationFixtures.잠실나루;
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
        Section section = Section.of(잠실, 잠실나루, 100);
        Section addSameDistanceSection = Section.of(잠실, 잠실나루, 100);
        Section addOverDistanceSection = Section.of(잠실, 잠실나루, 101);

        // when
        // then
        assertThrows(InvalidParameterException.class,
            () -> section.relocationUpStation(addSameDistanceSection));
        assertThrows(InvalidParameterException.class,
            () -> section.relocationUpStation(addOverDistanceSection));
    }

    @Test
    @DisplayName("신규 구간의 거리가 기존 구간 길이 보다 작으면 성공")
    void addSection_minus_distance() {
        // given
        Section section = Section.of(잠실, 잠실나루, 100);
        Section addSection = Section.of(잠실, 잠실나루, 10);

        // when
        section.relocationUpStation(addSection);
        Integer actual = section.getDistance();

        // then
        assertThat(actual).isEqualTo(90);
    }
}
