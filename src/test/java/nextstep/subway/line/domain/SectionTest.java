package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.InvalidDistanceException;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.line.exception.InvalidSectionsException;
import nextstep.subway.station.domain.Station;

@DisplayName("구간 도메인 테스트")
public class SectionTest {

	Station 성수역;
	Station 뚝섬역;
	Line 이호선;
	Section 성수뚝섬구간;

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		이호선 = new Line(1L, "2호선", "초록색", new Fare(100));
		성수뚝섬구간 = new Section(이호선, 성수역, 뚝섬역, new Distance(10));
	}

	@DisplayName("구간 생성 테스트")
	@Test
	void create() {
		Section section = new Section(이호선, 성수역, 뚝섬역, new Distance(10));
		assertThat(section.getLine()).isEqualTo(이호선);
		assertThat(section.getUpStation()).isEqualTo(성수역);
		assertThat(section.getDownStation()).isEqualTo(뚝섬역);
		assertThat(section.getDistance()).isEqualTo(new Distance(10));
	}

	@DisplayName("구간 생성 테스트 - Line null 경우 에러 발생")
	@Test
	void createLineNullException() {
		assertThatThrownBy(() ->
			new Section(null, 성수역, 뚝섬역, new Distance(10))
		).isInstanceOf(InvalidSectionsException.class);
	}

	@DisplayName("구간 생성 테스트 - UpStation null 경우 에러 발생")
	@Test
	void createUpStationNullException() {
		assertThatThrownBy(() ->
			new Section(이호선, null, 뚝섬역, new Distance(10))
		).isInstanceOf(InvalidSectionsException.class);
	}

	@DisplayName("구간 생성 테스트 - DownStation null 경우 에러 발생")
	@Test
	void createDownStationNullException() {
		assertThatThrownBy(() ->
			new Section(이호선, 성수역, null, new Distance(10))
		).isInstanceOf(InvalidSectionsException.class);
	}

	@DisplayName("구간 생성 테스트 - Distance null 경우 에러 발생")
	@Test
	void createDistanceNullException() {
		assertThatThrownBy(() ->
			new Section(이호선, 성수역, 뚝섬역, null)
		).isInstanceOf(InvalidSectionsException.class);
	}

	@DisplayName("구간의 상행역과 같은지 확인 테스트")
	@Test
	void isEqualsUpStation() {
		assertThat(성수뚝섬구간.isEqualsUpStation(성수역)).isTrue();
		assertThat(성수뚝섬구간.isEqualsUpStation(뚝섬역)).isFalse();
	}

	@DisplayName("구간의 하행역과 같은지 확인 테스트")
	@Test
	void isEqualsDownStation() {
		assertThat(성수뚝섬구간.isEqualsDownStation(뚝섬역)).isTrue();
		assertThat(성수뚝섬구간.isEqualsDownStation(성수역)).isFalse();
	}

	@DisplayName("구간의 하행역과 같은지 확인 테스트")
	@Test
	void isDownStationExisted() {
		Station 건대입구역 = new Station("건대입구역");
		Station 강변역 = new Station("강변역");
		List<Station> stations = Arrays.asList(강변역, 건대입구역, 뚝섬역);
		assertThat(성수뚝섬구간.isDownStationExisted(stations)).isTrue();
		stations = Arrays.asList(강변역, 건대입구역, 성수역);
		assertThat(성수뚝섬구간.isDownStationExisted(stations)).isFalse();
	}

	@DisplayName("구간의 상행역과 같은지 확인 테스트")
	@Test
	void isUpStationExisted() {
		Station 건대입구역 = new Station("건대입구역");
		Station 강변역 = new Station("강변역");
		List<Station> stations = Arrays.asList(성수역, 강변역, 건대입구역);
		assertThat(성수뚝섬구간.isUpStationExisted(stations)).isTrue();
		stations = Arrays.asList(강변역, 건대입구역, 뚝섬역);
		assertThat(성수뚝섬구간.isUpStationExisted(stations)).isFalse();
	}

	@DisplayName("상행역 변경하기")
	@Test
	void updateUpStation() {
		Station 건대입구역 = new Station("건대입구역");
		성수뚝섬구간.updateUpStation(건대입구역, new Distance(6));
		assertThat(성수뚝섬구간.getUpStation()).isEqualTo(건대입구역);
		assertThat(성수뚝섬구간.getDownStation()).isEqualTo(뚝섬역);
		assertThat(성수뚝섬구간.getDistance()).isEqualTo(new Distance(4));
	}

	@DisplayName("상행역 변경하기 - 거리가 기존 구간보다 크거나 같을 때 에러 발생")
	@Test
	void updateUpStationDistanceError() {
		Station 건대입구역 = new Station("건대입구역");
		assertThatThrownBy(() ->
			성수뚝섬구간.updateUpStation(건대입구역, new Distance(10))
		).isInstanceOf(InvalidDistanceException.class);
	}

	@DisplayName("하행역 변경하기")
	@Test
	void updateDownStation() {
		Station 건대입구역 = new Station("건대입구역");
		성수뚝섬구간.updateDownStation(건대입구역, new Distance(6));
		assertThat(성수뚝섬구간.getUpStation()).isEqualTo(성수역);
		assertThat(성수뚝섬구간.getDownStation()).isEqualTo(건대입구역);
		assertThat(성수뚝섬구간.getDistance()).isEqualTo(new Distance(4));
	}

	@DisplayName("하행역 변경하기- 거리가 기존 구간보다 크거나 같을 때 에러 발생")
	@Test
	void updateDownStationDistanceError() {
		Station 건대입구역 = new Station("건대입구역");
		assertThatThrownBy(() ->
			성수뚝섬구간.updateDownStation(건대입구역, new Distance(10))
		).isInstanceOf(InvalidDistanceException.class);
	}
}
