package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관리")
public class LineTest {
    private static Station A, B, Z;

    @BeforeAll
    public static void setup() {
        A = new Station(1L,"A");
        B = new Station(2L,"B");
        Z = new Station(26L,"Z");
    }

    @Test
    @DisplayName("노선을 생성한다.")
    public void init() {
        Line line = new Line("알파벳 A-Z", "rainbow", A, Z, 26);

        assertAll(
                () -> assertThat(line).isNotNull(),
                () -> assertThat(line.getName()).isEqualTo("알파벳 A-Z"),
                () -> assertThat(line.getStations()).containsExactly(A, Z),
                () -> assertThat(line.getDistances()).containsExactly(26)
        );
    }

    @Test
    @DisplayName("노선을 변경한다.")
    public void update() {
        // given
        Line line = new Line("알파벳 A-B", "rainbow1", A, B, 2);

        // when
        line.update(new Line("알파벳 A-Z", "rainbow2", A, Z, 26));

        assertAll(
                () -> assertThat(line).isNotNull(),
                () -> assertThat(line.getName()).isEqualTo("알파벳 A-Z"),
                () -> assertThat(line.getColor()).isEqualTo("rainbow2"),
                () -> assertThat(line.getStations()).containsExactly(A, Z),
                () -> assertThat(line.getDistances()).containsExactly(26)
        );
    }

    @Test
    @DisplayName("노선에 구간을 추가한다.")
    public void addSection() {
        // given
        Line line = new Line("알파벳 A-Z", "rainbow", A, Z, 26);

        line.addSection(new Section(1L, A, B, 2));

        assertAll(
                () -> assertThat(line).isNotNull(),
                () -> assertThat(line.getName()).isEqualTo("알파벳 A-Z"),
                () -> assertThat(line.getColor()).isEqualTo("rainbow"),
                () -> assertThat(line.getStations()).containsExactly(A, B, Z)
        );
    }
}
