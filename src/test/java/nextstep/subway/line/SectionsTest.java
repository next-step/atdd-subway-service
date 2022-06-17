package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SectionsTest {
    private Line 지하철2호선;
    private Station 강남역;
    private Station 건대입구역;

    @BeforeEach
    public void setup() {
        강남역 = new Station("강남역");
        건대입구역 = new Station("건대입구역");
        지하철2호선 = new Line("지하철2호선", "GREEN", 강남역, 건대입구역, 10);
    }

    @Test
    void 이미_등록된_구간_등록시_에러_발생() {
        assertThrows(RuntimeException.class, () -> {
            지하철2호선.addSection(강남역, 건대입구역, 10);
        });
    }

    @Test
    void 상행역과_하행역_모두_존재하지_않는_구간_등록_시_에러_발생() {
        //given
        Station 신림역 = new Station("신림역");
        Station 합정역 = new Station("합정역");

        //when & then
        assertThrows(RuntimeException.class, () -> 지하철2호선.addSection(신림역, 합정역, 10));
    }

    @Test
    void 기존_역_사이_길이보다_크거나_같은_구간_등록시_에러_발생() {

        //when sections에 첫등록보다 길이가 큰 구간을 등록할시 then 에러가 발생한다.
        Station 삼성역 = new Station("삼성역");

        assertThrows(RuntimeException.class, () -> 지하철2호선.addSection(강남역, 삼성역, 20));
    }

    @Test
    void 새로운_상행역_등록() {
        //given
        Station 신림역 = new Station("신림역");

        //when
        지하철2호선.addSection(신림역, 강남역, 5);

        //then
        assertThat(지하철2호선.getStations().contains(신림역)).isTrue();
    }

    @Test
    void 새로운_하행역_등록() {
        //given
        Station 성수역 = new Station("성수역");

        //when
        지하철2호선.addSection(건대입구역, 성수역, 5);

        //then
        assertThat(지하철2호선.getStations().contains(성수역)).isTrue();
    }

    @Test
    void 새로운_역_등록() {
        //given 새로운역 생성
        Station 삼성역 = new Station("삼성역");

        //when 중간에 역 추가 등록
        지하철2호선.addSection(강남역, 삼성역, 5);

        //then 거리는 기존 10에서 각각 5가 된다.
        for(Section section : 지하철2호선.getSections()) {
            assertThat(section.getDistance()).isEqualTo(5);
        }
    }
}
