package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {


    @Test
    @DisplayName("노선의 포함된 역들을 가져온다")
    void getStations() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = Line.of("8호선", "분홍색", 잠실역, 문정역, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).contains(잠실역, 문정역);
    }

    @Test
    @DisplayName("이름과 색상을 받아 노선 업데이트")
    void update() {
        // given
        String name = "2호선";
        String color = "초록색";
        Line line = Line.of(name, color);

        // when
        line.update(name, color);

        // then
        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("구간을 받아 노선에 추가")
    void addSection() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = Line.of("8호선", "분홍색", 잠실역, 문정역, 10);

        // given
        Station 가락시장역 = new Station("가락시장역");
        Section section = Section.of(가락시장역, 문정역, 1);

        // when
        line.addSection(section);

        // then
        assertThat(line.getSections()).contains(section);
    }

    @Test
    @DisplayName("역을 받아 노선에서 역 삭제")
    void removeStation() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = Line.of("8호선", "분홍색", 잠실역, 문정역, 10);

        // given
        Station 가락시장역 = new Station("가락시장역");
        Section section = Section.of(가락시장역, 문정역, 1);
        line.addSection(section);

        // when
        line.removeSection(가락시장역);

        // then
        assertThat(line.getStations()).doesNotContain(가락시장역);
    }
}
