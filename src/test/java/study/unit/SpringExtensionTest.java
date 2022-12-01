package study.unit;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
class SpringExtensionTest {
    @MockBean
    private LineRepository lineRepository;

    @Test
    void findAllLines() {
        // given
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
        LineQueryService lineService = new LineQueryService(lineRepository);

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}
