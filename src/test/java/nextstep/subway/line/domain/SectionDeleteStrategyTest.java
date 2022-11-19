package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 삭제 전략 테스트")
class SectionDeleteStrategyTest {

    private Line 이호선;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
    }

    @Test
    void 삭제하려는_구간이_상행_종점역() {
        Section 교대역_강남역_구간 = new Section(교대역, 강남역, 10);
        Section 강남역_역삼역_구간 = new Section(강남역, 역삼역, 10);
        이호선.addSection(교대역_강남역_구간);
        이호선.addSection(강남역_역삼역_구간);

        new FirstSectionDeleteStrategy().delete(이호선, 교대역_강남역_구간, null);

        assertThat(이호선.getSections()).hasSize(1);
    }

    @Test
    void 삭제하려는_구간이_중간역() {
        Section 교대역_강남역_구간 = new Section(교대역, 강남역, 10);
        Section 강남역_역삼역_구간 = new Section(강남역, 역삼역, 10);
        이호선.addSection(교대역_강남역_구간);
        이호선.addSection(강남역_역삼역_구간);

        new MiddleSectionDeleteStrategy().delete(이호선, 강남역_역삼역_구간, 교대역_강남역_구간);

        assertThat(이호선.getSections()).hasSize(1);
    }

    @Test
    void 삭제하려는_구간이_하행_종점역() {
        Section 교대역_강남역_구간 = new Section(교대역, 강남역, 10);
        Section 강남역_역삼역_구간 = new Section(강남역, 역삼역, 10);
        이호선.addSection(교대역_강남역_구간);
        이호선.addSection(강남역_역삼역_구간);

        new LastSectionDeleteStrategy().delete(이호선, null, 강남역_역삼역_구간);

        assertThat(이호선.getSections()).hasSize(1);
    }
}
