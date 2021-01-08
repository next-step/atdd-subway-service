package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
public class LineSectionIntegrationTest {
    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    @AfterEach
    public void cleanup() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Station 강남역 = saveStation("강남역");
        Station 판교역 = saveStation("판교역");
        Station 정자역 = saveStation("정자역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));

        // when
        addSection(신분당선, 강남역, 양재역, 5);
        addSection(신분당선, 양재역, 판교역, 2);
        addSection(신분당선, 정자역, 광교역, 3);

        // then
        assertThat(getStationsByLine(신분당선))
                .extracting("name")
                .containsExactly("강남역", "양재역", "판교역", "정자역", "광교역");
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addSectionExistStationException() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Station 강남역 = saveStation("강남역");
        Station 판교역 = saveStation("판교역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));

        // when then
        assertThatThrownBy(() -> addSection(신분당선, 강남역, 판교역, 5))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addSectionExistSectionException() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));

        // when then
        assertThatThrownBy(() -> addSection(신분당선, 양재역, 광교역, 5))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));

        // when
        lineService.updateLine(신분당선.getId(), new LineRequest("3호선", "green lighten-1"));

        // then
        assertThat(lineService.findLineResponseById(신분당선.getId()).getName()).isEqualTo("3호선");
    }

    @DisplayName("구간 역을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Station 강남역 = saveStation("강남역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));
        addSection(신분당선, 강남역, 양재역, 5);

        // when
        lineService.removeLineStation(신분당선.getId(), 양재역.getId());

        // then
        assertThat(getStationsByLine(신분당선))
                .extracting("name")
                .containsExactly("강남역", "광교역");
    }

    @DisplayName("구간이 하나일때 삭제한다.")
    @Test
    void failRemoveStation() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));

        // when
        assertThatThrownBy(() -> lineService.removeLineStation(신분당선.getId(), 양재역.getId())).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("구간 삭제 실패됨");
    }

    @DisplayName("지하철역 아이디로 해당 노선에 포함되는지 확인하고 노선의 추가 요금을 가져온다.")
    @Test
    void containsSection() {
        // given
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10, 800));

        // when then
        assertThat(신분당선.hasSection(양재역.getId(), 광교역.getId())).isTrue();
    }

    private List<StationResponse> getStationsByLine(Line line) {
        return lineService.findLineResponseById(line.getId()).getStations();
    }

    private void addSection(Line line, Station upStation, Station downStation, int distance) {
        lineService.addLineStation(line.getId(), new SectionRequest(upStation.getId(), downStation.getId(), distance));
    }

    private Station saveStation(String name) {
        return stationRepository.save(new Station(name));
    }


}
