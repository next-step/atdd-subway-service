package nextstep.subway.path.acceptance.application;


import static nextstep.subway.path.acceptance.domain.PathFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.application.PathFactory;
import nextstep.subway.path.application.PathService;
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
    void getShortestPath() {
        // when
        PathResponse pathResponse = pathService.getShortestPath(강남.getId(), 남부터미널.getId());

        // then
        assertThat(pathResponse.getStations()).extracting("name")
            .containsExactly(강남.getName(), 교대.getName(), 남부터미널.getName());
        assertThat(pathResponse.getWeight()).isEqualTo(12);
    }

    @Test
    @DisplayName("`교대 - 양재` 구간 경로조회")
    void getShortestPath2() {
        // when
        PathResponse pathResponse = pathService.getShortestPath(교대.getId(), 양재.getId());

        // then
        assertThat(pathResponse.getStations()).extracting("name")
            .containsExactly(교대.getName(), 남부터미널.getName(), 양재.getName());
        assertThat(pathResponse.getWeight()).isEqualTo(5);
    }
}
