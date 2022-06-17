package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    Line 신분당선;
    Sections 모든구간;
    Station 광교역;
    Station 정자역;
    Station 강남역;

    @BeforeEach
    void setUp() {
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
        강남역 = new Station("강남역");
        신분당선 = new Line("신분당선", "bg-red-600");
        모든구간 = new Sections();
    }

    @Test
    @DisplayName("지하철역을 구간 순으로 정렬한다.")
    void getStationsInOrder() {
        모든구간.addLineStation(new Section(신분당선, 정자역, 강남역, 20000));
        모든구간.addLineStation(new Section(신분당선, 광교역, 정자역, 10000));

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역, 강남역);
    }

    @Test
    @DisplayName("빈 노선에 새로운 구간을 등록한다.")
    void addFirstSection() {
        Section 광교역_정자역 = new Section(신분당선, 광교역, 정자역, 20000);
        모든구간.addLineStation(광교역_정자역);

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역);
    }

    @Test
    @DisplayName("새로운 구간의 상행역이 기존에 존재할 경우 구간을 등록한다.")
    void addSectionWhenCommonUpstation() {
        모든구간.addLineStation(new Section(신분당선, 광교역, 정자역, 20000));
        Section 정자역_강남역 = new Section(신분당선, 정자역, 강남역, 10000);

        모든구간.addLineStation(정자역_강남역);

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역, 강남역);
    }

    @Test
    @DisplayName("새로운 구간의 상행역이 기존에 존재할 경우 구간을 등록한다.")
    void addSectionWhenCommonDownStation() {
        모든구간.addLineStation(new Section(신분당선, 정자역, 강남역, 20000));
        Section 광교역_정자역 = new Section(신분당선, 광교역, 정자역, 10000);

        모든구간.addLineStation(광교역_정자역);

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역, 강남역);
    }

    @Test
    @DisplayName("두 구간 사이에 새로운 구간을 등록한다.")
    void addMiddleSection() {
        모든구간.addLineStation(new Section(신분당선, 광교역, 정자역, 20000));
        모든구간.addLineStation(new Section(신분당선, 정자역, 강남역, 10000));

        Station 양재역 = new Station("양재역");
        모든구간.addLineStation(new Section(신분당선, 양재역, 강남역, 5000));

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역, 양재역, 강남역);
    }

    @Test
    @DisplayName("중복구간을 등록하려고 하면 예외를 발생시킨다.")
    void addDuplicateSection() {
        Section 정자역_강남역 = new Section(신분당선, 정자역, 강남역, 10000);
        모든구간.addLineStation(정자역_강남역);

        assertThatThrownBy(() -> 모든구간.addLineStation(정자역_강남역))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("상하행역이 모두 기존 구간에 없으면 예외를 발생시킨다.")
    void addNotExistingUpDownStation() {
        모든구간.addLineStation(new Section(신분당선, 광교역, 정자역, 20000));
        Station 논현역 = new Station("논현역");
        Station 신논현역 = new Station("신논현역");
        Section 논현역_신논현역 = new Section(신분당선, 논현역, 신논현역, 5000);

        assertThatThrownBy(() -> 모든구간.addLineStation(논현역_신논현역))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }


    @Test
    @DisplayName("마지막 구간을 제거하려고 하면 예외를 발생시킨다.")
    void removeLastSection() {
        Section 광교역_정자역 = new Section(신분당선, 광교역, 정자역, 20000);
        모든구간.addLineStation(광교역_정자역);

        assertThatThrownBy(() -> 모든구간.removeLineStation(신분당선, 정자역))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("마지막 구간은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("중간 역을 삭제하면 관련된 두 구간이 연결된다.")
    void removeStation() {
        Section 광교역_정자역 = new Section(신분당선, 광교역, 정자역, 20000);
        Section 정자역_강남역 = new Section(신분당선, 정자역, 강남역, 10000);
        모든구간.addLineStation(광교역_정자역);
        모든구간.addLineStation(정자역_강남역);

        모든구간.removeLineStation(신분당선, 정자역);

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 강남역);
    }
}
