package nextstep.subway.component.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionWeightedEdgeTest {

    private Station 강남역;
    private Station 광교역;
    private Line 신분당선;
    private Section 구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        신분당선 = new Line(1L, "신분당선");
        구간 = new Section(신분당선, 강남역, 광교역, 10);
    }

    @Test
    void 구간_간선_객체_생성() {
        SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(구간, 신분당선.getId());
        assertThat(sectionWeightedEdge.getSection().getUpStation()).isEqualTo(강남역);
        assertThat(sectionWeightedEdge.getSection().getDownStation()).isEqualTo(광교역);
        assertThat(sectionWeightedEdge.getLineId()).isEqualTo(신분당선.getId());
    }
}
