package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.exception.SectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {
    Station 강남역;
    Station 광교역;
    Station 양재역;
    Station 양재시민의숲역;
    Station 판교역;
    Line 신분당선;
    Sections 신분당선_구간 = new Sections();

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        양재시민의숲역 = new Station("양재시민의숲역");
        판교역 = new Station("판교역");
        신분당선 = new Line("신분당선", "red");
        신분당선_구간.add(new Section(신분당선, 양재역, 판교역, 10));
    }

    @DisplayName("이미 등록한 구간을 추가하는 경우 테스트")
    @Test
    void validate() {
        //given
        Section 이미_등록한_구간 = new Section(신분당선, 양재역, 판교역, 3);

        //when//then
        assertThatThrownBy(() -> 신분당선_구간.add(이미_등록한_구간))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("상행역 하행역 둘다 일치하는 역이 없는 경우 테스트")
    @Test
    void validate2() {
        //given
        Section 없는_구간 = new Section(신분당선, 강남역, 광교역, 13);

        //when//then
        assertThatThrownBy(() -> 신분당선_구간.add(없는_구간))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("노선 사이에 구간을 등록 테스트")
    @Test
    void addSection() {
        //given
        Section 사이_구간 = new Section(신분당선, 양재역, 양재시민의숲역, 2);

        //when
        신분당선_구간.add(사이_구간);

        //then
        assertThat(신분당선_구간.getOrderedStations(신분당선)).isEqualTo(Arrays.asList(양재역, 양재시민의숲역, 판교역));
    }

    @DisplayName("노선 사이에 상행 구간을 등록 테스트")
    @Test
    void addSection2() {
        //given
        Section 상행_구간 = new Section(신분당선, 강남역, 양재역, 2);

        //when
        신분당선_구간.add(상행_구간);

        //then
        assertThat(신분당선_구간.getOrderedStations(신분당선)).isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("노선 사이에 하행 구간을 등록 테스트")
    @Test
    void addSection3() {
        //given
        Section 하행_구간 = new Section(신분당선, 판교역, 광교역, 2);

        //when
        신분당선_구간.add(하행_구간);

        //then
        assertThat(신분당선_구간.getOrderedStations(신분당선)).isEqualTo(Arrays.asList(양재역, 판교역, 광교역));
    }

    @DisplayName("노선의 중간 구간을 제거 테스트")
    @Test
    void removeSection() {
        //given
        Section 사이_구간 = new Section(신분당선, 양재역, 양재시민의숲역, 2);
        신분당선_구간.add(사이_구간);
        //when
        신분당선_구간.remove(신분당선, 양재시민의숲역);
        //then
        assertThat(신분당선_구간.getOrderedStations(신분당선)).isEqualTo(Arrays.asList(양재역, 판교역));
    }

    @DisplayName("노선의 종점 구간을 제거 테스트")
    @Test
    void removeSection2() {
        //given
        Section 사이_구간 = new Section(신분당선, 양재역, 양재시민의숲역, 2);
        신분당선_구간.add(사이_구간);
        //when
        신분당선_구간.remove(신분당선, 양재역);
        //then
        assertThat(신분당선_구간.getOrderedStations(신분당선)).isEqualTo(Arrays.asList(양재시민의숲역, 판교역));
    }

    @DisplayName("노선에 등록되지 않은 역을 제거하는 경우 테스트")
    @Test
    void validate3() {
        //when//then
        assertThatThrownBy(() -> 신분당선_구간.remove(신분당선, 양재시민의숲역))
                .isInstanceOf(SectionException.class);
    }

    @DisplayName("노선의 마지막 구간을 제거하는 경우 테스트")
    @Test
    void validate4() {
        //when//then
        assertThatThrownBy(() -> 신분당선_구간.remove(신분당선, 판교역))
                .isInstanceOf(SectionException.class);
    }
}
