package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LineTest {

	private LineNew lineNew;
	private Station originEndUpStation;
	private Station originMiddleUpStation;
	private Station originMiddleDownStation;
	private Station originEndDownEndStation;

	private static final int DEFAULT_DISTANCE = 10;
	private static final int ORIGIN_SECTION_SIZE = 3;
	private static final int ORIGIN_STATION_SIZE = 4;
	private static final int ORIGIN_TOTAL_DISTANCE = DEFAULT_DISTANCE * ORIGIN_SECTION_SIZE;
	private SectionNew originSection1;
	private SectionNew originSection2;
	private SectionNew originSection3;

	Station newStation;

	@BeforeEach
	void setUp() {
		//given
		lineNew = new LineNew("2호선", "green");
		originEndUpStation = new Station("당산역");
		originMiddleUpStation = new Station("문래역");
		originMiddleDownStation = new Station("사당역");
		originEndDownEndStation = new Station("잠실역");
		originSection1 = new SectionNew(lineNew, originEndUpStation, originMiddleUpStation, DEFAULT_DISTANCE);
		originSection2 = new SectionNew(lineNew, originMiddleUpStation, originMiddleDownStation, DEFAULT_DISTANCE);
		originSection3 = new SectionNew(lineNew, originMiddleDownStation, originEndDownEndStation, DEFAULT_DISTANCE);
		lineNew.setSection(Arrays.asList(originSection1, originSection2, originSection3));
	}

	@Test
	@DisplayName("상행역 기준으로 중간에 새로운 역을 등록하는 경우, 신규 구간이 정상적으로 등록되어야한다.")
	void addSectionBasedMiddleUpStation() {
		//given
		newStation = new Station("신도림역");
		int newDistance = 1;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, newStation, originMiddleDownStation, originEndDownEndStation};

		//when
		lineNew.addSection(originMiddleUpStation, newStation, newDistance);

		//then
		List<SectionNew> sections = lineNew.getSections();
		SectionNew originDownSection = sections.stream()
			.filter(section -> section.getDownStation().equals(originMiddleDownStation))
			.findFirst().get();
		int totalDistance = sections.stream()
			.mapToInt(SectionNew::getDistance)
			.sum();
		List<Station> stations = lineNew.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//기존 하행역의 상행역이 새로운 역으로 변경되어야한다.
		assertThat(originDownSection.getUpStation()).isEqualTo(newStation);
		//기존 하행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.
		assertThat(originDownSection.getDistance()).isEqualTo(DEFAULT_DISTANCE - newDistance);
		//상행종점부터 하행종점까지 추가한 역을 포함하여 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 변하지 않아야 한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE);
	}

	@Test
	@DisplayName("하행역 기준으로 중간에 새로운 역을 등록하는 경우, 신규 구간이 정상적으로 등록되어야한다.")
	void addSectionBasedMiddleDownStation() {
		//given
		newStation = new Station("서울대입구역");
		int newDistance = 2;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, newStation, originMiddleDownStation, originEndDownEndStation};

		//when
		lineNew.addSection(newStation, originMiddleDownStation, newDistance);

		//then
		List<SectionNew> sections = lineNew.getSections();
		SectionNew originUpSection = sections.stream()
			.filter(section -> section.getUpStation().equals(originMiddleUpStation))
			.findFirst().get();
		int totalDistance = sections.stream()
			.mapToInt(SectionNew::getDistance)
			.sum();
		List<Station> stations = lineNew.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//상행역의 하행역이 새로운 역으로 변경되어야한다.
		assertThat(originUpSection.getDownStation()).isEqualTo(newStation);
		//기존 하행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.
		assertThat(originUpSection.getDistance()).isEqualTo(DEFAULT_DISTANCE - newDistance);
		//상행종점부터 하행종점까지 추가한 역을 포함하여 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 변하지 않아야 한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE);
	}

	@Test
	@DisplayName("상행종점에 새로운 역을 등록할 경우, 신규 구간이 정상적으로 등록되어야한다.")
	void addSectionEndUpStation() {
		//given
		newStation = new Station("홍대입구역");
		int newDistance = 2;
		Station[] expectedSortedStations = {newStation, originEndUpStation, originMiddleUpStation, originMiddleDownStation, originEndDownEndStation};

		//when
		lineNew.addSection(newStation, originEndUpStation, newDistance);

		//then
		List<SectionNew> sections = lineNew.getSections();
		int totalDistance = sections.stream()
			.mapToInt(SectionNew::getDistance)
			.sum();
		List<Station> stations = lineNew.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//새로운 상행종점부터 하행종점까지 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이가 신규 구간만큼 늘어나야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE + newDistance);
	}

	@Test
	@DisplayName("하행종점에 새로운 역을 등록할 경우, 신규 구간이 정상적으로 등록되어야한다.")
	void addSectionEndDownStation() {
		//given
		newStation = new Station("건대입구역");
		int newDistance = 4;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, originMiddleDownStation, originEndDownEndStation, newStation};

		//when
		lineNew.addSection(originEndDownEndStation, newStation, newDistance);

		//then
		List<SectionNew> sections = lineNew.getSections();
		int totalDistance = sections.stream()
			.mapToInt(SectionNew::getDistance)
			.sum();
		List<Station> stations = lineNew.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//상행종점부터 하행종점까지 추가한 역을 포함하여 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이가 신규 구간만큼 늘어나야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE + newDistance);
	}

	@Test
	@DisplayName("신규 구간의 거리가 기존 구간의 거리와 같은 경우, RuntimeException 을 Throw 해야한다.")
	void addSectionEqualsOriginDistance() {
		//given
		newStation = new Station("신도림역");
		int newDistance = DEFAULT_DISTANCE;

		//when/then
		assertThatThrownBy(() -> lineNew.addSection(originMiddleUpStation, newStation, newDistance))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("신규 구간의 거리가 기존 구간의 거리보다 큰 경우, RuntimeException 을 Throw 해야한다.")
	void addSectionGreaterThanOriginDistance() {
		//given
		newStation = new Station("신도림역");
		int newDistance = DEFAULT_DISTANCE + 1;

		//when/then
		assertThatThrownBy(() -> lineNew.addSection(originMiddleUpStation, newStation, newDistance))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("새로운 구간의 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 RuntimeException 을 Throw 해야한다.")
	void addSectionAlreadyExist() {
		//when/then
		assertThatThrownBy(() -> lineNew.addSection(originMiddleUpStation, originMiddleDownStation, 4))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("새로운 구간의 상행역과 하행역 둘 중 하나도 노선에 포함되어있지 않다면 RuntimeException 을 Throw 해야한다.")
	void addSectionNotExist() {
		//given
		Station newStation = new Station("홍대입구역");
		Station newStation2 = new Station("이대역");

		//when/then
		assertThatThrownBy(() -> lineNew.addSection(newStation, newStation2, 4))
			.isInstanceOf(RuntimeException.class);
	}
}
