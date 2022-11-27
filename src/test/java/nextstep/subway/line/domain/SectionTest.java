package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.Fixture.createSection;
import static nextstep.subway.Fixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @DisplayName("구간의 상행역 수정 작업에 성공한다")
    @Test
    void updateUpStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Section section = createSection(서울역, 종각역, 10);

        section.updateUpStation(createSection(종각역, createStation("신설동역", 3L), 3));

        assertThat(section.getDistance()).isEqualTo(7);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void updateUpStationWithException() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Section section = createSection(서울역, 종각역, 10);

        assertThatThrownBy(() -> section.updateUpStation(createSection(종각역, 시청역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("구간의 하행역 수정 작업에 성공한다")
    @Test
    void updateDownStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Section section = createSection(서울역, 종각역, 10);

        section.updateUpStation(createSection(시청역, 종각역, 3));

        assertThat(section.getDistance()).isEqualTo(7);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void updateDownStationWithException() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Section section = createSection(서울역, 종각역, 10);

        assertThatThrownBy(() -> section.updateDownStation(createSection(시청역, 종각역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("2개의 구간의 하행역이 같으면 성공한다")
    @Test
    void isSameDownStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);

        Section section = createSection(서울역, 종각역, 10);

        assertThat(section.isSameDownStation(section)).isTrue();
    }

    @DisplayName("2개의 구간의 상행역이 같으면 성공한다")
    @Test
    void isSameUpStation() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);

        Section section = createSection(서울역, 종각역, 10);

        assertThat(section.isSameUpStation(createSection(서울역, 시청역, 3))).isTrue();
    }
}
