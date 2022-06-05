package nextstep.subway.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private Sections sections;
    private Station 강남역;
    private Station 판교역;
    private Section 강남_판교;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        판교역 = Station.of(2L, "판교역");
        강남_판교 = Section.of(강남역, 판교역, 10);

        sections = new Sections();
        sections.add(강남_판교);
    }

    @DisplayName("강남_판교 구간 길이10에 강남_양재 구간 3을 추가하면, 구간은 2개이고 총구간길이는 10이다.")
    @Test
    void 중간_구간추가_테스트() {
        // given
        Station 양재역 = Station.of(3L, "양재역");
        Section 강남_양재 = Section.of(강남역, 양재역, 3);

        // when
        sections.add(강남_양재);

        // then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getTotalDistance()).isEqualTo(10);
    }

    @DisplayName("강남_판교 구간 길이10에 강남_양재 구간 3을 추가하면, 구간은 2개이고 총구간길이는 10이다.")
    @Test
    void 상행_구간추가_테스트() {
        // given
        Station 논현역 = Station.of(3L, "논현역");
        Section 논현_강남 = Section.of(논현역, 강남역, 3);

        // when
        sections.add(논현_강남);

        // then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getTotalDistance()).isEqualTo(13);
    }
}
