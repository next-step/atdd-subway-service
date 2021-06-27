package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.wrappers.Distance;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 파라미터 테스트")
class SectionRequestTest {

    private SectionRequest request;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        request = new SectionRequest(1L, 2L, 10);
        신분당선 = new Line("신분당선", "red");
    }

    @Test
    void 구간_파라미터_객체를_이용하여_구간_객체_생성() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Section section = request.toSection(신분당선, 강남역, 양재역);
        assertThat(section.getDistance()).isEqualTo(new Distance(10));
        assertThat(section.getUpStation()).isEqualTo(강남역);
        assertThat(section.getDownStation()).isEqualTo(양재역);

    }
}
