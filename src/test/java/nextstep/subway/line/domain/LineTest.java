package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선 대한 단위 테스트")
class LineTest {

    @DisplayName("노선을 생성하면 정상적으로 생성되어 조회되어야 한다")
    @Test
    void line_create_test() {
        // given
        Line line = Line.of("신분당선", "red");

        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo("red");
    }

    @DisplayName("노선의 이름이 없거나 공백이라면 예외가 발생해야 한다")
    @Test
    void line_name_exception_test() {
        assertThatThrownBy(() -> {
            Line.of(null, "red");
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_NULL_LINE_NAME.getMessage());

        assertThatThrownBy(() -> {
            Line.of("", "red");
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_NULL_LINE_NAME.getMessage());
    }

    @DisplayName("노선의 색상이 없거나 공백이라면 예외가 발생해야 한다")
    @Test
    void line_color_exception_test() {
        assertThatThrownBy(() -> {
            Line.of("신분당선", null);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_NULL_LINE_COLOR.getMessage());

        assertThatThrownBy(() -> {
            Line.of("신분당선", "");
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_NULL_LINE_COLOR.getMessage());
    }

    @DisplayName("노선의 색싱이나 이름을 변경하면 변경되어야 한다")
    @Test
    void line_update_test() {
        // given
        Line line = Line.of("신분당선", "red");
        Line newLine = Line.of("2호선", "blue");

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo("2호선");
        assertThat(line.getColor()).isEqualTo("blue");
    }

    @DisplayName("노선이 구간을 생성하면 정상적으로 생성되어야 한다")
    @Test
    void line_created_section_test() {
        // given
        Station 대림역 = new Station("대림");
        Station 신대방역 = new Station("신대방");
        Station 신림역 = new Station("신림");
        Line 이호선 = Line.of("2호선", "blue", 대림역, 신림역, 5, 0);

        Section 생성된_노선 = 이호선.createSection(대림역, 신대방역, 10);

        assertAll(
            () -> assertThat(생성된_노선.getUpStation()).isEqualTo(대림역),
            () -> assertThat(생성된_노선.getDownStation()).isEqualTo(신대방역),
            () -> assertThat(생성된_노선.getDistance()).isEqualTo(new Distance(10))
        );
    }
}
