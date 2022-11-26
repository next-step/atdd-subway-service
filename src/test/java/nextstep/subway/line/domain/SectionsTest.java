package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Station 종각역;
    private Station 서울역;
    private Sections sections;

    @BeforeEach
    void setUp() {
        종각역 = new Station("종각역");
        서울역 = new Station("서울역");
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
        sections.add(new Section(종각역, 시청역, 5));
        assertThat(sections.count()).isEqualTo(2);
    }
}
