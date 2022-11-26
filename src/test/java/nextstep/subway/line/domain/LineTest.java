package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    void 정류장_조회() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(강남역, 역삼역));
        sections.add(new Section(역삼역, 선릉역));
        Line 이호선 = new Line("이호선", "green", sections);

        //when
        List<Station> stations = 이호선.getStations();

        Assertions.assertThat(stations).isEqualTo(Arrays.asList(강남역, 역삼역, 선릉역));
    }

}