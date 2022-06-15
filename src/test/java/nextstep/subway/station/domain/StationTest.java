package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    /*
     * Given : 지하철 역을 생성하고
     * When : 구간에 해당 지하철 역을 추가하면
     * Then : 지하철역에서 해당 구간을 찾을수 있다.
     */
    @DisplayName("연관관계 테스트")
    @Test
    void createRelateTest() {
        // Given
        final Station downStation = new Station("양재역");

        // when
        final Section section = new Section(new Line("신분당선", "bg-red-600"), new Station("강남역"), downStation, 10);

        // Then
        assertThat(downStation.getSections()).contains(section);
    }

    /**
     * Given : 새로운 지하철 역, 노선, 구간을 생성하고
     * When : 노선 에서 구간을 삭제 하면
     * Then : 지하철 역 에서도 해당 구간이 삭제 되어야 한다.
     */
    @DisplayName("노선에서 구간 삭제 시 연관된 지하철 역에서도 삭제되어야 한다.")
    @Test
    void removeRelateTest() {
        // Given
        final Station downStation = new Station("양재역");
        final Line 신분당선 = new Line("신분당선", "bg-red-600");
        new Section(신분당선, new Station("강남역"), downStation, 10);
        new Section(신분당선, downStation, new Station("논현역"), 10);

        // When
        신분당선.removeSection(downStation);

        // Then
        assertThat(downStation.getSections()).isEmpty();
    }
}