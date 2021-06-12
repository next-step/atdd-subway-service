package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineTest {
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
    }


    @Test
    @DisplayName("섞여있어도 정렬하여 가져올 수 있다.")
    void 섞여있어도_정렬하여_가져올_수_있다() {
        Section firstSection = new Section(null, 강남역, 양재역, 3);
        Section secondSection = new Section(null, 양재역, 판교역, 3);
        Section thirdSection = new Section(null, 판교역, 정자역, 3);

        // when
        Line line = new Line("신분당", "RED", secondSection.getUpStation(), secondSection.getDownStation(), 3);
        line.getSections().add(thirdSection);
        line.getSections().add(firstSection);

        List<Station> stations = line.sortedStation();

        // then
        assertThat(stations)
                .containsExactly(강남역, 양재역, 판교역, 정자역);
    }

    @Test
    @DisplayName("삭제시 구간이 1개만 있으면 RuntimeException이 발생한다")
    void 삭제시_구간이_1개만_있으면_RuntimeException이_발생한다() {
        Section secondSection = new Section(null, 강남역, 양재역, 3);

        Line line = new Line("신분당", "RED", secondSection.getUpStation(), secondSection.getDownStation(), 3);

        // when / then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.removeStation(양재역));
    }

    @Test
    @DisplayName("역을 삭제하면 새로운 구간이 반환된다")
    void 역을_삭제하면_새로운_구간이_반환된다() {
        // given
        Line line = new Line("신분당", "RED", 강남역, 양재역, 3);
        line.getSections().add(new Section(null, 양재역, 판교역, 5));

        // when
        line.removeStation(양재역);

        // then
        assertThat(line.sortedStation2().toCollection())
                .containsExactly(강남역, 판교역);
    }
}