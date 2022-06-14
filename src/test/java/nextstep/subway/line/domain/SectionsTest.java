package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityExistsException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class SectionsTest {
    private Line 신분당선;
    private Station 광교역;
    private Station 광교중앙역;
    private Station 상현역;
    private Station 성복역;

    private Sections 구간들이_저장된_노선들;
    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-blue-200");
        광교역 = new Station("광교역");
        광교중앙역 = new Station("광교중앙역");
        상현역 = new Station("상현역");
        성복역 = new Station("성복역");
        구간들이_저장된_노선들 = new Sections(Arrays.asList(
                new Section(신분당선, 광교역, 광교중앙역, 10),
                new Section(신분당선, 광교중앙역, 상현역, 10),
                new Section(신분당선, 상현역, 성복역, 10)));
    }

    /*
     * Given 빈 노선에서
     * When 구간을 검색하면
     * Then 검색이 되지 않는다.
     * */
    @DisplayName("구간이 저장되어 있지 않은 상태에서 시작구간을 찾으면 검색이 되지 않는다.")
    @Test
    void invalidHasFindStartStationTest() {
        // Given
        final Sections 빈_구간_정보들 = new Sections(Collections.emptyList());

        // when
        final Optional<Station> isStartStation = 빈_구간_정보들.getStartStation();

        // Then
        assertThat(isStartStation).isEqualTo(Optional.empty());
    }

    /*
     * Given 구간들이 저장 하고
     * When 시작노선을 검색하면
     * Then 시작노선을 찾는다.
     * */
    @DisplayName("저장된 구간들에서 StartStation 을 찾을수 있다.")
    @Test
    void findStartStationTest() {
        // When
        final Optional<Station> isStartStation = 구간들이_저장된_노선들.getStartStation();

        // Then
        assertThat(isStartStation.orElseThrow(EntityExistsException::new)).isEqualTo(광교역);
    }

    /*
     * Given 저장된 구간들에서
     * When 역 정보를 가져오면
     * Then 순서대로 역을 가져온다.
     * */
    @DisplayName("저장된 구간들에서 역정보를 가져올때 순서대로 가져온다.")
    @Test
    void getStationsTest() {
        // When
        List<Station> 순서대로_역_정보 = 구간들이_저장된_노선들.getStations();
        // Then
        assertThat(순서대로_역_정보.toArray(new Station[0])).containsExactly(광교역, 광교중앙역, 상현역, 성복역);
    }

    /*
     * Given 구간이 정보가 없는 노선에서
     * When 역을 가져오면
     * Then 결과값이 없다.
     * */
    @DisplayName("구간들이 저장되어 있지 않는 상태에서 역정보를 가져오면 결과 값이 없다.")
    @Test
    void invalidGetStationsWhenLineHasEmptySection() {
        // Given
        final Sections 빈_구간_정보들 = new Sections(Collections.emptyList());

        // When
        List<Station> 결과값이_비어_있음 = 빈_구간_정보들.getStations();
        // Then
        assertThat(결과값이_비어_있음.isEmpty()).isTrue();
    }

    @DisplayName("구간 정보들 입력시 잘못된 값을 입력할 경우 에러가 발생한다.")
    @Test
    void invalidCreateTest() {
        assertThatThrownBy(() -> new Sections(null)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    /*
     * Given 저장된 구간들에서
     * When 특정 구간을 포함되어 있는지 요청하면
     * Then 포함 여부를 알수 있다.
     * */
    @DisplayName("구간 정보들 에서 특정 구간을 있는지 여부를 알수 있다.")
    @Test
    void isContainsTest() {
        // When
        final boolean result = 구간들이_저장된_노선들.isContains(new Section(신분당선, 광교역, 광교중앙역, 10));
        // Then
        assertThat(result).isTrue();
    }

    /*
     * Given 저장된 구간들 에서
     * When 특정 구간보다 거리가 긴 구간을 추가하면
     * Then 등록이 실패한다.
     * */
    @DisplayName("구간 추가시 기존구간보다 길이가 긴 구간을 추가하면 등록이 되지 않는다.")
    @Test
    void invalidDistanceTest() {
        // When
        assertThatThrownBy(() -> new Section(신분당선, 광교중앙역, new Station("수원역"), 11))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    /*
     * Given 저장된 구간들 에서
     * When 기존에 등록된 구간을 추가하면
     * Then 등록이 실패한다.
     * */
    @DisplayName("중복 구간을 허용하지  않는다.")
    @Test
    void noOverlapTest() {
        // When
        assertThatThrownBy(() -> new Section(신분당선, 광교중앙역, 상현역, 2))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    /*
     * Given 저장된 구간들 에서
     * When 새로운 구간을 추가하면
     * Then 정상 등록이 된다.
     * */
    @DisplayName("새로운 구간을 추가시 정상 등록이 된다.")
    @Test
    void addSectionTest() {
        // Given
        Station 양재시민의숲 = new Station("양재시민의숲");
        // When
        구간들이_저장된_노선들.addSection(new Section(신분당선, 광교중앙역,양재시민의숲, 3));

        // Then
        assertThat(구간들이_저장된_노선들.getStations().toArray(new Station[0])).containsExactly(광교역, 광교중앙역, 양재시민의숲, 상현역, 성복역);
    }
}