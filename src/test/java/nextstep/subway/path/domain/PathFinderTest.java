package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NotConnectedLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/16
 * @description :
 **/
class PathFinderTest {

	Station 강남역 = new Station(1L, "강남역");
	Station 양재역 = new Station(2L, "양재역");
	Station 교대역 = new Station(3L, "교대역");
	Line 신분당선 = new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 0);

	@DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
	@Test
	void sourceStationAndTargetStationNotConnectedException() {

		LoginMember loginMember = new LoginMember();
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선), 강남역, 교대역, loginMember);
		assertThatThrownBy(() -> {
			pathFinder.getDijkstraShortestPath();
		}).isInstanceOf(NotConnectedLineException.class);
	}

}