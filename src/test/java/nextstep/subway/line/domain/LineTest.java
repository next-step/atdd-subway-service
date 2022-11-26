package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class LineTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 선릉역 = new Station("선릉역");

    @Test
    void 정류장_조회() {
        //given
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(강남역, 역삼역));
        sections.add(new Section(역삼역, 선릉역));
        Line 이호선 = new Line("이호선", "green", sections);

        //when
        List<Station> stations = 이호선.getStations();

        assertThat(stations).isEqualTo(Arrays.asList(강남역, 역삼역, 선릉역));
    }

    @Test
    void 구간이_한개인_경우_정류장_삭제_불가능() {
        //given
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(강남역, 역삼역));
        Line 이호선 = new Line("이호선", "green", sections);

        //when & then
        assertThatThrownBy(() -> 이호선.removeStation(역삼역))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 정류장_삭제() {
        //given
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(강남역, 역삼역));
        sections.add(new Section(역삼역, 선릉역));
        Line 이호선 = new Line("이호선", "green", sections);

        //when
        이호선.removeStation(역삼역);

        //then
        assertThat(이호선.getStations()).isEqualTo(Arrays.asList(강남역, 선릉역));
    }


}