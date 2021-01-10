package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DisplayName("구간 관련 기능")
public class SectionsTest {
    private Line 이호선 = new Line("2호선", "bg-green-600");
    @Mock
    private Station 신도림;
    @Mock
    private Station 도림천;
    @Mock
    private Station 양천구청;
    @Mock
    private Station 신정네거리;
    @Mock
    private Station 까치산;

    public SectionsTest() {
        MockitoAnnotations.openMocks(this);
        when(신도림.getId()).thenReturn(1L);
        when(도림천.getId()).thenReturn(2L);
        when(양천구청.getId()).thenReturn(3L);
        when(신정네거리.getId()).thenReturn(4L);
        when(까치산.getId()).thenReturn(5L);
    }

    @DisplayName("구간에 포함 된 역 목록 조회")
    @Test
    void getStations() {
        Sections sections = new Sections(Arrays.asList(new Section(이호선, 신도림, 신정네거리, 3), new Section(이호선, 신정네거리, 도림천, 3)) );
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(신도림, 신정네거리, 도림천);
    }

    @DisplayName("이미 등록 된 구간을 등록")
    @Test
    void addSectionDuplicate() {
        Section section = new Section(이호선, 신도림, 까치산, 10);

        Sections sections = new Sections(new ArrayList<>(Collections.singletonList(section)));
        assertThatExceptionOfType(InvalidSectionException.class)
                .isThrownBy(() -> sections.add(section))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("연결되는 역이 없는 구간 등록")
    @Test
    void addSectionWithoutRelation() {
        Section section = new Section(이호선, 신도림, 까치산, 10);
        Section otherSection = new Section(이호선,양천구청, 신정네거리, 5);

        Sections sections = new Sections(new ArrayList<>(Collections.singletonList(section)));
        assertThatExceptionOfType(InvalidSectionException.class)
                .isThrownBy(() -> sections.add(otherSection))
                .withMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간이 하나도 없을 떄 등록")
    @Test
    void addSectionWhenEmpty() {
        Section section = new Section(이호선, 신도림, 까치산, 10);

        Sections sections = new Sections();
        sections.add(section);
        assertThat(sections.getStations()).containsExactly(신도림, 까치산);
    }

    @DisplayName("구간 등록")
    @Test
    void addSection() {
        Section section1 = new Section(이호선, 도림천, 신정네거리, 10);
        Section section2 = new Section(이호선, 도림천, 양천구청, 5);
        Section section3 = new Section(이호선, 신도림, 도림천, 5);
        Section section4 = new Section(이호선, 신정네거리, 까치산, 5);

        Sections sections = new Sections();
        sections.addAll(section1, section2, section3, section4);

        assertThat(sections.getStations()).containsExactly(신도림, 도림천, 양천구청, 신정네거리, 까치산);
    }

    @DisplayName("구간에서 역 삭제")
    @Test
    void removeStation() {
        Section section1 = new Section(이호선, 도림천, 신정네거리, 10);
        Section section2 = new Section(이호선, 도림천, 양천구청, 5);
        Section section3 = new Section(이호선, 신도림, 도림천, 5);
        Section section4 = new Section(이호선, 신정네거리, 까치산, 5);

        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section1, section2, section3, section4)));
        sections.removeAllStation(신도림, 까치산, 양천구청);

        assertThat(sections.getStations()).containsExactly(도림천, 신정네거리);
    }
}
