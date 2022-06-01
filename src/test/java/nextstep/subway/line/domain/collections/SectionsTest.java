package nextstep.subway.line.domain.collections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("노선의 구간 관련 단위테스트")
@DataJpaTest
class SectionsTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private final Station 강남 = new Station("강남");
    private final Station 판교 = new Station("판교");
    private final Station 정자 = new Station("정자");
    private final Station 양재 = new Station("양재");
    private final Station 신논현 = new Station("신논현");
    private final Station 광교 = new Station("광교");
    private final Line 신분당선 = new Line("신분당선", "bg-redd-100", 판교, 정자, 10);

    @BeforeEach
    void setUp() {
        stationRepository.save(강남);
        Section section = new Section(신분당선, 강남, 판교, 10);
        신분당선.addSection(section);
        lineRepository.save(신분당선);
    }

    @DisplayName("노선이 가진 구간을 기준으로 정렬된 지하철역 리스트를 반환한다.")
    @Test
    void findStations(){
        //given
        Sections sections = 신분당선.getSections();

        //when
        List<Station> stations= sections.getStations();

        //then
        assertThat(stations).containsExactly(강남,판교,정자);
    }

    @DisplayName("요청 노선의 최상단 출발 지하철역을 찾는다.")
    @Test
    void findDepartStation() {
        //given
        Sections sections = 신분당선.getSections();

        //when
        Station departStation = sections.findDepartStation();

        //then
        assertThat(departStation).isEqualTo(강남);
    }

    @DisplayName("노선에 대한 신규 구간을 추가한다.(기존 구간은 업데이트)")
    @Test
    void addNewSection(){
        //given
        stationRepository.saveAll(Arrays.asList(양재, 신논현, 광교));
        Sections sections = 신분당선.getSections();

        //when
        sections.addNewSection(신분당선, 강남, 양재, 3);
        sections.addNewSection(신분당선, 신논현, 강남, 10);
        sections.addNewSection(신분당선, 정자, 광교, 20);
        List<Station> stations = sections.getStations();

        //then
        assertThat(stations).containsExactly(신논현, 강남, 양재, 판교, 정자, 광교);
    }

    @DisplayName("노선에 대한 구간을 삭제한다.(기존 구간은 업데이트)")
    @Test
    void removeSection(){
        //given
        stationRepository.saveAll(Arrays.asList(양재, 신논현, 광교));
        Sections sections = 신분당선.getSections();
        sections.addNewSection(신분당선, 강남, 양재, 3);
        sections.addNewSection(신분당선, 신논현, 강남, 10);
        sections.addNewSection(신분당선, 정자, 광교, 20);

        //when
        sections.removeSection(신분당선, 강남);
        sections.removeSection(신분당선, 신논현);
        sections.removeSection(신분당선, 광교);
        List<Station> stations = sections.getStations();

        //then
        assertThat(stations).containsExactly(양재, 판교, 정자);
    }
}
