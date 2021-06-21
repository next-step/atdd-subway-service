package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionGroupTest {
    private Station 방화역;
    private Station 하남검단산역;
    private Section 오호선생성구간;
    private SectionGroup 구간그룹;
    private Line 오호선;

    @BeforeEach
    private void setUp() {
        방화역 = new Station(1L, "방화역");
        하남검단산역 = new Station(2L, "하남검단산역");
        오호선생성구간 = new Section(1L, 방화역, 하남검단산역, 10);
        구간그룹 = new SectionGroup(new ArrayList<>());
        구간그룹.addSection(오호선생성구간);
        오호선 = new Line(1L, "오호선", "보라색", 구간그룹);
    }

    @DisplayName("구간등록 - 예외발생 - 구간의 두 역이 이미 노선에 등록되어있으면 구간 등록될 수 없다.")
    @Test
    public void 이미등록된두역_등록_예외() throws Exception {
        //given
        Section section = new Section(2L, 방화역, 하남검단산역, 5);

        //when
        //then
        assertThatThrownBy(() -> 구간그룹.addSection(section)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("구간등록 - 예외발생 - 구간의 두 역이 모두 노선에 등록되어있지 않으면 구간 등록될 수 없다.")
    @Test
    public void 등록되지않은두역_등록_예외() throws Exception {
        //given
        Station 청구역 = new Station(3L, "청구역");
        Station 미사역 = new Station(4L, "미사역");
        Section section = new Section(2L, 청구역, 미사역, 5);

        //when
        //then
        assertThatThrownBy(() -> 구간그룹.addSection(section)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("구간등록 - 상행종점역 등록")
    @Test
    public void 상행종점역_등록_확인() throws Exception {
        //given
        Station 새로운상행종점역 = new Station(3L, "새로운상행종점역");
        Section section = new Section(2L, 새로운상행종점역, 방화역, 3);

        //when
        구간그룹.addSection(section);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(새로운상행종점역, 방화역, 하남검단산역);
    }

    @DisplayName("구간등록 - 하행종점역 등록")
    @Test
    public void 하행종점역_등록_확인() throws Exception {
        //given
        Station 새로운하행종점역 = new Station(3L, "새로운하행종점역");
        Section section = new Section(2L, 하남검단산역, 새로운하행종점역, 3);

        //when
        구간그룹.addSection(section);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역, 새로운하행종점역);
    }

    @DisplayName("구간등록 - 중간상행역 등록")
    @Test
    public void 중간상행역_등록_확인() throws Exception {
        //given
        Station 중간상행역 = new Station(3L, "중간상행역");
        Section section = new Section(2L, 중간상행역, 하남검단산역, 3);

        //when
        구간그룹.addSection(section);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 중간상행역, 하남검단산역);
    }

    @DisplayName("구간등록 - 중간하행역 등록")
    @Test
    public void 중간하행역_등록_확인() throws Exception {
        //given
        Station 중간하행역 = new Station(3L, "중간하행역");
        Section section = new Section(2L, 방화역, 중간하행역, 3);

        //when
        구간그룹.addSection(section);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 중간하행역, 하남검단산역);
    }

    @DisplayName("구간제외 - 예외발생 - 노선에 포함된 역이 두 개 이하이면 구간을 제외할 수 없다.")
    @Test
    public void 역이두개이하일때_구간제외_예외() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> 구간그룹.removeSection(오호선, 방화역)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("구간제외 - 상행종점역 제외")
    @Test
    public void 상행종점역_제외_확인() throws Exception {
        //given
        Station 새로운상행종점역 = new Station(3L, "새로운상행종점역");
        Section section = new Section(2L, 새로운상행종점역, 방화역, 3);
        구간그룹.addSection(section);

        //when
        구간그룹.removeSection(오호선, 새로운상행종점역);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }

    @DisplayName("구간제외 - 하행종점역 제외")
    @Test
    public void 하행종점역_제외_확인() throws Exception {
        //given
        Station 새로운하행종점역 = new Station(3L, "새로운하행종점역");
        Section section = new Section(2L, 하남검단산역, 새로운하행종점역, 3);
        구간그룹.addSection(section);

        //when
        구간그룹.removeSection(오호선, 새로운하행종점역);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }

    @DisplayName("구간제외 - 중간역 제외")
    @Test
    public void 중간역_제외_확인() throws Exception {
        //given
        Station 새로운중간역 = new Station(3L, "새로운중간역");
        Section section = new Section(2L, 방화역, 새로운중간역, 3);
        구간그룹.addSection(section);

        //when
        구간그룹.removeSection(오호선, 새로운중간역);

        //then
        assertThat(구간그룹.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }
}
