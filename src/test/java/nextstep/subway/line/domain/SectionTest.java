package nextstep.subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("지하철 구간 관련 기능")
public class SectionTest {
    private Station 강남역;
    private Station 양재역;

    private Section 강남_양재_구간;
    
    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        
        강남_양재_구간 = new Section(null, 강남역, 양재역, 40);
    }

    @DisplayName("구간에 특정역이 있는지 조회한다.")
    @Test
    void search_hasStation() {
        // when
        boolean isFind = 강남_양재_구간.hasStation(강남역);

        // then
        Assertions.assertThat(isFind).isTrue();
    }

    @DisplayName("특정역이 구간의 상행역과 일치하는지 조회한다.")
    @Test
    void search_upStationMatch() {
        // when
        boolean isFind = 강남_양재_구간.isEqualUpStation(강남역);

        // then
        Assertions.assertThat(isFind).isTrue();
    }

    @DisplayName("특정역이 구간의 하행역과 일치하는지 조회한다.")
    @Test
    void search_downStationMatch() {
        // when
        boolean isFind = 강남_양재_구간.isEqualDownStation(양재역);

        // then
        Assertions.assertThat(isFind).isTrue();
    }
}
