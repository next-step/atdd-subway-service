package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Line Request 테스트")
class LineRequestTest {

    private LineRequest request;

    @BeforeEach
    void setUp() {
        request = new LineRequest("신분당선", "red", 1L, 2L, 10);;
    }

    @Test
    void upStatin_downStation_정보를_이용하여_Line_entity_생성() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line line = request.toLine(강남역, 양재역);
        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo("red");
        assertThat(line.getSections()).containsExactly(new Section(line, 강남역, 양재역, 10));
    }

    @Test
    void line_생성_method_toLine_이름_color으로_생성() {
        Line line = request.toLine();
        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo("red");
    }
}