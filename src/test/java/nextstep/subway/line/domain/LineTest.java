package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {
    private Station 방화역;
    private Station 하남검단산역;
    private Section 오호선생성구간;
    private Line 오호선;

    @BeforeEach
    public void setUp() {
        방화역 = new Station(1L, "방화역");
        하남검단산역 = new Station(2L, "하남검단산역");
        오호선생성구간 = new Section(1L, 방화역, 하남검단산역, 10);
        오호선 = new Line(1L, "오호선", "보라색");
        오호선.addSection(오호선생성구간);
    }

    @DisplayName("구간등록 - 상행종점역 등록")
    @Test
    public void 상행종점역_등록_확인() throws Exception {
        //given
        Station 새로운상행종점역 = new Station(3L, "새로운상행종점역");
        Section section = new Section(2L, 새로운상행종점역, 방화역, 3);

        //when
        오호선.addSection(section);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(새로운상행종점역, 방화역, 하남검단산역);
    }

    @DisplayName("구간등록 - 하행종점역 등록")
    @Test
    public void 하행종점역_등록_확인() throws Exception {
        //given
        Station 새로운하행종점역 = new Station(3L, "새로운하행종점역");
        Section section = new Section(2L, 하남검단산역, 새로운하행종점역, 3);

        //when
        오호선.addSection(section);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역, 새로운하행종점역);
    }

    @DisplayName("구간등록 - 중간상행역 등록")
    @Test
    public void 중간상행역_등록_확인() throws Exception {
        //given
        Station 중간상행역 = new Station(3L, "중간상행역");
        Section section = new Section(2L, 중간상행역, 하남검단산역, 3);

        //when
        오호선.addSection(section);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 중간상행역, 하남검단산역);
    }

    @DisplayName("구간등록 - 중간하행역 등록")
    @Test
    public void 중간하행역_등록_확인() throws Exception {
        //given
        Station 중간하행역 = new Station(3L, "중간하행역");
        Section section = new Section(2L, 방화역, 중간하행역, 3);

        //when
        오호선.addSection(section);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 중간하행역, 하남검단산역);
    }

    @DisplayName("구간제외 - 상행종점역 제외")
    @Test
    public void 상행종점역_제외_확인() throws Exception {
        //given
        Station 새로운상행종점역 = new Station(3L, "새로운상행종점역");
        Section section = new Section(2L, 새로운상행종점역, 방화역, 3);
        오호선.addSection(section);

        //when
        오호선.removeSection(새로운상행종점역);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }

    @DisplayName("구간제외 - 하행종점역 제외")
    @Test
    public void 하행종점역_제외_확인() throws Exception {
        //given
        Station 새로운하행종점역 = new Station(3L, "새로운하행종점역");
        Section section = new Section(2L, 하남검단산역, 새로운하행종점역, 3);
        오호선.addSection(section);

        //when
        오호선.removeSection(새로운하행종점역);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }

    @DisplayName("구간제외 - 중간역 제외")
    @Test
    public void 중간역_제외_확인() throws Exception {
        //given
        Station 새로운중간역 = new Station(3L, "새로운중간역");
        Section section = new Section(2L, 방화역, 새로운중간역, 3);
        오호선.addSection(section);

        //when
        오호선.removeSection(새로운중간역);

        //then
        assertThat(오호선.findStationsOrderUpToDown()).containsExactly(방화역, 하남검단산역);
    }
}
