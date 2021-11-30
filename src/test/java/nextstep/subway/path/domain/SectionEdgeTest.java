package nextstep.subway.path.domain;

import static nextstep.subway.line.step.SectionStep.section;
import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 엣지")
class SectionEdgeTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> SectionEdge.from(section("강남", "양재", 1)));
    }

    @Test
    @DisplayName("구간은 필수")
    void instance_nullSection_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> SectionEdge.from(null))
            .withMessage("구간은 필수입니다.");
    }

    @Test
    @DisplayName("구간 정보 가져오기")
    void edge() {
        // given
        SectionEdge edge = SectionEdge.from(section("강남", "양재", 1));

        // when, then
        assertThat(edge)
            .extracting(SectionEdge::getSource, SectionEdge::getTarget, SectionEdge::getWeight)
            .containsExactly(station("강남"), station("양재"), 1d);
    }
}
