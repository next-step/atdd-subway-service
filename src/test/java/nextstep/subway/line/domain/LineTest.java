package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 라인 도메인 테스트")
@DataJpaTest
class LineTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        기초_지하철역_등록();
        기초_라인_생성();
    }

    private void 기초_지하철역_등록() {
        List<String> stationNames = Arrays.asList("강남", "양재", "양재시민의숲", "판교");
        List<Station> stations = stationNames.stream()
                .map(Station::new)
                .collect(Collectors.toList());
        stations.stream()
                .forEach(stationRepository::save);
    }

    private void 기초_라인_생성() {
        Line line = new Line("신분당선", "bg-red-600");

        기초_구간_등록(line);

        lineRepository.save(line);
    }

    private void 기초_구간_등록(Line line) {
        Station 강남 = stationRepository.findByName("강남");
        Station 양재 = stationRepository.findByName("양재");
        Station 양재시민의숲 = stationRepository.findByName("양재시민의숲");

        line.addSection(new Section(양재, 양재시민의숲, 10));
        line.addSection(new Section(강남, 양재, 10));
    }

    @Test
    @DisplayName("지하철 구간역들 가져오기")
    public void getStationsTest() {
        Line line = lineRepository.getByName("신분당선");

        assertThat(line.getStations().stream().map(it -> it.getName()).collect(Collectors.toList())).containsExactly("강남", "양재", "양재시민의숲");
    }

    @Test
    @DisplayName("지하철 구간역추가 ( 실패 : 등록된 구간역 추가 )")
    public void addSectionTest1() {
        Line line = lineRepository.getByName("신분당선");

        Station 강남 = new Station("강남");
        Station 양재 = new Station("양재");

        assertThatThrownBy(() -> line.addSection(new Section(line, 강남, 양재, 10)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("지하철 구간역추가 ( 실패 : 구간에 존재하지 않는 구간역 추가 )")
    public void addSectionTest2() {
        Line line = lineRepository.getByName("신분당선");

        Station 판교 = new Station("판교");
        Station 신논현 = new Station("신논현");

        assertThatThrownBy(() -> line.addSection(new Section(line, 판교, 신논현, 10)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("지하철 구간역추가 ( 성공 : 등록된 역이 없을때 추가 )")
    public void addSectionTest3() {
        Line line = new Line("6호선", "bg-red-600");

        Station 망원 = new Station("망원");
        Station 마포구청 = new Station("마포구청");

        line.addSection(new Section(line, 망원, 마포구청, 10));

        assertThat( line.getStations().stream().map(it -> it.getName()).collect(Collectors.toList()) ).containsExactly("망원", "마포구청");
    }

    @Test
    @DisplayName("지하철 구간역추가 ( 성공 )")
    public void addSectionTest4() {
        Line line = lineRepository.getByName("신분당선");

        Station 강남 = stationRepository.findByName("강남");
        Station 판교 = stationRepository.findByName("판교");

        line.addSection(new Section(line, 강남, 판교, 5));

        assertThat( line.getStations().stream().map(it -> it.getName()).collect(Collectors.toList()) ).containsExactly("강남", "판교", "양재", "양재시민의숲");
    }

    @Test
    @DisplayName("지하철 구간삭제 ( 실패 : 추가된 구간이 없을때 )")
    public void removeLineStationTest1() {
        Line line = new Line("6호선", "bg-red-600");

        assertThatThrownBy(() -> line.removeStation(stationRepository.findByName("강남")))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("지하철 구간삭제 ( 성공 )")
    public void removeLineStationTest2() {
        Line line = lineRepository.getByName("신분당선");
        Station 양재 = stationRepository.findByName("양재");

        line.removeStation(양재);

        assertThat( line.getStations().stream().map(it -> it.getName()).collect(Collectors.toList()) ).containsExactly("강남", "양재시민의숲");
    }
}