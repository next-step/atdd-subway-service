package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LineTest {

	private Line line;
	private Station originEndUpStation;
	private Station originMiddleUpStation;
	private Station originMiddleDownStation;
	private Station originEndDownEndStation;

	private static final int END_UP_DISTANCE = 10;
	private static final int MIDDLE_DISTANCE = 12;
	private static final int END_DOWN_DISTANCE = 8;
	private static final int ORIGIN_SECTION_SIZE = 3;
	private static final int ORIGIN_STATION_SIZE = 4;
	private static final int ORIGIN_TOTAL_DISTANCE = END_UP_DISTANCE + MIDDLE_DISTANCE + END_DOWN_DISTANCE;
	private Section originSection1;
	private Section originSection2;
	private Section originSection3;

	Station newStation;

	@BeforeEach
	void setUp() {
		//given
		originEndUpStation = new Station("당산역");
		originMiddleUpStation = new Station("문래역");
		originMiddleDownStation = new Station("사당역");
		originEndDownEndStation = new Station("잠실역");
		originSection1 = new Section(line, originEndUpStation, originMiddleUpStation, END_UP_DISTANCE);
		originSection2 = new Section(line, originMiddleUpStation, originMiddleDownStation, MIDDLE_DISTANCE);
		originSection3 = new Section(line, originMiddleDownStation, originEndDownEndStation, END_DOWN_DISTANCE);
		line = new Line("2호선", "green", Arrays.asList(originSection1, originSection2, originSection3));
	}

	@Test
	@DisplayName("신규 구간 등록 (중간 상행역기준) : A-B-C-D 에서 B-NEW 추가 -> A-B-NEW-C-D")
	void addSectionBasedMiddleUpStation() {
		//given
		newStation = new Station("신도림역");
		int newDistance = 1;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, newStation, originMiddleDownStation, originEndDownEndStation};

		//when
		line.addSection(originMiddleUpStation, newStation, newDistance);

		//then
		List<Section> sections = line.getSections();
		Section originDownSection = sections.stream()
			.filter(section -> section.getDownStation().equals(originMiddleDownStation))
			.findFirst().get();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//기존 하행역의 상행역이 새로운 역으로 변경되어야한다.
		assertThat(originDownSection.getUpStation()).isEqualTo(newStation);
		//기존 하행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.
		assertThat(originDownSection.getDistance()).isEqualTo(MIDDLE_DISTANCE - newDistance);
		//상행종점부터 하행종점까지 추가한 역을 포함하여 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 변하지 않아야 한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE);
	}

	@Test
	@DisplayName("신규 구간 등록 (중간 하행역기준) : A-B-C-D 에서 NEW-C 추가 -> A-B-NEW-C-D")
	void addSectionBasedMiddleDownStation() {
		//given
		newStation = new Station("서울대입구역");
		int newDistance = 2;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, newStation, originMiddleDownStation, originEndDownEndStation};

		//when
		line.addSection(newStation, originMiddleDownStation, newDistance);

		//then
		List<Section> sections = line.getSections();
		Section originUpSection = sections.stream()
			.filter(section -> section.getUpStation().equals(originMiddleUpStation))
			.findFirst().get();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//상행역의 하행역이 새로운 역으로 변경되어야한다.
		assertThat(originUpSection.getDownStation()).isEqualTo(newStation);
		//기존 하행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.
		assertThat(originUpSection.getDistance()).isEqualTo(MIDDLE_DISTANCE - newDistance);
		//상행종점부터 하행종점까지 추가한 역을 포함하여 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 변하지 않아야 한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE);
	}

	@Test
	@DisplayName("신규 구간 등록 (상행종점) : A-B-C-D 에서 NEW-A 추가 -> NEW-A-B-C-D")
	void addSectionEndUpStation() {
		//given
		newStation = new Station("홍대입구역");
		int newDistance = 2;
		Station[] expectedSortedStations = {newStation, originEndUpStation, originMiddleUpStation, originMiddleDownStation, originEndDownEndStation};

		//when
		line.addSection(newStation, originEndUpStation, newDistance);

		//then
		List<Section> sections = line.getSections();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//새 구간이 추가되어야한다
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE + 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE + 1);
		//새로운 상행종점부터 하행종점까지 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이가 신규 구간만큼 늘어나야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE + newDistance);
	}

	@Test
	@DisplayName("신규 구간 등록 (하행종점) : A-B-C-D 에서 D-NEW 추가 -> A-B-C-D-NEW")
	void addSectionEndDownStation() {
		//given
		newStation = new Station("건대입구역");
		int newDistance = 4;
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, originMiddleDownStation, originEndDownEndStation, newStation};

		//when
		line.addSection(originEndDownEndStation, newStation, newDistance);

		//then
		List<Section> sections = line.getSections();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

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

		//when/then
		assertThatThrownBy(() -> line.addSection(originMiddleUpStation, newStation, MIDDLE_DISTANCE))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("신규 구간의 거리가 기존 구간의 거리보다 큰 경우, RuntimeException 을 Throw 해야한다.")
	void addSectionGreaterThanOriginDistance() {
		//given
		newStation = new Station("신도림역");
		int newDistance = MIDDLE_DISTANCE + 1;

		//when/then
		assertThatThrownBy(() -> line.addSection(originMiddleUpStation, newStation, newDistance))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("새로운 구간의 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 RuntimeException 을 Throw 해야한다.")
	void addSectionAlreadyExist() {
		//when/then
		assertThatThrownBy(() -> line.addSection(originMiddleUpStation, originMiddleDownStation, 4))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("새로운 구간의 상행역과 하행역 둘 중 하나도 노선에 포함되어있지 않다면 RuntimeException 을 Throw 해야한다.")
	void addSectionNotExist() {
		//given
		Station newStation = new Station("홍대입구역");
		Station newStation2 = new Station("이대역");

		//when/then
		assertThatThrownBy(() -> line.addSection(newStation, newStation2, 4))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("구간 제거 (중간역) : A-B-C-D 에서 B 제거 -> A-C-D")
	void removeMiddleStation() {
		//given
		Station[] expectedSortedStations = {originEndUpStation, originMiddleDownStation, originEndDownEndStation};

		//when
		line.removeStation(originMiddleUpStation);

		//then
		List<Section> sections = line.getSections();
		Section originDownSection = sections.stream()
			.filter(section -> section.getDownStation().equals(originMiddleDownStation))
			.findFirst().get();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//구간이 제거 되어야한다.
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE - 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE - 1);
		//제거된 구간을 제외하고 이전 노선의 순서대로 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//제거되는 역이 상행역인 구간의 상행역은 제거되는 역이 하행역인 구간의 상행역으로 변경해야한다.
		assertThat(originDownSection.getUpStation()).isEqualTo(originEndUpStation);
		//제거되는 역이 상행역인 구간의 거리는 제거되는 역이 하행역인 구간의 거리와 더해진 값이어야 한다.
		assertThat(originDownSection.getDistance()).isEqualTo(END_UP_DISTANCE + MIDDLE_DISTANCE);
		//전체 구간의 길이는 달라지지 않아야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE);
	}

	@Test
	@DisplayName("구간 제거 (상행종점) : A-B-C-D 에서 A 제거 -> B-C-D")
	void removeEndUpStation() {
		//given
		Station[] expectedSortedStations = {originMiddleUpStation, originMiddleDownStation, originEndDownEndStation};

		//when
		line.removeStation(originEndUpStation);

		//then
		List<Section> sections = line.getSections();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//구간이 제거 되어야한다.
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE - 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE - 1);
		//기존 상행종점을 제외하고 이전 노선의 순서대로 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 상행종점을 포함한 구간의 길이만큼 짧아져야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE - END_UP_DISTANCE);
	}

	@Test
	@DisplayName("구간 제거 (하행종점) : A-B-C-D 에서 D 제거 -> A-B-C")
	void removeEndDownStation() {
		//given
		Station[] expectedSortedStations = {originEndUpStation, originMiddleUpStation, originMiddleDownStation};

		//when
		line.removeStation(originEndDownEndStation);

		//then
		List<Section> sections = line.getSections();
		int totalDistance = sections.stream()
			.mapToInt(Section::getDistance)
			.sum();
		List<Station> stations = line.getStations();

		//구간이 제거 되어야한다.
		assertThat(sections.size()).isEqualTo(ORIGIN_SECTION_SIZE - 1);
		assertThat(stations.size()).isEqualTo(ORIGIN_STATION_SIZE - 1);
		//기존 상행종점을 제외하고 이전 노선의 순서대로 정렬되어야한다.
		assertThat(stations).containsExactly(expectedSortedStations);
		//전체 구간의 길이는 상행종점을 포함한 구간의 길이만큼 짧아져야한다.
		assertThat(totalDistance).isEqualTo(ORIGIN_TOTAL_DISTANCE - END_DOWN_DISTANCE);
	}

	@Test
	@DisplayName("구간이 하나인 노선에서 마지막 구간을 제거하려 할 때 RuntimeException 을 Throw 해야한다.")
	void removeStationExistOneSection() {
		//given
		Line newLineHavingOneSection = new Line("2호선", "green", originEndUpStation, originEndDownEndStation, END_UP_DISTANCE);

		//when/then
		assertThatThrownBy(() -> newLineHavingOneSection.removeStation(originEndUpStation))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("노선에 등록되어있지 않은 역을 제거하려 할 때, RuntimeException 을 Throw 해야한다.")
	void removeStationNotExist() {
		//given
		Line newLineHavingOneSection = new Line("2호선", "green", originEndUpStation, originEndDownEndStation, END_UP_DISTANCE);

		//when/then
		assertThatThrownBy(() -> newLineHavingOneSection.removeStation(originMiddleDownStation))
			.isInstanceOf(RuntimeException.class);
	}
}
