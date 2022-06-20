package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    Station 강남역;
    Station 광교역;
    Station 판교역;
    Station 성수역;
    Line 신분당선;
    Sections 신분당선_구간;


    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");
        성수역 = new Station("성수역");

        신분당선 = new Line("신분당선", "bg-red-600");
        신분당선_구간 = new Sections(new Section(신분당선, 강남역, 광교역, 10));
    }


    @Test
    @DisplayName("이미 추가할 구간에 상행역과 하행역이 존재하면 추가를 할 수 없다.")
    void addSectionValid() {
        //given
        final Section section = new Section(신분당선, 강남역, 판교역, 10);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 신분당선_구간.addSection(section)
        );
    }

    @Test
    @DisplayName("이미 추가할 구간에 상행역과 하행역이 존재하지 않으면 추가를 할 수 없다.")
    void addSectionExistValid() {
        //given
        final Section section = new Section(신분당선, 성수역, new Station("가로숲역"), 10);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 신분당선_구간.addSection(section)
                );
    }

    @Test
    @DisplayName("이미 추가할 구간에 거리보다 추가할 거리가 크면 추가 할수 없다.")
    void addSectionDistanceOver() {
        //given
        final Section section = new Section(신분당선, 성수역, 판교역, 10);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> 신분당선_구간.addSection(section)
        );
    }

    @Test
    @DisplayName("가운대 구간이 추가된다")
    void addSection() {
        //given
        신분당선_구간.addSection(new Section(신분당선, 광교역, 판교역, 10));
        final Section section = new Section(신분당선, 성수역, 판교역, 2);

        //when
        신분당선_구간.addSection(section);

        //then
        assertThat(신분당선_구간.getOrderStations()).containsExactly(강남역, 광교역, 성수역, 판교역);
    }

    @Test
    @DisplayName("하행역 구간이 추가된다")
    void addDownSection() {
        //given
        신분당선_구간.addSection(new Section(신분당선, 광교역, 판교역, 10));
        final Section section = new Section(신분당선, 판교역, 성수역, 2);

        //when
        신분당선_구간.addSection(section);

        //then
        assertThat(신분당선_구간.getOrderStations()).containsExactly(강남역, 광교역, 판교역, 성수역);
    }

    @Test
    @DisplayName("상행역 구간이 추가된다")
    void addUpSection() {
        //given
        신분당선_구간.addSection(new Section(신분당선, 광교역, 판교역, 10));
        final Section section = new Section(신분당선, 성수역, 강남역, 2);

        //when
        신분당선_구간.addSection(section);

        //then
        assertThat(신분당선_구간.getOrderStations()).containsExactly(성수역, 강남역, 광교역, 판교역);
    }


    @Test
    @DisplayName("역이 삭제된다.")
    void removeStation() {
        //when
        신분당선_구간.addSection(new Section(신분당선, 광교역, 판교역, 10));
        신분당선_구간.removeStation(광교역);

        //then
        assertAll(
                () -> assertThat(신분당선_구간.getOrderStations()).containsExactly(강남역, 판교역),
                () -> assertThat(신분당선_구간.getSectionElements().get(0).getDistance()).isEqualTo(Distance.of(20))
        );

    }

    @Test
    @DisplayName("종점역이 삭제된다.")
    void removeStation2() {
        //when
        신분당선_구간.addSection(new Section(신분당선, 광교역, 판교역, 10));
        신분당선_구간.removeStation(강남역);

        //then
        assertAll(
                () -> assertThat(신분당선_구간.getOrderStations()).containsExactly(광교역,판교역),
                () -> assertThat(신분당선_구간.getSectionElements().get(0).getDistance()).isEqualTo(Distance.of(10))
        );
    }

    @Test
    @DisplayName("순서에 맞게 역을 조회한다.")
    void searchStation() {
        //given
        final Section section = new Section(신분당선, 성수역, 강남역, 2);
        신분당선_구간.addSection(section);

        //when
        List<Station> 정렬된_역 = 신분당선_구간.getOrderStations();

        //then
        assertThat(정렬된_역).containsExactly(성수역, 강남역, 광교역);
    }

}
