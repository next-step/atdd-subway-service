package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    @DisplayName("노선 생성 테스트")
    @Test
    void createLine() {
        Line line = new Line("신분당선", "빨간색");

        assertThat(line).isInstanceOf(Line.class);
    }

    @DisplayName("노선 이름이 없거나, 색이 지정되지 않은 경우 예외 발생")
    @Test
    void createLineWithNoPassValidate() {
        assertThatThrownBy(() -> new Line(null, "빨간색"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Line("", "빨간색"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Line("신분당선", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Line("신분당선", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 포함되어 있는 역 리스트 가져오기")
    @Test
    void getStations() {
        Line line = new Line("신분당선", "빨간색", new Station("강남역"), new Station("광교역"), 10);

        List<Station> stations = line.getStations();

        assertThat(stations).containsExactlyElementsOf(Arrays.asList(
                new Station("강남역"),
                new Station("광교역")
        ));
    }

    @DisplayName("노선의 정보를 업데이트 한다")
    @Test
    void updateLine() {
        Line line = new Line("신분당선", "파란색");

        Line newLine = new Line("신분당선", "빨간색");
        line.update(newLine);

        assertThat(line.equals(newLine)).isTrue();
        assertThat(line.getColor()).isEqualTo("빨간색");
    }
}
