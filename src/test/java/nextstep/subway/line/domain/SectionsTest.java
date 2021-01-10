package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("구간 관련 기능")
public class SectionsTest {

    @DisplayName("구간에 포함 된 역 목록 조회")
    @Test
    void getStations() {
        Line line = new Line("2호선", "bg-green-600");
        Station upStation = new Station("신도림역");
        Station middleStation = new Station("신정네거리역");
        Station downStation = new Station("까치산역");

        Sections sections = new Sections(Arrays.asList(new Section(line, upStation, middleStation, 3), new Section(line, middleStation, downStation, 3)) );
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(upStation, middleStation, downStation);
    }

    @DisplayName("이미 등록 된 구간을 등록")
    @Test
    void addSectionDuplicate() {
        Line line = new Line("2호선", "bg-green-600");
        Section section = new Section(line, new Station("신도림역"), new Station("까치산역"), 10);

        Sections sections = new Sections(new ArrayList<>(Collections.singletonList(section)));
        assertThatExceptionOfType(InvalidSectionException.class)
                .isThrownBy(() -> sections.add(section))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("연결되는 역이 없는 구간 등록")
    @Test
    void addSectionWithoutRelation() {
        Line line = new Line("2호선", "bg-green-600");
        Section section = new Section(line, new Station("신도림역"), new Station("까치산역"), 10);
        Section otherSection = new Section(line, new Station("양천구청역"), new Station("신정네거리역"), 5);

        Sections sections = new Sections(new ArrayList<>(Collections.singletonList(section)));
        assertThatExceptionOfType(InvalidSectionException.class)
                .isThrownBy(() -> sections.add(otherSection))
                .withMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간이 하나도 없을 떄 등록")
    @Test
    void addSectionWhenEmpty() {
        Line line = new Line("2호선", "bg-green-600");
        Station upStation = new Station("신도림역");
        Station downStation = new Station("까치산역");
        Section section = new Section(line, upStation, downStation, 10);

        Sections sections = new Sections();
        sections.add(section);
        assertThat(sections.getStations()).containsExactly(upStation, downStation);
    }

    @DisplayName("구간 등록")
    @Test
    void addSection() {
        Line line = new Line("2호선", "bg-green-600");

        Station sindorim = new Station("신도림역");
        Station dorimcheon = new Station("도림천역");
        Station yangcheon = new Station("양천구청역");
        Station sinjeong = new Station("신정네거리역");
        Station kkachisan = new Station("까치산역");

        Section section1 = new Section(line, dorimcheon, sinjeong, 10);
        Section section2 = new Section(line, dorimcheon, yangcheon, 5);
        Section section3 = new Section(line, sindorim, dorimcheon, 5);
        Section section4 = new Section(line, sinjeong, kkachisan, 5);

        Sections sections = new Sections();
        sections.addAll(section1, section2, section3, section4);

        assertThat(sections.getStations()).containsExactly(sindorim, dorimcheon, yangcheon, sinjeong, kkachisan);
    }

    @DisplayName("구간에서 역 삭제")
    @Test
    void removeStation() {
        Line line = new Line("2호선", "bg-green-600");

        Station sindorim = new Station("신도림역");
        Station dorimcheon = new Station("도림천역");
        Station yangcheon = new Station("양천구청역");
        Station sinjeong = new Station("신정네거리역");
        Station kkachisan = new Station("까치산역");

        Section section1 = new Section(line, dorimcheon, sinjeong, 10);
        Section section2 = new Section(line, dorimcheon, yangcheon, 5);
        Section section3 = new Section(line, sindorim, dorimcheon, 5);
        Section section4 = new Section(line, sinjeong, kkachisan, 5);

        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section1, section2, section3, section4)));
        sections.removeAllStation(sindorim, kkachisan,yangcheon);

        assertThat(sections.getStations()).containsExactly(dorimcheon, sinjeong);
    }
}
