package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    @DisplayName("입력된 지하철역이 하행역과 같다.")
    public void isEqualsDownStation_true() throws Exception {
        // given
        Station station = new Station("강남역");
        Section section = new Section(station);

        // when
        boolean result = section.isEqualsDownStation(station);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("입력된 지하철역이 하행역과 다르다.")
    public void isEqualsDownStation_false() throws Exception {
        // given
        Station station = new Station("강남역");
        Section section = new Section(station);

        // when
        boolean result = section.isEqualsDownStation(new Station("양재역"));

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("입력된 지하철역이 상행역과 같은지 검사한다.")
    public void isEqualsUpStation() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section section = new Section(강남역, 양재역);

        // when
        boolean 강남역_결과 = section.isEqualsUpStation(강남역);
        boolean 양재역_결과 = section.isEqualsUpStation(양재역);

        // then
        assertThat(강남역_결과).isTrue();
        assertThat(양재역_결과).isFalse();
    }
}