package nextstep.subway.line.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("지하철 구간 일급컬렉션 관련 기능")
public class SectionsTest {
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;
    private Station 역삼역;
    private Station 선릉역;

    private Section 강남_광교_구간;
    private Section 강남_양재_구간;
    private Section 양재_광교_구간;
    private Section 역삼_선릉_구간;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        
        강남_광교_구간 = new Section(null, 강남역, 광교역, 100);
        강남_양재_구간 = new Section(null, 강남역, 양재역, 40);
        양재_광교_구간 = new Section(null, 양재역, 광교역, 60);
        역삼_선릉_구간 = new Section(null, 역삼역, 선릉역, 30);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void add_section() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        
        // when
        sections.add(강남_양재_구간);

        // then
        Assertions.assertThat(sections).isEqualTo(Sections.of(양재_광교_구간, 강남_양재_구간));
    }

    @DisplayName("신규 구간의 모든 역이 기동록된 구간의 역에 있는 경우 에러가 발생한다.")
    @Test
    void exception_addSection_allHasStation() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        sections.add(강남_양재_구간);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> sections.add(양재_광교_구간));
    }

    @DisplayName("신규 구간의 모든 역이 기동록된 구간의 역에 없는 경우 에러가 발생한다.")
    @Test
    void exception_addSection_allNotHasStation() {
        // given
        Sections sections = Sections.of(강남_광교_구간);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> sections.add(역삼_선릉_구간));
    }

    @DisplayName("구간의 상행 종점역을 삭제한다.")
    @Test
    void delete_upTerminalStation() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        sections.add(강남_양재_구간);

        // when
        sections.deleteStation(강남역);
        
        // then
        Assertions.assertThat(sections).isEqualTo(Sections.of(양재_광교_구간));
    }

    @DisplayName("구간의 하행 종점역을 삭제한다.")
    @Test
    void delete_downTerminalStation() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        sections.add(강남_양재_구간);

        // when
        sections.deleteStation(광교역);
        
        // then
        Assertions.assertThat(sections).isEqualTo(Sections.of(강남_양재_구간));
    }

    @DisplayName("구간의 중간에 위치한 역을 삭제한다.")
    @Test
    void delete_midTerminalStation() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        sections.add(강남_양재_구간);

        // when
        sections.deleteStation(양재역);
        
        // then
        Assertions.assertThat(sections).isEqualTo(Sections.of(강남_광교_구간));
    }
    
    @DisplayName("기등록된 구간이 1개 이하일떄 삭제 할 경우 에러가 발생한다.")
    @Test
    void exception_deleteStation_RegistedSectionCountOneUnder() {
        // given
        Sections sections = Sections.of(강남_광교_구간);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> sections.deleteStation(강남역));
    }

    @DisplayName("기등록된 구간의 역리스트를 조회한다.")
    @Test
    void search_stations() {
        // given
        Sections sections = Sections.of(강남_광교_구간);
        sections.add(강남_양재_구간);

        // when
        List<Station> findStations = sections.findStations();

        // then
        Assertions.assertThat(findStations).isEqualTo(List.of(강남역, 양재역, 광교역));
    }

}
