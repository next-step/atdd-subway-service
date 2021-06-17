package study.unit;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("단위 테스트 - mockito를 활용한 가짜 협력 객체 사용")
public class MockitoTest {
    @Test
    void findAllLines() {
        // given
        LineRepository lineRepository = mock(LineRepository.class);

        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line("신분당선", "빨간색")));
        LineQueryService lineQueryService = new LineQueryService(lineRepository);

        // when
        LineResponses responses = lineQueryService.findLines();

        // then
        assertThat(responses.size()).isEqualTo(1);
    }
}
