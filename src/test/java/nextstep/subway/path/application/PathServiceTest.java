package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(SpringExtension.class)
class PathServiceTest {

	@MockBean
	private LineRepository lineRepository;
	@MockBean
	private StationService stationService;
	@MockBean
	private MemberService memberService;

	@DisplayName("최단 경로 찾기 테스트")
	@Test
	void testFindShortestPath() {
		Station sourceStation = new Station("source");
		Station targetStation = new Station("target");
		Line line = new Line("name", "color", sourceStation, targetStation, 10);
		List<Line> lines = new ArrayList<>();
		lines.add(line);

		Member member = new Member("email@email.com", "123", 30);

		Mockito.when(this.lineRepository.findAll()).thenReturn(lines);
		Mockito.when(memberService.findById(Mockito.eq(1L))).thenReturn(member);
		Mockito.when(stationService.findById(Mockito.eq(1L))).thenReturn(sourceStation);
		Mockito.when(stationService.findById(Mockito.eq(2L))).thenReturn(targetStation);

		PathService pathService = new PathService(stationService, lineRepository, memberService);
		PathResponse path = pathService.findShortestPath(1L, 1L, 2L);
		Assertions.assertThat(path.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
			.contains("source", "target");
		Assertions.assertThat(path.getDistance()).isEqualTo(10);
	}
}
