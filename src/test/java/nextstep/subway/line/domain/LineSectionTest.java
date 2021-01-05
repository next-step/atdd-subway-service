package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class LineSectionTest {

	private Line line;
	private Map<String, Station> stationMap;

	@BeforeEach
	void setUp() {
		Station 강남역 = new Station("강남역");
		Station 역삼역 = new Station("역삼역");
		Station 교대역 = new Station("교대역");
		Station 선릉역 = new Station("선릉역");
		Station 잠실역 = new Station("잠실역");
		Station 강변역 = new Station("강변역");
		ReflectionTestUtils.setField(강남역, "id", 1L);
		ReflectionTestUtils.setField(역삼역, "id", 2L);
		ReflectionTestUtils.setField(교대역, "id", 3L);
		ReflectionTestUtils.setField(선릉역, "id", 4L);
		ReflectionTestUtils.setField(잠실역, "id", 5L);
		ReflectionTestUtils.setField(강변역, "id", 6L);
		stationMap = new HashMap<>();
		stationMap.put("강남역", 강남역);
		stationMap.put("역삼역", 역삼역);
		stationMap.put("교대역", 교대역);
		stationMap.put("선릉역", 선릉역);
		stationMap.put("잠실역", 잠실역);
		stationMap.put("강변역", 강변역);

		line = new Line("2호선", "green", 강남역, 역삼역, 10);
	}

	@DisplayName("최초 구간정보 등록")
	@Test
	void addLineSectionEmptySections() {
		//given
		Line line = new Line("2호선", "green");
		LineSection lineSection = new LineSection(line, stationMap.get("강남역"),
			  stationMap.get("역삼역"));

		//when
		lineSection.addLineStation(5);

		//then
		assertThat(line.getSections()).hasSize(1);
	}

	@DisplayName("상행종점 추가")
	@Test
	void addLineSectionFinalUpStation() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("교대역"),
			  stationMap.get("강남역"));

		//when
		lineSection.addLineStation(5);

		//then
		assertThat(line.getStations())
			  .containsExactly(stationMap.get("교대역"), stationMap.get("강남역"), stationMap.get("역삼역"));
	}

	@DisplayName("하행종점 추가")
	@Test
	void addLineSectionFinalDownStation() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("역삼역"),
			  stationMap.get("선릉역"));

		//when
		lineSection.addLineStation(5);

		//then
		assertThat(line.getStations())
			  .containsExactly(stationMap.get("강남역"), stationMap.get("역삼역"), stationMap.get("선릉역"));
	}

	@DisplayName("중간역 추가")
	@Test
	void addLineSectionMiddleStation() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("강남역"),
			  stationMap.get("선릉역"));

		//when
		lineSection.addLineStation(4);

		//then
		assertThat(line.getStations())
			  .containsExactly(stationMap.get("강남역"), stationMap.get("선릉역"), stationMap.get("역삼역"));
	}

	@DisplayName("역간 거리가 기존 구간보다 긴 경우 추가할 수 없다.")
	@Test
	void addLineSectionMiddleStationTooLongDistance() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("강남역"),
			  stationMap.get("선릉역"));

		//when, then
		assertThatThrownBy(() ->lineSection.addLineStation(10))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
	}

	@DisplayName("이미 등록된 구간은 추가할 수 없다.")
	@Test
	void addLineSectionAlreadyIncludeStations() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("강남역"),
			  stationMap.get("역삼역"));

		//when
		assertThatThrownBy(() -> lineSection.addLineStation(1))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("이미 등록된 구간 입니다.");
	}

	@DisplayName("연결된 역을 찾을 수 없는 경우 구간에 추가할 수 없다.")
	@Test
	void addLineSectionNotIncludeStations() {
		//given
		LineSection lineSection = new LineSection(line, stationMap.get("교대역"),
			  stationMap.get("선릉역"));

		//when
		assertThatThrownBy(() -> lineSection.addLineStation(1))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("등록할 수 없는 구간 입니다.");
	}
}
