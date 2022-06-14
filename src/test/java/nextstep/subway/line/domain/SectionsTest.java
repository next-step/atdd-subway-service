package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Station upStation;
    private Station downStation;
    Line line;
    private Sections sections;


    @BeforeEach
    public void setUp() {
        upStation = new Station("상행 종점");
        downStation = new Station("하행 종점");
        line = new Line("2호선", "color", upStation, downStation, Distance.from(5));
        sections = new Sections();
        sections.add(new Section(line, upStation, downStation, Distance.from(10)));
    }

    @Test
    void 구간_등록() {
        // when
        Station newStation = new Station("역");
        Section input = new Section(line, upStation, newStation, Distance.from(2));
        sections.add(input);

        // then
        assertThat(sections.getStations()).contains(newStation);
    }

    @Test
    void 구간_등록_중복_예외() {
        // then
        assertThatThrownBy(
                () -> sections.add(new Section(line, upStation, downStation, Distance.from(2)))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 잘못된_구간_등록_예외() {
        // when
        Station notExistedStation1 = new Station("구간역1");
        Station notExistedStation2 = new Station("구간역2");

        // then
        assertThatThrownBy(
                () -> sections.add(new Section(line, notExistedStation1, notExistedStation2, Distance.from(2)))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 역_제거() {
        // given
        Station removedStation = new Station("역");
        Section input = new Section(line, upStation, removedStation, Distance.from(2));
        sections.add(input);

        // when
        sections.removeStation(removedStation);

        // then
        assertThat(sections.getStations()).doesNotContain(removedStation);
    }

    @Test
    void 역_제거_예외() {
        // then
        assertThatThrownBy(
                () -> sections.removeStation(upStation)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 구간_역_목록() {
        assertThat(sections.getStations()).containsExactly(upStation, downStation);
    }
}