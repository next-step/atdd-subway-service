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
        Line line = 지하철_노선_생성("8호선", "분홍색", 잠실역, 문정역, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).contains(잠실역, 문정역);
    }

    @Test
    @DisplayName("색상,이름이 다른 노선을 받아 노선 업데이트")
    void update() {
        // given
        String name = "2호선";
        String color = "초록색";
        Line line = 지하철_노선_생성(name, color);

        // given
        String newName = "8호선";
        String newColor = "초록색";
        Line newLine = 지하철_노선_생성(newName, newColor);

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
        assertThat(line.getColor()).isEqualTo(newColor);
    }

    @Test
    @DisplayName("구간을 받아 노선에 추가")
    void addSection() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = 지하철_노선_생성("8호선", "분홍색", 잠실역, 문정역, 10);

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
        Line line = 지하철_노선_생성("8호선", "분홍색", 잠실역, 문정역, 10);

        // given
        Station 가락시장역 = new Station("가락시장역");
        Section section = 지하철_구간_생성(가락시장역, 문정역, 1);
        line.addSection(section);

        // when
        line.removeSection(가락시장역);

        // then
        assertThat(line.getStations()).doesNotContain(가락시장역);
    }


    private Line 지하철_노선_생성(String name, String color) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private Section 지하철_구간_생성(Station upStation, Station downStation, int distance) {
        return new Section.Builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

}
