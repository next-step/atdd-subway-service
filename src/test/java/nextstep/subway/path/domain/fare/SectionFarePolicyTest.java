package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SectionMaxFare;
import nextstep.subway.station.domain.Station;

class SectionFarePolicyTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    private Section 강남_양재_구간;
    private Section 양재_판교_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 8, Fare.from(1000));
        양재_판교_구간 = new Section(신분당선, 양재역, 판교역, 2, Fare.from(999));
    }

    @DisplayName("구간 추가 금액 중 최댓값 찾기(역방향)")
    @Test
    void findMaxSectionFareDownToUp() {
        List<Section> sections = Arrays.asList(강남_양재_구간, 양재_판교_구간);
        List<Station> stations = Arrays.asList(판교역, 양재역, 강남역);
        SectionFarePolicy sectionFarePolicy = new SectionFarePolicy(sections, stations);

        Fare fare = sectionFarePolicy.calculateFare(Fare.ZERO);

        assertThat(fare).isEqualTo(Fare.from(1000));
    }

    @DisplayName("구간 추가 금액 중 최댓값 찾기(정방향)")
    @Test
    void findMaxSectionFareUpToDown() {
        List<Section> sections = Arrays.asList(강남_양재_구간, 양재_판교_구간);
        List<Station> stations = Arrays.asList(양재역, 판교역);
        SectionFarePolicy sectionFarePolicy = new SectionFarePolicy(sections, stations);

        Fare fare = sectionFarePolicy.calculateFare(Fare.ZERO);

        assertThat(fare).isEqualTo(Fare.from(999));
    }
}