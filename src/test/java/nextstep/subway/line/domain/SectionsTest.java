package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.line.domain.StationFixture.블루보틀역;
import static nextstep.subway.line.domain.StationFixture.선릉역;
import static nextstep.subway.line.domain.StationFixture.스타벅스역;
import static nextstep.subway.line.domain.StationFixture.역삼역;

import nextstep.subway.line.exception.InvalidRemoveException;
import nextstep.subway.line.exception.NoRelateStationException;
import nextstep.subway.line.exception.SectionAlreadyExistException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    void 정류장_순서별_조회() {
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));
        Assertions.assertThat(sections.getStations())
            .hasSize(3)
            .containsExactly(강남역, 역삼역, 블루보틀역);
    }

    @Test
    void 구간_추가() {
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));
        sections.add(null, 블루보틀역, 선릉역, 3);
        Assertions.assertThat(sections.getStations())
            .hasSize(4)
            .containsExactly(강남역, 역삼역, 블루보틀역, 선릉역);
    }

    @Test
    void 구간_추가중_해당_구간이_이미_있을경우_에러발생() {
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));
        Assertions.assertThatThrownBy(() -> sections.add(null, 역삼역, 블루보틀역, 3))
            .isInstanceOf(SectionAlreadyExistException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    void 구간_추가중_구간에_연결되는_역이_없을경우_에러발생생() {
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));
        Assertions.assertThatThrownBy(() -> sections.add(null, 선릉역, 스타벅스역, 3))
            .isInstanceOf(NoRelateStationException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    void 구간_제거() {
        Sections sections = Sections
            .of(new Section(null, 강남역, 역삼역, 4), new Section(null, 역삼역, 블루보틀역, 3));
        sections.remove(null, 블루보틀역);
        Assertions.assertThat(sections.getStations())
            .hasSize(2)
            .containsExactly(강남역, 역삼역);
    }

    @Test
    void 구간_제거할_항목이_없을경우_에러_발생() {
        Sections sections = Sections.of(new Section(null, 강남역, 역삼역, 4));
        Assertions.assertThatThrownBy(() -> sections.remove(null, 강남역))
            .isInstanceOf(InvalidRemoveException.class)
            .hasMessage("해당 정류장 제거시 구간이 모두 사라집니다.");
    }


}