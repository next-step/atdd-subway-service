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

    @Test
    @DisplayName("입력된 구간의 상행역과 현재 상행역이 같은지 검사한다.")
    public void isEqualsUpStation_section() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(강남역, 양재역);
        Section 강남_판교_구간 = new Section(강남역, 판교역);
        Section 양재_강남_구간 = new Section(양재역, 강남역);

        // when
        boolean 강남_양재_강남_판교_결과 = 강남_양재_구간.isEqualsUpStation(강남_판교_구간);
        boolean 강남_양재_양재_강남_결과 = 강남_양재_구간.isEqualsUpStation(양재_강남_구간);

        // then
        assertThat(강남_양재_강남_판교_결과).isTrue();
        assertThat(강남_양재_양재_강남_결과).isFalse();
    }

    @Test
    @DisplayName("현재 구간의 상행역과 거리를 수정한다.")
    public void updateUpStation() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        int 강남_양재_거리 = 10;
        Section 강남_양재_구간 = new Section(강남역, 양재역, 강남_양재_거리);

        int 양재_판교_거리 = 3;
        Section 양재_판교_구간 = new Section(양재역, 판교역, 양재_판교_거리);

        // when
        강남_양재_구간.updateUpStation(양재_판교_구간);

        // then
        assertThat(강남_양재_구간.getUpStation()).isEqualTo(판교역);
        assertThat(강남_양재_구간.getDistance().getDistance()).isEqualTo(강남_양재_거리 - 양재_판교_거리);
    }

    @Test
    @DisplayName("입력된 구간의 하행역과 현재 하행역이 같은지 검사한다.")
    public void isEqualsDownStation_section() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(강남역, 양재역);
        Section 판교_양재_구간 = new Section(판교역, 양재역);
        Section 양재_강남_구간 = new Section(양재역, 강남역);

        // when
        boolean 강남_양재_판교_양재_결과 = 강남_양재_구간.isEqualsDownStation(판교_양재_구간);
        boolean 강남_양재_양재_강남_결과 = 강남_양재_구간.isEqualsDownStation(양재_강남_구간);

        // then
        assertThat(강남_양재_판교_양재_결과).isTrue();
        assertThat(강남_양재_양재_강남_결과).isFalse();
    }

    @Test
    @DisplayName("현재 구간의 하행역과 거리를 수정한다.")
    public void updateDownStation() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        int 강남_양재_거리 = 10;
        Section 강남_양재_구간 = new Section(강남역, 양재역, 강남_양재_거리);

        int 판교_양재_거리 = 3;
        Section 판교_양재_구간 = new Section(판교역, 양재역, 판교_양재_거리);

        // when
        강남_양재_구간.updateDownStation(판교_양재_구간);

        // then
        assertThat(강남_양재_구간.getDownStation()).isEqualTo(판교역);
        assertThat(강남_양재_구간.getDistance().getDistance()).isEqualTo(강남_양재_거리 - 판교_양재_거리);
    }

    @Test
    @DisplayName("두 구간의 거리를 더한 결과를 리턴한다.")
    public void plusDistance() throws Exception {
        // given
        int sectionDistance = 1;
        Section section = new Section(sectionDistance);

        int inputSectionDistance = 2;
        Section inputSection = new Section(inputSectionDistance);

        // when
        Distance result = section.plusDistance(inputSection);

        // then
        assertThat(result.getDistance()).isEqualTo(sectionDistance + inputSectionDistance);
    }
}