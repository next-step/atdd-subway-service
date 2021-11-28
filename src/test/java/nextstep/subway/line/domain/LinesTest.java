package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import java.util.List;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선들")
class LinesTest {

    private Station 강남역;
    private Station 양재역;
    private Section 강남_양재_구간;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = Station.from(Name.from("강남역"));
        양재역 = Station.from(Name.from("양재역"));
        강남_양재_구간 = Section.of(강남역, 양재역, Distance.from(10));
        신분당선 = Line.of(Name.from("신분당선"), Color.from("red"),
            Sections.from(강남_양재_구간));
    }

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Lines.from(Collections.singletonList(신분당선)));
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
        Lines 신분당선만_있는_노선들 = Lines.from(Collections.singletonList(신분당선));

        // when
        List<Station> stationList = 신분당선만_있는_노선들.stationList();

        // then
        assertThat(stationList)
            .hasSize(2)
            .doesNotHaveDuplicates()
            .containsExactly(강남역, 양재역);
    }

    @Test
    @DisplayName("구간 목록")
    void sectionList() {
        // given
        Lines 신분당선만_있는_노선들 = Lines.from(Collections.singletonList(신분당선));

        // when
        List<Section> sectionList = 신분당선만_있는_노선들.sectionList();

        // then
        assertThat(sectionList)
            .hasSize(1)
            .containsExactly(강남_양재_구간);
    }
}
