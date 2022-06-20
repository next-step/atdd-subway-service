package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("station의 일급 컬랙션 테스트")
class StationsTest {
    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        //given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        final Section section = new Section(신분당선, 광교역, 판교역, 10);
        신분당선.addSection(section);
    }

    @Test
    @DisplayName("구간의 상행역 구한다")
    void getUpStation() {
        //when
        final Stations upStations = Stations.createUpStations(신분당선.getSections());

        //then
        assertThat(upStations.getStationElements()).contains(강남역, 광교역);
    }

    @Test
    @DisplayName("구간의 하행역 구한다")
    void getDownStation() {
        //when
        final Stations upStations = Stations.createDownStations(신분당선.getSections());

        //then
        assertThat(upStations.getStationElements()).contains(광교역, 판교역);
    }


    @Test
    @DisplayName("포함되지 않은 역의 첫번째를 구한다.")
    void notContainsFirstStation() {
        //givne
        Stations soruce = Stations.of(Arrays.asList(광교역, 판교역));
        Stations target = Stations.of(Arrays.asList(판교역, 강남역));

        //when
        final Optional<Station> notContainsFirstStation = soruce.isNotContainsFirstStation(target);

        //then
        assertAll(
                () -> assertThat(notContainsFirstStation.isPresent()).isTrue(),
                () -> assertThat(notContainsFirstStation.get()).isEqualTo(광교역)
        );
    }
}
