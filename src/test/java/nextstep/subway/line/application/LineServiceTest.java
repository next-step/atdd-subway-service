package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LineServiceTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    private Station 강남역;
    private Station 역삼역;
    private Station 교대역;
    private Station 양재역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        교대역 = stationRepository.save(new Station("교대역"));
        양재역 = stationRepository.save(new Station("양재역"));
        이호선 = new Line("2호선", "green", 0, 강남역, 역삼역, 10);
        lineRepository.save(이호선);
    }

    @Test
    void saveLine() {
        LineRequest request = new LineRequest("3호선", "orange", 0, 교대역.getId(), 양재역.getId(), 10);

        LineResponse lineResponse = lineService.saveLine(request);

        assertAll(() -> assertThat(lineResponse).isNotNull(),
                () -> assertThat(lineResponse.getName()).isEqualTo("3호선"));
    }

    @Test
    void findLines() {
        Line 삼호선 = new Line("3호선", "orange", 0, 교대역, 양재역, 10);
        lineRepository.saveAll(Arrays.asList(이호선, 삼호선));

        List<LineResponse> lines = lineService.findLines();

        assertAll(() -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.get(0).getName()).isEqualTo("2호선"),
                () -> assertThat(lines.get(1).getName()).isEqualTo("3호선"));
    }

    @Test
    void findLineById() {
        Line responseLine = lineService.findLineById(이호선.getId());

        assertAll(() -> assertThat(responseLine).isNotNull(),
                () -> assertThat(responseLine.getColor()).isEqualTo("green"));
    }

    @Test
    void findLineResponseById() {
        LineResponse lineResponseById = lineService.findLineResponseById(이호선.getId());

        assertAll(() -> assertThat(lineResponseById).isNotNull(),
                () -> assertThat(lineResponseById.getColor()).isEqualTo("green"));
    }

    @Test
    void updateLine() {
        LineRequest updateRequest = new LineRequest("3호선", "orange", 0,
                강남역.getId(), 역삼역.getId(), 15);

        lineService.updateLine(이호선.getId(), updateRequest);

        Line updatedLine = lineService.findLineById(이호선.getId());
        assertAll(() -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName()),
                () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor()));
    }

    @Test
    void deleteLineById() {
        lineService.deleteLineById(이호선.getId());

        assertThat(lineService.findLines()).hasSize(0);
    }

    @Test
    void addLineStation() {
        SectionRequest sectionRequest = new SectionRequest(역삼역.getId(), 양재역.getId(), 12);

        lineService.addLineStation(이호선.getId(), sectionRequest);

        Line addedSectionLine = lineService.findLineById(이호선.getId());
        assertThat(addedSectionLine.getSections()).hasSize(2);
    }

    @Test
    void removeLineStation() {
        SectionRequest sectionRequest = new SectionRequest(역삼역.getId(), 양재역.getId(), 12);
        lineService.addLineStation(이호선.getId(), sectionRequest);

        lineService.removeLineStation(이호선.getId(), 강남역.getId());

        Line removedSectionLine = lineService.findLineById(이호선.getId());
        assertThat(removedSectionLine.getSections()).hasSize(1);
    }
}
