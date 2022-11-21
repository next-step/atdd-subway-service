package study.unit;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class SpringExtensionTest {
    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private StationRepository stationRepository;

    @Test
    void findAllLines() {
        // given
        when(lineRepository.findAll())
                .thenReturn(Lists.newArrayList(createLine("신분당선", "bg-red", createStation("강남역"), createStation("양재역"), 10)));
        LineService lineService = new LineService(lineRepository, stationRepository);

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}
