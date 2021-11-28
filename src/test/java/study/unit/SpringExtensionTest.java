package study.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.List;
import nextstep.subway.common.domain.Name;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
class SpringExtensionTest {

    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private StationService stationService;

    @Test
    @DisplayName("모든 노선 찾기")
    void findAllLines() {
        // given
        when(lineRepository.findAllLines()).thenReturn(Lines.from(Lists.newArrayList(신분당선())));
        LineService lineService = new LineService(lineRepository, stationService);

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }

    private Line 신분당선() {
        return Line.of(
            Name.from("신분당선"), Color.from("bg-red-600"),
            Sections.from(Section.of(
                Station.from(Name.from("강남역")),
                Station.from(Name.from("광교역")),
                Distance.from(10))
            )
        );
    }
}
