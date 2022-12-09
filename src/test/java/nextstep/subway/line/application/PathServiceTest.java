package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathGraph;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PathServiceTest{

    @Mock
    StationRepository stationRepository;
    @Mock
    SectionRepository sectionRepository;
    @Autowired
    PathGraph pathGraph;

    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Line line;

    @BeforeEach
    void beforeEach(){
        station1 = new Station(10L,"영등포구청역");
        station2 = new Station(20L,"여의도역");
        station3 = new Station(30L,"마포역");
        station4 = new Station(40L,"공덕역");
        line = new Line("5호선", "red", station1, station2, 2, 500);

        when(stationRepository.findById(10L)).thenReturn(Optional.of(station1));
        when(stationRepository.findById(40L)).thenReturn(Optional.of(station4));
        when(sectionRepository.findAll()).thenReturn(Lists.newArrayList(
                new Section(line, station1, station2, 2),
                new Section(line, station2, station3, 5),
                new Section(line, station3, station4, 5)
        ));
    }

    @Test
    @DisplayName("최단 경로 탐색")
    void path(){
        // given

        // when
        PathService pathService = new PathService(stationRepository, sectionRepository, pathGraph);
        PathResponse path = pathService.path(station1.getId(), station4.getId(), null);

        // then
        List<String> stationNames = path.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(path.getDistance()).isEqualTo(12);
        assertThat(path.getCharge()).isEqualTo(1850);
        assertThat(stationNames).containsExactly(
                "영등포구청역","여의도역","마포역","공덕역"
        );
    }

    @Test
    @DisplayName("청소년 최단 경로 탐색")
    void pathWithTeenAge(){
        // given
        LoginMember loginMember = new LoginMember(999L, "byunsw4@naver.com", 15);

        // when
        PathService pathService = new PathService(stationRepository, sectionRepository, pathGraph);
        PathResponse path = pathService.path(station1.getId(), station4.getId(), loginMember);

        // then
        List<String> stationNames = path.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(path.getDistance()).isEqualTo(12);
        assertThat(path.getCharge()).isEqualTo(1200);
        assertThat(stationNames).containsExactly(
                "영등포구청역","여의도역","마포역","공덕역"
        );
    }
}
