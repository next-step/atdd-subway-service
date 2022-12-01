package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDistanceException;
import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    Sections 구간;
    Line 노선;
    Station 당산역;
    Station 여의도역;
    Station 노량진역;
    Station 동작역;

    @BeforeEach
    void setup() {
        당산역 = new Station("당산역");
        여의도역 = new Station("여의도역");
        노량진역 = new Station("노량진역");
        동작역 = new Station("동작역");
        노선 = new Line("9호선", "color");
        구간 = new Sections();
    }

    @Test
    @DisplayName("구간 중간에 역에 추가할 수 있다")
    void addSection() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        구간.addSection(new Section(노선, 당산역, 여의도역, 10));
        구간.addSection(new Section(노선, 여의도역, 노량진역, 10));

        역_순서_검증(당산역, 여의도역, 노량진역, 동작역);
    }

    @Test
    @DisplayName("구간 끝에 역에 추가할 수 있다")
    void addSectionAtLastStation() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        구간.addSection(new Section(노선, 여의도역, 당산역, 10));
        구간.addSection(new Section(노선, 동작역, 노량진역, 10));

        역_순서_검증(여의도역, 당산역, 동작역, 노량진역);
    }

    @Test
    @DisplayName("구간 중간에 역에 추가할 경우 거리를 분할한다")
    void distanceSplit() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        구간.addSection(new Section(노선, 당산역, 여의도역, 10));
        구간.addSection(new Section(노선, 여의도역, 노량진역, 10));

        구간_거리_검증(10, 10, 80);
    }

    @Test
    @DisplayName("구간 중간에 역에 추가할 경우 거리는 기존 구간보다 작을 경우 예외를 던진다")
    void whenDistanceSplitDistanceLessThanBefore() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));

        Section section = new Section(노선, 당산역, 여의도역, 100 + 1);

        assertThatThrownBy(() -> 구간.addSection(section))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    @DisplayName("구간 끝에 역에 추가할 경우 거리는 기존 구간에 상관없다")
    void whenDistanceSplitDistanceNoMatter() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));

        구간.addSection(new Section(노선, 여의도역, 당산역, 100+1));

        구간_거리_검증(100, 101);
    }

    @Test
    @DisplayName("중간역을 삭제할 경우 역이 재배치 된다")
    void removeSection() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        구간.addSection(new Section(노선, 당산역, 여의도역, 10));
        구간.addSection(new Section(노선, 여의도역, 노량진역, 10));

        구간.removeSection(여의도역);
        역_순서_검증(당산역, 노량진역, 동작역);

        구간.removeSection(노량진역);
        역_순서_검증(당산역, 동작역);
    }

    @Test
    @DisplayName("종점역을 삭제할 경우 종점역이 삭제된다")
    void removeSectionLastStation() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        구간.addSection(new Section(노선, 당산역, 여의도역, 10));
        구간.addSection(new Section(노선, 여의도역, 노량진역, 10));

        구간.removeSection(당산역);
        역_순서_검증(여의도역, 노량진역, 동작역);

        구간.removeSection(동작역);
        역_순서_검증(여의도역, 노량진역);
    }

    @Test
    @DisplayName("존재하지 않는 역을 삭제할 수 없다")
    void removeSectionWhenStationNotExists() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));
        Station 홍대역 = new Station("홍대역");

        assertThatThrownBy(() -> 구간.removeSection(홍대역))
                .isInstanceOf(InvalidRemoveSectionException.class);
    }

    @Test
    @DisplayName("구간이 1개인 노선은 구간을 삭제할 수 없다")
    void removeSectionWhenOnlyOneSection() {
        구간.addSection(new Section(노선, 당산역, 동작역, 100));

        assertThatThrownBy(() -> 구간.removeSection(당산역))
                .isInstanceOf(InvalidRemoveSectionException.class);
    }

    private void 구간_거리_검증(Integer ...distances) {
        List<Distance> expectDistances = Arrays.stream(distances).map(Distance::new).collect(Collectors.toList());
        assertThat(구간.getSections())
                .extracting(Section::getDistance)
                .containsExactlyInAnyOrderElementsOf(
                        expectDistances);
    }

    private void 역_순서_검증(Station ...역_목록) {
        List<String> 역_목록_이름 = Arrays.stream(역_목록).map(Station::getName).collect(Collectors.toList());

        assertThat(구간.getStations())
                .extracting(Station::getName)
                .containsExactlyElementsOf(역_목록_이름);
    }
}