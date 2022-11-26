package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {
    private Station 종각역;
    private Station 서울역;
    private Section section;

    @BeforeEach
    void setUp() {
        종각역 = new Station("종각역");
        서울역 = new Station("서울역");
        Field 종각역_ID = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(종각역_ID).setAccessible(true);
        ReflectionUtils.setField(종각역_ID, 종각역, 1L);
        Field 서울역_ID = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(서울역_ID).setAccessible(true);
        ReflectionUtils.setField(서울역_ID, 서울역, 2L);
        section = new Section(종각역, 서울역, 10);
    }

    @DisplayName("구간의 상행역 수정 작업에 성공한다")
    @Test
    void updateUpStation() {
        section.updateUpStation(new Section(종각역, new Station("신설동역"),3));

        assertThat(section.getDistance()).isEqualTo(7);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void updateUpStationWithException() {
        assertThatThrownBy(() -> section.updateUpStation(new Section(종각역, new Station("신설동역"),10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("구간의 하행역 수정 작업에 성공한다")
    @Test
    void updateDownStation() {
        section.updateUpStation(new Section(new Station("신설동역"), 종각역,3));

        assertThat(section.getDistance()).isEqualTo(7);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void updateDownStationWithException() {
        assertThatThrownBy(() -> section.updateDownStation(new Section(new Station("신설동역"), 종각역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("2개의 구간의 하행역이 같으면 성공한다")
    @Test
    void isSameDownStation() {
        assertThat(section.isSameDownStation(new Section(new Station("신설동역"), 서울역, 3))).isTrue();
    }

    @DisplayName("2개의 구간의 상행역이 같으면 성공한다")
    @Test
    void isSameUpStation() {
        assertThat(section.isSameUpStation(new Section(종각역, new Station("신설동역"), 3))).isTrue();
    }
}
