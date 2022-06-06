package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
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

    @DisplayName("노선에 구간을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void line_register_section_test() {
        // given
        Station 강남역 = new Station("강남");
        Station 양재역 = new Station("양재");
        Station 양재시민의숲역 = new Station("양재시민의숲");
        Station 신논현역 = new Station("신논현");

        // when
        Line line = Line.of("신분당선", "red", 강남역, 양재시민의숲역, 10);
        line.registerSection(강남역, 양재역, 5);
        line.registerSection(신논현역, 강남역, 10);

        // then
        assertThat(line.getStations()).containsExactly(신논현역, 강남역, 양재역, 양재시민의숲역);
    }

    @DisplayName("노선에 구간을 삭제하면 정상적으로 삭제되어야 한다")
    @Test
    void line_remove_section_test() {
        // given
        Station 강남역 = new Station("강남");
        Station 양재역 = new Station("양재");
        Station 양재시민의숲역 = new Station("양재시민의숲");
        Station 신논현역 = new Station("신논현");

        // when
        Line line = Line.of("신분당선", "red", 강남역, 양재시민의숲역, 10);
        line.registerSection(강남역, 양재역, 5);
        line.registerSection(신논현역, 강남역, 10);

        // then
        line.removeStation(양재역);
        assertThat(line.getStations()).containsExactly(신논현역, 강남역, 양재시민의숲역);
    }
}
