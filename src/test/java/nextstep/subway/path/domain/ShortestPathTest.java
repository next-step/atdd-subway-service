package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Charge;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {

    private Line 신분당선;
    private Station 광교역;
    private Station 광교중앙역;
    private Station 상현역;
    private Station 성복역;

    private Section 광교_광교중앙역;
    private Section 상현역_광교역;
    private Section 성복역_상현역;

    private List<Section> 구간정보들;

    private ShortestPath 검색된_최단_경로_정보;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-blue-200", 100);
        광교역 = new Station("광교역");
        광교중앙역 = new Station("광교중앙역");
        상현역 = new Station("상현역");
        성복역 = new Station("성복역");
        광교_광교중앙역 = new Section(신분당선, 광교역, 광교중앙역, 10);
        상현역_광교역 = new Section(신분당선, 상현역, 광교역, 10);
        성복역_상현역 = new Section(신분당선, 성복역, 상현역, 10);

        구간정보들 = Arrays.asList(성복역_상현역, 상현역_광교역, 광교_광교중앙역);
        검색된_최단_경로_정보 = new ShortestPath(구간정보들);
    }

    @DisplayName("검색된 결과값인 Section List 를 입력받는다.")
    @Test
    void createTest() {
        assertThat(검색된_최단_경로_정보).isEqualTo(new ShortestPath(Arrays.asList(성복역_상현역, 상현역_광교역, 광교_광교중앙역)));
    }

    @DisplayName("잘못된 입력값을 입력시 에러가 발생한다.")
    @Test
    void invalidCreateTest() {
        assertThatThrownBy(() -> new ShortestPath(Collections.emptyList())).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ShortestPath(null)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Given 검색된 구간들에서
     * When 전체 길이를 요청하면
     * Then 전체 길이를 반환한다.
     */
    @DisplayName("검색된 최단경로에 대한 전체 길이를 알수 있다.")
    @Test
    void getTotalDistanceTest() {
        assertThat(검색된_최단_경로_정보.totalDistance()).isEqualTo(new Distance(30));
    }

    /**
     * Given 검색된 구간들에서
     * When 전체 요금을 요청하면
     * Then 전체 요금을 반환한다.
     */
    @DisplayName("검색된 최단경로에 대한  요금을 알수 있다.")
    @Test
    void totalChargeTest() {
        assertThat(검색된_최단_경로_정보.totalCharge()).isEqualTo(new Charge(1650).plus(신분당선.getExtraCharge()));
    }


    /**
     * Given 검색된 구간들에서
     * When 전체 역 정보를 요청하면
     * Then 정보를 반환한다.
     */
    @DisplayName("검색된 최단경로에 대한 역 정보를 알수 있다.")
    @Test
    void getStationsTest() {
        assertThat(검색된_최단_경로_정보.stations()).containsExactly(성복역,상현역,광교역,광교중앙역);
    }
}
