package nextstep.subway.line.domain;

import static nextstep.subway.line.step.LineStep.line;
import static nextstep.subway.line.step.SectionStep.section;
import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선들")
class LinesTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Lines.from(Collections.singletonList(신분당선())));
    }

    @Test
    @DisplayName("초기 목록은 필수")
    void instance_nullList_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Lines.from(null))
            .withMessage("지하철 노선 목록은 필수입니다.");
    }

    @Test
    @DisplayName("목록이 비어있는지 판단")
    void isEmpty() {
        // given
        Lines emptyLines = Lines.from(Collections.emptyList());

        // when
        boolean isEmpty = emptyLines.isEmpty();

        // then
        assertThat(isEmpty).isTrue();
    }

    @Test
    @DisplayName("지하철 역 목록")
    void stationList() {
        // given
        Lines 신분당선만_있는_노선들 = Lines.from(Collections.singletonList(신분당선()));

        // when
        List<Station> stationList = 신분당선만_있는_노선들.stationList();

        // then
        assertThat(stationList)
            .hasSize(2)
            .doesNotHaveDuplicates()
            .containsExactly(station("강남역"), station("양재역"));
    }

    @Test
    @DisplayName("구간 목록")
    void sectionList() {
        // given
        Lines 신분당선만_있는_노선들 = Lines.from(Collections.singletonList(신분당선()));

        // when
        List<Section> sectionList = 신분당선만_있는_노선들.sectionList();

        // then
        assertThat(sectionList)
            .hasSize(1)
            .containsExactly(section("강남역", "양재역", 1));
    }

    private Line 신분당선() {
        return line("신분당선", "red",
            section("강남역", "양재역", 1)
        );
    }
}
