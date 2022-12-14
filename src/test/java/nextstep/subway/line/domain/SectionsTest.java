package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.line.domain.StationFixture.블루보틀역;
import static nextstep.subway.line.domain.StationFixture.선릉역;
import static nextstep.subway.line.domain.StationFixture.스타벅스역;
import static nextstep.subway.line.domain.StationFixture.양재;
import static nextstep.subway.line.domain.StationFixture.역삼역;

import java.util.List;
import nextstep.subway.line.exception.InvalidRemoveException;
import nextstep.subway.line.exception.NoRelateStationException;
import nextstep.subway.line.exception.SectionAlreadyExistException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    void 정류장_순서별_조회() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when
        List<Station> stations = sections.getStations();

        //then
        Assertions.assertThat(stations)
            .hasSize(3)
            .containsExactly(강남역, 역삼역, 블루보틀역);
    }

    @Test
    void 구간_추가() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when
        sections.add(null, 블루보틀역, 선릉역, new Distance(3));

        //then
        Assertions.assertThat(sections.getStations())
            .hasSize(4)
            .containsExactly(강남역, 역삼역, 블루보틀역, 선릉역);
    }

    @Test
    void 구간_추가중_해당_구간이_이미_있을경우_에러발생() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when & then
        Assertions.assertThatThrownBy(() -> sections.add(null, 역삼역, 블루보틀역, new Distance(3)))
            .isInstanceOf(SectionAlreadyExistException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    void 구간_추가중_구간에_연결되는_역이_없을경우_에러발생생() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when & then
        Assertions.assertThatThrownBy(() -> sections.add(null, 선릉역, 스타벅스역, new Distance(3)))
            .isInstanceOf(NoRelateStationException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    void 구간_제거() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when
        sections.remove(null, 블루보틀역);

        //then
        Assertions.assertThat(sections.getStations())
            .hasSize(2)
            .containsExactly(강남역, 역삼역);
    }

    @Test
    void 구간_제거할_항목이_없을경우_에러_발생() {
        //given
        Sections sections = Sections.of(new Section(null, 강남역, 역삼역, 4));

        //when & then
        Assertions.assertThatThrownBy(() -> sections.remove(null, 강남역))
            .isInstanceOf(InvalidRemoveException.class)
            .hasMessage("해당 정류장 제거시 구간이 모두 사라집니다.");
    }

    @Test
    void 구간_포함_여부_확인() {
        //given
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));

        //when
        Assertions.assertThat(sections.contains(new Section(null, 강남역, 역삼역, 3))).isTrue();
        Assertions.assertThat(sections.contains(new Section(null, 강남역, 양재, 4))).isFalse();
    }

}