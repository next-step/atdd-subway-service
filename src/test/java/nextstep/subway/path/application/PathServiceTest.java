package nextstep.subway.path.application;


import static nextstep.subway.path.domain.PathFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infrastructure.JgraphtPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로조회 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        // given
        PathFactory pathFactory = new JgraphtPath();
        pathService = new PathService(lineRepository, pathFactory);
        when(lineRepository.findAll()).thenReturn(전체구간());
    }

    @Test
    @DisplayName("`강남 - 남부터미널` 구간 경로조회")
    void 경로조회1() {
        // when
        PathResponse pathResponse = pathService.getShortestPath(강남.getId(), 남부터미널.getId());

        // then
        경로_순서대로_조회됨(pathResponse, Arrays.asList(강남.getName(), 교대.getName(), 남부터미널.getName()));
        경로_거리_조회됨(pathResponse, 12);
    }


    @Test
    @DisplayName("`교대 - 양재` 구간 경로조회")
    void 경로조회2() {
        // when
        PathResponse pathResponse = pathService.getShortestPath(교대.getId(), 양재.getId());

        // then
        경로_순서대로_조회됨(pathResponse, Arrays.asList(교대.getName(), 남부터미널.getName(), 양재.getName()));
        경로_거리_조회됨(pathResponse, 5);
    }

    private void 경로_순서대로_조회됨(PathResponse pathResponse, List<String> expected) {
        assertThat(pathResponse.getStations()).extracting("name")
            .containsExactlyElementsOf(expected);
    }

    private void 경로_거리_조회됨(PathResponse pathResponse, int distance) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }
}
