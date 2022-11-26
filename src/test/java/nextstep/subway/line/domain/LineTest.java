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

class LineTest {
    private Station 종각역;
    private Station 서울역;
    private Line line;

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
        line = new Line("1호선", "blue", 종각역, 서울역, 10);
    }

    @DisplayName("노선에 포함된 지하철역 리스트 조회 작업을 성공한다")
    @Test
    void getStations() {
        assertThat(line.getStations()).containsExactly(종각역, 서울역);
    }

    @DisplayName("노선에 구간 추가 작업을 성공한다")
    @Test
    void addSection() {
        Station 시청역 = new Station("시청역");

        line.addSection(new Section(종각역, 시청역, 5));

        assertThat(line.getStations()).containsExactly(종각역, 시청역, 서울역);
    }

    @DisplayName("기존 구간거리 보다 크면 IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException() {
        Station 시청역 = new Station("시청역");

        assertThatThrownBy(() -> line.addSection(new Section(종각역, 시청역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("이미 등록된 구간을 등록하면 IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException2() {
        assertThatThrownBy(() -> line.addSection(new Section(종각역, 서울역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("등록하려는 구간의 역이 하나라도 존재하지 않으면, IllegalArgumentException을 반환한다")
    @Test
    void addSectionWithException3() {
        Station 시청역 = new Station("시청역");
        Station 신설동역 = new Station("신설동역");

        assertThatThrownBy(() -> line.addSection(new Section(시청역, 신설동역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("노선에 구간 제거 작업을 성공한다")
    @Test
    void removeSection() {
        Station 시청역 = new Station("시청역");
        Field id = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(id).setAccessible(true);
        ReflectionUtils.setField(id, 시청역, 3L);
        line.addSection(new Section(종각역, 시청역, 5));

        line.deleteStationById(3L);

        assertThat(line.getStations()).containsExactly(종각역, 서울역);
    }
}
