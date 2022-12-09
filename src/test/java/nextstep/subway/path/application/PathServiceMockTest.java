package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.AGE;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.EMAIL;
import static nextstep.subway.path.domain.PathFinder.SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceMockTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Line lineA;
    private Line lineB;
    private Line lineC;
    private Line lineD;
    private Line lineE;

    private Station stationA;
    private Station stationB;
    private Station stationC;
    private Station stationD;
    private Station stationE;
    private Station stationF;

    /**
     * stationA  --- lineD    --- stationD                      stationF
     * /             거리 1        /                              /
     * *lineA* 거리 5                lineC 거리 1               *lineE* 거리 2
     * /                             /                            /
     * stationB  ---   lineB ---  stationC                      stationE
     * 거리 3
     */
    @BeforeEach
    void setUp() {
        stationA = new Station("A");
        stationB = new Station("B");
        stationC = new Station("C");
        stationD = new Station("D");
        stationE = new Station("E");
        stationF = new Station("F");

        lineA = Line.of("A", "RED", 0);
        lineB = Line.of("B", "BLUE", 0);
        lineC = Line.of("C", "GREEN", 0);
        lineD = Line.of("D", "YELLOW", 0);
        lineE = Line.of("E", "ORANGE", 0);

        lineA.addSection(new Section(lineA, stationA, stationB, new Distance(5)));
        lineB.addSection(new Section(lineB, stationB, stationC, new Distance(3)));
        lineC.addSection(new Section(lineC, stationC, stationD, new Distance(1)));
        lineD.addSection(new Section(lineD, stationD, stationA, new Distance(1)));
        lineE.addSection(new Section(lineE, stationE, stationF, new Distance(2)));
    }

    @DisplayName("최단 경로를 조회")
    @Test
    void findPath() {

        LoginMember member = new LoginMember(1L, EMAIL, AGE);

        Long sourceId = 1L;
        Long targetId = 2L;

        when(stationRepository.findById(sourceId)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(targetId)).thenReturn(Optional.of(stationC));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        PathResponse pathResponse = pathService.findPath(member, sourceId, targetId);

        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(2);
    }

    @DisplayName("출발역과 도착역이 같은 경우 조회할 수 없다.")
    @Test
    void findPath_fail_sameStation() {

        LoginMember member = new LoginMember(1L, EMAIL, AGE);

        Long sourceId = 1L;
        Long targetId = 2L;

        when(stationRepository.findById(sourceId)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(targetId)).thenReturn(Optional.of(stationA));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertThatThrownBy(() -> pathService.findPath(member, sourceId, targetId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 조회할 수 없다.")
    @Test
    void findPath_fail_notConnect() {

        LoginMember member = new LoginMember(1L, EMAIL, AGE);

        Long sourceId = 1L;
        Long targetId = 2L;

        when(stationRepository.findById(sourceId)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(targetId)).thenReturn(Optional.of(stationF));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertThatThrownBy(() -> pathService.findPath(member, sourceId, targetId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 조회할 수 없다.")
    @Test
    void findPath_fail_notExist() {

        LoginMember member = new LoginMember(1L, EMAIL, AGE);

        Long sourceId = 1L;
        Long targetId = 2L;

        when(stationRepository.findById(sourceId)).thenThrow(EntityNotFoundException.class);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertThatThrownBy(() -> pathService.findPath(member, sourceId, targetId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
