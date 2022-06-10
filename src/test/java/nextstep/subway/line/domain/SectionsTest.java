package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class SectionsTest {

    /*
     * Given 빈 노선에서
     * When 구간을 검색하면
     * Then 검색이 되지 않는다.
     * */
    @DisplayName("노선에 구간이 저장되어 있지 않은 상태에서 시작구간을 찾으면 검색이 되지 않는다.")
    @Test
    void invalidHasFindStartStationTest() {
        // Given
        final Line 구간이_없는_일호선 = new Line("일호선", "bg-white-20");

        // when
        final Optional<Station> isStartStation = 구간이_없는_일호선.getStartStation();

        // Then
        assertThat(isStartStation).isEqualTo(Optional.empty());
    }

    /*
     * Given 노선에서 구간들이 저장 하고
     * When 시작노선을 검색하면
     * Then 시작노선을 찾는다.
     * */
    @DisplayName("Line 은 StartStation 을 찾을수 있다.")
    @Test
    void findStartStationTest() {
        // Given
        신분당선.addSection(new Section(신분당선, 광교역, 광교중앙역, 10));
        신분당선.addSection(new Section(신분당선, 광교중앙역, 상현역, 10));
        신분당선.addSection(new Section(신분당선, 상현역, 성복역, 10));

        // When
        final Optional<Station> isStartStation = 신분당선.getStartStation();

        // Then
        assertThat(isStartStation.orElseThrow(EntityExistsException::new)).isEqualTo(광교역);
    }

    /*
     * Given 등록된 노선에서
     * When 역을 가져오면
     * Then 순서대로 역을 가져온다.
     * */
    @DisplayName("노선에서 역정보를 가져올때 순서대로 가져온다.")
    @Test
    void getStationsTest() {
        // Given
        신분당선.addSection(new Section(신분당선, 광교역, 광교중앙역, 10));
        신분당선.addSection(new Section(신분당선, 광교중앙역, 상현역, 10));
        신분당선.addSection(new Section(신분당선, 상현역, 성복역, 10));

        // When
        List<Station> 순서대로_역_정보 = 신분당선.getStations();
        // Then
        assertThat(순서대로_역_정보.toArray(new Station[0])).containsExactly(광교역,광교중앙역,상현역,성복역);
    }

    /*
     * Given 구간이 정보가 없는 노선에서
     * When 역을 가져오면
     * Then 결과값이 없다.
     * */
    @DisplayName("구간이 등록되지 않는 노선에서 역정보를 가져오면 결과 값이 없다.")
    @Test
    void invalidGetStationsWhenLineHasEmptySection() {
        // Given
        assertThat(신분당선.isSize()).isEqualTo(0);
        // When
        List<Station> 결과값이_비어_있음 = 신분당선.getStations();
        // Then
        assertThat(결과값이_비어_있음.isEmpty()).isTrue();
    }
}