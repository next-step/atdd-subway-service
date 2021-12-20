package nextstep.subway.fare.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.shortest.ShortestPath;

class FareServiceTest {

	private final FareService fareService = new FareService();

	@DisplayName("(연령구간:어린이, 노선운임료:100) - 거리비례구간별 운임료")
	@Test
	void calculate_kid_100_line_fare_by_distances() {
		final LoginMember 어린이 = mockLoginMember(12);
		final List<Line> 노선운임료100원 = Arrays.asList(mockLine(0), mockLine(100));

		final double 거리10km이하 = 10;
		assertThat(fareService.calculate(
			어린이, mockShortestPath(노선운임료100원, 거리10km이하)).getFare()
		).isEqualTo(500); // ((1250 + 100 + 0) - 350) * 0.5

		final double 거리10km초과50km이하 = 24;
		assertThat(fareService.calculate(
			어린이, mockShortestPath(노선운임료100원, 거리10km초과50km이하)).getFare()
		).isEqualTo(650); // ((1250 + 100 + 300) - 350) * 0.5

		final double 거리50km이상 = 59;
		assertThat(fareService.calculate(
			어린이, mockShortestPath(노선운임료100원, 거리50km이상)).getFare()
		).isEqualTo(1000); // ((1250 + 100 + (800+200)) - 350) * 0.5
	}

	@DisplayName("(연령구간:청소년, 노선운임료:100) - 거리비례구간별 운임료")
	@Test
	void calculate_teenager_100_line_fare_by_distances() {
		final LoginMember 청소년 = mockLoginMember(18);
		final List<Line> 노선운임료100원 = Arrays.asList(mockLine(0), mockLine(100));

		final double 거리10km이하 = 10;
		assertThat(fareService.calculate(
			청소년, mockShortestPath(노선운임료100원, 거리10km이하)).getFare()
		).isEqualTo(800); // ((1250 + 100 + 0) - 350) * 0.8

		final double 거리10km초과50km이하 = 24;
		assertThat(fareService.calculate(
			청소년, mockShortestPath(노선운임료100원, 거리10km초과50km이하)).getFare()
		).isEqualTo(1040); // ((1250 + 100 + 300) - 350) * 0.8

		final double 거리50km이상 = 59;
		assertThat(fareService.calculate(
			청소년, mockShortestPath(노선운임료100원, 거리50km이상)).getFare()
		).isEqualTo(1600); // ((1250 + 100 + (800+200)) - 350) * 0.8
	}

	@DisplayName("(연령구간:일반, 노선운임료:100) - 거리비례구간별 운임료")
	@Test
	void calculate_general_100_line_fare_by_distances() {
		final LoginMember 일반 = mockLoginMember(19);
		final List<Line> 노선운임료100원 = Arrays.asList(mockLine(0), mockLine(100));

		final double 거리10km이하 = 10;
		assertThat(fareService.calculate(
			일반, mockShortestPath(노선운임료100원, 거리10km이하)).getFare()
		).isEqualTo(1350); // 1250 + 100 + 0

		final double 거리10km초과50km이하 = 24;
		assertThat(fareService.calculate(
			일반, mockShortestPath(노선운임료100원, 거리10km초과50km이하)).getFare()
		).isEqualTo(1650); // 1250 + 100 + 300

		final double 거리50km이상 = 59;
		assertThat(fareService.calculate(
			일반, mockShortestPath(노선운임료100원, 거리50km이상)).getFare()
		).isEqualTo(2350); // 1250 + 100 + (800+200)
	}

	private LoginMember mockLoginMember(int age) {
		final LoginMember member = mock(LoginMember.class);
		given(member.getAge()).willReturn(age);
		return member;
	}

	private Line mockLine(int fare) {
		final Line line = mock(Line.class);
		given(line.getFare()).willReturn(fare);
		return line;
	}

	private ShortestPath mockShortestPath(List<Line> lines, double distance) {
		final ShortestPath shortestPath = mock(ShortestPath.class);
		given(shortestPath.getLines()).willReturn(lines);
		given(shortestPath.getDistance()).willReturn(distance);
		return shortestPath;
	}
}
