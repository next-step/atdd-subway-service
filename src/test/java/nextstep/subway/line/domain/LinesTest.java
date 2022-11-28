package nextstep.subway.line.domain;

import nextstep.subway.fixture.LineTestFactory;
import nextstep.subway.fixture.SectionTestFactory;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LinesTest {
    @DisplayName("노선들에 포함된 전체 지하철 정보를 가져올 수 있다.")
    @Test
    void stations() {
        // given
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Station 인천역 = new Station("인천역");
        Station 부평역 = new Station("부평역");
        Line 신분당선 = LineTestFactory.create("신분당선", "bg-red-600", 판교역, 강남역, 10, 0);
        Line 일호선 = LineTestFactory.create("신분당선", "bg-red-600", 인천역, 부평역, 10, 0);

        Lines lines = new Lines(Arrays.asList(신분당선, 일호선));

        // when
        List<Station> results = lines.getStations();

        // then
        assertThat(results).containsOnly(판교역, 강남역, 인천역, 부평역);
    }

    @DisplayName("노선들에 포함된 전체 구간 정보를 가져올 수 있다.")
    @Test
    void sections() {
        // given
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Station 인천역 = new Station("인천역");

        Section section1 = SectionTestFactory.create(판교역, 강남역, 10);
        Section section2 = SectionTestFactory.create(강남역, 인천역, 10);

        Line 신분당선 = new Line("신분당선", "bg-red-600", new ExtraFare(0));
        신분당선.addSection(section1);
        신분당선.addSection(section2);

        Lines lines = new Lines(Arrays.asList(신분당선));

        // when
        List<Section> results = lines.getSections();

        // then
        assertThat(results).containsExactly(section1, section2);
    }

    @DisplayName("노선들에 지하철이 없는지 알 수있다.")
    @Test
    void hasNotStation() {
        // given
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Station 존재하지_않는_역 = new Station("안드로메다");

        Line 신분당선 = LineTestFactory.create("신분당선", "bg-red-600", 판교역, 강남역, 10, 0);
        Lines lines = new Lines(Arrays.asList(신분당선));

        // when & then
        assertAll(
                () -> assertThat(lines.hasNotStation(존재하지_않는_역)).isTrue(),
                () -> assertThat(lines.hasNotStation(판교역)).isFalse()
        );
    }

    @DisplayName("노선들 중 가장 높은 추가요금을 구할 수 있다.")
    @Test
    void maxExtraFare() {
        // given
        int expectExtraFare = 3_000;
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        Line 신분당선 = LineTestFactory.create("신분당선", "bg-red-600", 판교역, 강남역, 10, expectExtraFare);
        Line 이호선 = LineTestFactory.create("신분당선", "bg-red-600", 판교역, 강남역, 10, 1_000);
        Lines lines = new Lines(Arrays.asList(신분당선, 이호선));

        // when
        ExtraFare result = lines.maxExtraFare();

        // then
        assertThat(result.get()).isEqualTo(expectExtraFare);
    }
}
