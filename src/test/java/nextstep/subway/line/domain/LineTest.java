package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @DisplayName("노선에 포함된 지하철역 리스트 조회 작업을 성공한다")
    @Test
    void getStations() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);

        Line line = createLine("1호선", "blue", 종각역, 서울역, 10);

        assertThat(line.getStations()).containsExactly(종각역, 서울역);
    }

    @DisplayName("노선에 구간 추가 작업을 성공한다")
    @Test
    void addSection() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Line line = createLine("1호선", "blue", 종각역, 서울역, 10);

        line.addSection(createSection(종각역, 시청역, 5));

        assertThat(line.getStations()).containsExactly(종각역, 시청역, 서울역);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 시청역 = createStation("시청역", 3L);
        Line line = createLine("1호선", "blue", 종각역, 서울역, 10);

        assertThatThrownBy(() -> line.addSection(createSection(종각역, 시청역, 15)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("이미 등록된 구간을 등록하면 IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException2() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Line line = createLine("1호선", "blue", 종각역, 서울역, 10);

        assertThatThrownBy(() -> line.addSection(createSection(종각역, 서울역, 15)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("등록하려는 구간의 역이 하나라도 존재하지 않으면, IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException3() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 신설동역 = createStation("신설동역", 3L);
        Station 용산역 = createStation("용산역", 4L);
        Line line = createLine("1호선", "blue", 종각역, 서울역, 10);

        assertThatThrownBy(() -> line.addSection(createSection(신설동역, 용산역, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("노선에 구간 제거 작업을 성공한다")
    @Test
    void removeSection() {
        Station 서울역 = createStation("서울역", 1L);
        Station 종각역 = createStation("종각역", 2L);
        Station 신설동역 = createStation("신설동역", 3L);
        Section section = createSection(종각역, 신설동역, 5);
        Line line = createLine("1호선", "blue", 서울역, 종각역, 10);
        line.addSection(section);

        line.deleteStationById(3L);

        assertThat(line.getStations()).containsExactly(서울역,종각역);
    }
}
