package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest{

    private Line line;
    private Station 강남;
    private Station 역삼;
    private Station 선릉;
    private Station 삼성;
    private Station 교대;

    @BeforeEach
    void setUp() {
        교대 = new Station("강남");
        강남 = new Station("강남");
        역삼 = new Station("역삼");
        선릉 = new Station("선릉");
        삼성 = new Station("삼성");
        line = new Line("2호선", "초록", 강남, 선릉, 10, 0);
    }

    @DisplayName("상행역이 같은 역 추가")
    @Test
    void addSectionUpStationSame() {
        Section newSection = new Section(line, 강남, 역삼, 4);
        line.addSection(newSection);

        assertThat(line.getStations())
                .containsExactly(강남, 역삼, 선릉);

    }

    @DisplayName("하행역이 같은 역 추가")
    @Test
    void addSectionDownStationSame() {
        Section newSection = new Section(line, 역삼, 선릉, 4);
        line.addSection(newSection);

        assertThat(line.getStations())
                .containsExactly(강남, 역삼, 선릉);

    }

    @DisplayName("상행역 종점에 추가")
    @Test
    void addSectionEndUpStation() {
        Section newSection = new Section(line, 교대, 강남, 4);
        line.addSection(newSection);

        line.removeLineStation(교대);
        assertThat(line.getStations())
                .containsExactly(강남, 선릉);

    }

    @DisplayName("상행역 종점에 추가")
    @Test
    void removeStation() {
        Section newSection = new Section(line, 교대, 강남, 4);
        line.addSection(newSection);

        assertThat(line.getStations())
                .containsExactly(교대, 강남, 선릉);
    }

    @DisplayName("일치하는 역 없을 경우")
    @Test
    void addSection_Exception1() {
        Section newSection = new Section(line, 교대, 역삼, 4);
        assertThatThrownBy(() -> line.addSection(newSection))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("이미 등록된 구간의 경우")
    @Test
    void addSection_Exception2() {
        Section newSection = new Section(line, 강남, 선릉, 4);
        assertThatThrownBy(() -> line.addSection(newSection))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("구간이 하나인 상태에서 역 삭제")
    @Test
    void removeSection_Exception1() {
        assertThatThrownBy(() -> line.removeLineStation(강남))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
