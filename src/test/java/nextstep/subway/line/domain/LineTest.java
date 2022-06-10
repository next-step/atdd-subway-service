package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LineTest {
    private Line 신분당선;
    private Station 광교역;
    private Station 광교중앙역;
    private Station 상현역;
    private Station 성복역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-blue-200");
        광교역 = new Station("광교역");
        광교중앙역 = new Station("광교중앙역");
        상현역 = new Station("상현역");
        성복역 = new Station("성복역");
    }

    /*
    * Given 등록된 라인 에
    * When Section 을 추가하면
    * Then 성공적으로 등록된다.
    * */
    @DisplayName("Line 은 Section 을 추가 할수 있다.")
    @Test
    void addSectionTest() {

        // when
        신분당선.addSection(new Section(신분당선, 광교역, 광교중앙역, 10));

        // then
        assertThat(신분당선).isEqualTo(new Line("신분당선", "bg-blue-200",  광교역, 광교중앙역, 10));
    }

    /*
    * When 다른 노선에 등록된 Section 을 기존 노선에 등록하려면
    * Then 등록되지 않는다.
    * */
    @DisplayName("Section 추가시 같은 노선이 아닌 경우 에러를 발생한다.")
    @Test
    void invalidAddSectionTest() {
        // when
        final Section otherSection = new Section(new Line("일호선", "bg-red-200"), 광교역, 광교중앙역, 10);

        // then
        assertThatThrownBy(() -> 신분당선.addSection(otherSection))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("노선 정보가 다릅니다.");
    }

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
        final Optional<Station> isStartStation = 신분당선.getStartStation1();

        // Then
        assertThat(isStartStation.orElseThrow(EntityExistsException::new)).isEqualTo(광교역);
    }


}