package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    @Test
    @DisplayName("Line 생성")
    void createLine() {
        // given & when
        Line actual = Line.of("신분당선", "bg-red-600");

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("신분당선"),
                () -> assertThat(actual.getColor()).isEqualTo("bg-red-600")
        );
    }

    @Test
    @DisplayName("Line 생성 실패 - 노선명 필수값")
    void createLineNameIsNotBlank() {
        assertAll(
                () -> assertThatThrownBy(() -> Line.of(null, "bg-red-600"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("노선 이름은 필수입니다."),
                () -> assertThatThrownBy(() -> Line.of("", "bg-red-600"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("노선 이름은 필수입니다.")
        );
    }

    @Test
    @DisplayName("Line 생성 실패 - 색상 필수값")
    void createLineColorIsNotBlank() {
        assertAll(
                () -> assertThatThrownBy(() -> Line.of("신분당선", null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("노선 색상은 필수입니다."),
                () -> assertThatThrownBy(() -> Line.of("신분당선", ""))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("노선 색상은 필수입니다.")
        );
    }

    @Test
    @DisplayName("Line 저장 역 순서대로 반환")
    void getSortStations() {
        // given
        Station 신논현역 = Station.from("신논현역");
        Station 강남역 = Station.from("강남역");
        Station 양재역 = Station.from("광교역");

        // when
        Line line = Line.of("신분당선", "bg-red-600", Section.of(강남역, 양재역, Distance.from(5)));
        line.addSection(Section.of(신논현역, 강남역, Distance.from(3)));
        List<Station> actual = line.sortStations();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).containsExactly(신논현역, 강남역, 양재역)
        );
    }
}
