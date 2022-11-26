package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class LineTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 선릉역 = new Station("선릉역");
    private final Station 블루보틀역 = new Station("블루보틀역");

    @Test
    void 정류장_조회() {
        //given
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(강남역, 역삼역, 3);
        이호선.addSection(역삼역, 선릉역, 4);

        //when
        List<Station> stations = 이호선.getStations();

        assertThat(stations).isEqualTo(Arrays.asList(강남역, 역삼역, 선릉역));
    }

    @Test
    void 구간이_한개인_경우_정류장_삭제_불가능() {
        //given;
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(강남역, 역삼역, 7);

        //when & then
        assertThatThrownBy(() -> 이호선.removeStation(역삼역))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 정류장_삭제() {
        //given
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(강남역, 역삼역, 4);
        이호선.addSection(역삼역, 선릉역, 3);

        //when
        이호선.removeStation(역삼역);

        //then
        assertThat(이호선.getStations()).isEqualTo(Arrays.asList(강남역, 선릉역));
    }

    @Test
    void 구간_추가() {
        //given
        Line 이호선 = new Line("이호선", "green");

        //when
        이호선.addSection(강남역, 역삼역, 7);
        이호선.addSection(역삼역, 선릉역, 3);

        //then
        assertThat(이호선.getStations()).isEqualTo(Arrays.asList(강남역, 역삼역, 선릉역));
    }

    @Test
    void 동일_구간이_이미_존재하면_오류발생() {
        //given
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(강남역, 역삼역, 7);

        //when & then
        assertThatThrownBy(() -> 이호선.addSection(강남역, 역삼역, 7))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    void 구간내_상행선과_하행선중_동일한_항목이_없다면_등록_불가능() {
        //given
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(강남역, 역삼역, 7);

        //when & then
        assertThatThrownBy(() -> 이호선.addSection(선릉역, 블루보틀역, 4))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }


}