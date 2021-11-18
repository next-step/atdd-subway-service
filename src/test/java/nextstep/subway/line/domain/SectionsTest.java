package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 목록 테스트")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        이호선 = new Line("2호선", "green");
    }

    @Test
    @DisplayName("지하철 구간을 추가한다.")
    void add() {
        // given
        Sections sections = new Sections();

        // when
        sections.add(new Section(이호선, 강남역, 역삼역, 10));

        // then
        assertThat(sections).isEqualTo(new Sections(Collections.singletonList(
                new Section(이호선, 강남역, 역삼역, 10)
        )));
    }
}
