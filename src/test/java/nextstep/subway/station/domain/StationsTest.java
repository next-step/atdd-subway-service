package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    private final static Line 신분당선 = new Line("신분당선", "red");
    private final static Station 신논현역 = new Station("신논현");
    private final static Station 강남역 = new Station("강남역");
    private final static Station 양재역 = new Station("양재역");

    @Test
    void 구간의_상행역_하행역이_포함안됐는지_확인() {
        // given
        Stations stations = new Stations();
        stations.add(양재역);
        Section section = new Section(신분당선, 신논현역, 강남역, 5);

        // when
        boolean actual = stations.isNoneMatchStation(section);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 구간의_상행역이_포함됐는지_확인() {
        // given
        Stations stations = new Stations();
        stations.add(신논현역);
        Section section = new Section(신분당선, 신논현역, 강남역, 5);

        // when
        boolean actual = stations.isAnyMatchUpStation(section);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 구간의_하행역이_포함됐는지_확인() {
        // given
        Stations stations = new Stations();
        stations.add(강남역);
        Section section = new Section(신분당선, 신논현역, 강남역, 5);

        // when
        boolean actual = stations.isAnyMatchDownStation(section);

        // then
        assertThat(actual).isTrue();
    }

}