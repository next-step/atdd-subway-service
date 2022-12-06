package nextstep.subway.line.domain;

import nextstep.subway.JpaEntityTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 관련 도메인 테스트")
public class LineTest extends JpaEntityTest {
    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("노선 생성 테스트")
    @Test
    void createLine() {
        Line line = lineRepository.save(new Line("신분당선", "빨간색"));

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
        Line line = lineRepository.save(new Line("신분당선", "빨간색", new Station("강남역"), new Station("광교역"), 10));

        List<Station> stations = line.getStations();

        assertThat(stations).containsExactlyElementsOf(Arrays.asList(
                new Station("강남역"),
                new Station("광교역")
        ));
    }

    @DisplayName("노선의 정보를 업데이트 한다")
    @Test
    void updateLine() {
        Line line = lineRepository.save(new Line("신분당선", "파란색"));

        Line newLine = new Line("신분당선", "빨간색");
        line.update(newLine);
        flushAndClear();

        assertThat(line).isEqualTo(newLine);
        assertThat(line.getColor()).isEqualTo("빨간색");
    }

    @DisplayName("초과운임을 포함하여 노선 생성")
    @Test
    void createLineWithSurcharge() {
        Line 신분당선 = new Line("신분당선", "빨간색", 1_900);

        assertThat(신분당선.getSurcharge()).isEqualTo(1_900);
    }

    @DisplayName("초과운임을 포함하여 노선 생성 시, 음의 초과운임 입력 시 에러")
    @Test
    void createLineWithMinusSurchargeException() {
        assertThatThrownBy(() -> new Line("신분당선", "빨간색", -9_000))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
