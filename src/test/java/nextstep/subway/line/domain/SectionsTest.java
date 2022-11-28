package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class SectionsTest {

    Sections sections = new Sections();
    Station 강남 = new Station("강남");
    Station 판교 = new Station("판교");
    Station 모란 = new Station("모란");
    Station 서현 = new Station("서현");

    @Test
    void 구간_목록에_등록한다() {
        Line line = new Line("신분당선", "red", 강남, 판교, 10);

        sections.add(line, 모란, 서현, 5);

        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    void 구간_목록을_가져온다() {
        sections.add(null, 강남, 판교, 5);

        List<Section> sectionList = sections.getSections();

        assertThat(sectionList).hasSize(1);
    }

    @Test
    void 가장_앞구간_상행역을_가져온다() {
        sections.add(null, 강남, 판교, 5);
        sections.add(null, 판교, 모란, 5);
        sections.add(null, 모란, 서현, 5);

        Station upStation = sections.findUpStation();

        assertThat(upStation.getName()).isEqualTo("강남");
    }

    @Test
    void 구간에_속한_역들을_가져온다() {
        sections.add(null, 강남, 판교, 5);
        sections.add(null, 판교, 모란, 5);
        sections.add(null, 모란, 서현, 5);

        List<Station> stations = sections.getStations();

        assertThat(stations).containsExactly(강남, 판교, 모란, 서현);
    }

    @Test
    void 이미_등록된_구간은_동일하게_등록_불가능() {
        sections.add(null, 강남, 판교, 5);

        ThrowingCallable addExistedSection = () -> sections.addSection(강남, 판교);

        assertThatIllegalArgumentException().isThrownBy(addExistedSection)
                .withMessageContaining("이미 등록된 구간 입니다.");
    }

    @Test
    void 매칭되는_역이_전혀_없으면_등록_불가능() {
        sections.add(null, 강남, 판교, 5);

        ThrowingCallable addNotExistedSection = () -> sections.addSection(모란, 서현);

        assertThatIllegalArgumentException().isThrownBy(addNotExistedSection)
                .withMessageContaining("등록할 수 없는 구간 입니다.");
    }
}
