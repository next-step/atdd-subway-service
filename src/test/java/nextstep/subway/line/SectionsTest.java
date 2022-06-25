package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    Station 강남역 = new Station("강남역");
    Station 양재역 = new Station("양재역");
    Station 판교역 = new Station("판교역");
    Line line = new Line("2호선", "green");
    Section section1, section2;
    Sections sections;


    @BeforeEach
    void setUp() {
        section1 = new Section(line, 강남역, 양재역, 2);
        section2 = new Section(line, 양재역, 판교역, 2);
        sections = new Sections(new ArrayList<>(Arrays.asList(section1, section2)));
    }

    @DisplayName("새로운 구간을 등록한다")
    @Test
    void addStation() {
        //given
        Station 서울역 = new Station("서울역");
        Section section = new Section(line, 서울역, 강남역, 2);
        //when
        sections.addStations(section);
        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
    }

    @DisplayName("이미 등록된 구간은 등록에 실패한다")
    @Test
    void addExistStation() {
        //given
        Section section = new Section(line, 강남역, 양재역, 2);
        //when, then
        assertThatThrownBy(() -> sections.addStations(section))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("기존 구간들과 연관되지 않을시 등록에 실패한다")
    @Test
    void addIrrelevantStation() {
        //given
        Section section = new Section(line, new Station("서울역"), new Station("광교역"), 2);
        //when, then
        assertThatThrownBy(() -> sections.addStations(section))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간에 역을 제거한다")
    @Test
    void removeStation() {
        //when
        sections.removeStation(판교역);
        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
        assertThat(sections.getStations().size()).isEqualTo(2);
    }

    @DisplayName("구간이 1개 이하이면 구간 제거를 실패한다")
    @Test
    void removeStations() {
        //when
        sections.removeStation(강남역);
        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
        assertThatThrownBy(() -> sections.removeStation(판교역))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("중간역을 제거하면 이전 하행역과 이후 상행역이 합쳐진다")
    @Test
    void removeMiddleStation() {
        //when
        sections.removeStation(양재역);
        //then
        assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역);
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(판교역);
    }

    @DisplayName("구간의 모든 역을 조회한다")
    @Test
    void getStations() {
        List<Station> stations = sections.getStations();
        assertThat(stations.size()).isEqualTo(3);
    }
}
