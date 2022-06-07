package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    private Section section;

    @BeforeEach
    void setUp() {
        Station upStation = new Station("강남역");
        Station downStation = new Station("판교역");

        section = new Section(upStation, downStation, 10);
    }
    
    @Test
    void 역_사이에_새로운_구간이_들어오면_새롭게_추가된_역_기준으로_길이가_조정된다() {
        // given
        Station upStation = new Station("강남역");
        Station downStation = new Station("양재역");

        Section newSection = new Section(upStation, downStation, 3);
        
        // when
        section.repairStation(newSection);

        // then
        assertThat(section.getDistance()).isEqualTo(7);
    }
}
