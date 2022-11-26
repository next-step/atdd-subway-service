package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Station 종각역;
    private Station 서울역;
    private Sections sections;

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
        sections = new Sections(new Section(종각역, 서울역, 10));
    }

    @DisplayName("노선에 포함된 지하철역 리스트 조회 작업을 성공한다")
    @Test
    void getStations() {
        assertThat(sections.getStations()).containsExactly(종각역, 서울역);
    }

    @DisplayName("구간리스트에 구간 추가 작업을 성공한다")
    @Test
    void addSection() {
        Station 시청역 = new Station("시청역");

        sections.add(new Section(서울역, 시청역, 5));

        assertThat(sections.count()).isEqualTo(2);
    }

    @DisplayName("구간리스트의 구간 제거 작업을 성공한다")
    @Test
    void removeSection() {
        Station 시청역 = new Station("시청역");
        Field 시청역_ID = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(시청역_ID).setAccessible(true);
        ReflectionUtils.setField(시청역_ID, 시청역, 3L);
        sections.add(new Section(서울역, 시청역, 5));

        sections.remove(3L);

        assertThat(sections.count()).isEqualTo(1);
    }
}
