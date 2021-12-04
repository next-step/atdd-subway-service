package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.exception.SectionException;
import nextstep.subway.station.domain.Station;

@DisplayName("단위 테스트 - 구간 일급 컬렉션 도메인")
class SectionsTest {

	private final Line 신분당선 = Line.from();
	private Sections 신분당선_구간들;
	private Station 강남역;
	private Station 광교역;
	private Station 청계산입구역;
	private Station 판교역;
	private Station 정자역;
	private Section 강남_정자_구간;

	@BeforeEach
	void setup() {
		신분당선_구간들 = new Sections();
		강남역 = Station.from("강남역");
		광교역 = Station.from("광교역");
		청계산입구역 = Station.from("청계산입구역");
		판교역 = Station.from("판교역");
		정자역 = Station.from("정자역");
		강남_정자_구간 = Section.of(Line.from(), 강남역, 정자역, 10);
		신분당선_구간들.addSection(강남_정자_구간);
	}

	@DisplayName("같은 상, 하행 종점인 구간을 추가하는 경우 예외 발생")
	@Test
	void exceptionSameUpDownStation() throws Exception {
		// given
		Section section = Section.of(신분당선, 강남역, 정자역, 10);
		Method method = 신분당선_구간들.getClass().getDeclaredMethod("validSameUpDownStation", Section.class);
		method.setAccessible(true);

		// when // then
		assertThatThrownBy(() -> {
			method.invoke(신분당선_구간들, section);
		}).isInstanceOf(InvocationTargetException.class);
	}

	@DisplayName("구간의 상, 하행 종점 중 하나도 포함하지 않는 구간을 추가하는 경우 예외 발생")
	@Test
	void exceptionIsNotInStations() throws Exception {
		// given
		Section section = Section.of(신분당선, 청계산입구역, 판교역, 1);
		Method method = 신분당선_구간들.getClass().getDeclaredMethod("validIsNotInStations", Section.class);
		method.setAccessible(true);

		// when // then
		assertThatThrownBy(() -> {
			method.invoke(신분당선_구간들, section);
		}).isInstanceOf(InvocationTargetException.class);
	}

	@DisplayName("구간의 상행 종점 리스트를 조회하는 테스트")
	@Test
	void getUpStations() {
		// given
		Section section = Section.of(신분당선, 판교역, 정자역, 4);
		신분당선_구간들.addSection(section);

		// when
		List<Station> upStations = 신분당선_구간들.getUpStations();

		// then
		assertAll(
			() -> assertThat(upStations).hasSize(2),
			() -> assertThat(upStations).containsExactly(강남역, 판교역)
		);
	}

	@DisplayName("구간의 하행 종점 리스트를 조회하는 테스트")
	@Test
	void getDownStations() {
		// given
		Section section = Section.of(신분당선, 강남역, 판교역, 4);
		신분당선_구간들.addSection(section);

		// when
		List<Station> downStations = 신분당선_구간들.getDownStations();

		// then
		assertAll(
			() -> assertThat(downStations).hasSize(2),
			() -> assertThat(downStations).containsExactly(정자역, 판교역)
		);
	}

	@DisplayName("구간 중 가장 앞에 존재하는 구간을 반환하는 테스트")
	@Test
	void findFirstSection() throws Exception {
		// given
		Section section = Section.of(신분당선, 판교역, 정자역, 4);
		신분당선_구간들.addSection(section);
		Method method = 신분당선_구간들.getClass().getDeclaredMethod("findFirstSection");
		method.setAccessible(true);

		// when
		Section expectSection = (Section)method.invoke(신분당선_구간들);

		// then
		assertThat(expectSection).isEqualTo(강남_정자_구간);
	}

	@DisplayName("구간 중 가장 마지막 구간을 반환하는 테스트")
	@Test
	void findLastSection() throws Exception {
		// given
		Section section = Section.of(신분당선, 정자역, 광교역, 4);
		신분당선_구간들.addSection(section);
		Method method = 신분당선_구간들.getClass().getDeclaredMethod("findLastSection");
		method.setAccessible(true);

		// when
		Section expectSection = (Section)method.invoke(신분당선_구간들);

		// then
		assertThat(expectSection).isEqualTo(section);
	}

	@DisplayName("구간의 지하철역을 순서대로 조회하는 테스트")
	@Test
	void getStations() {
		// given
		Section section = Section.of(신분당선, 판교역, 정자역, 4);
		신분당선_구간들.addSection(section);

		// when
		List<Station> getStations = 신분당선_구간들.getStations();

		// then
		assertThat(getStations).containsExactly(강남역, 판교역, 정자역);
	}

	@DisplayName("구간이 1개만 존재할 때 예외처리 테스트")
	@Test
	void validSectionsSize() {
		//given // when // then
		assertThatThrownBy(() -> {
			신분당선_구간들.validRemoveStation(Station.from("test"));
		}).isInstanceOf(SectionException.class)
			.hasMessageContaining(ErrorCode.VALID_CAN_NOT_REMOVE_LAST_STATION.getErrorMessage());
	}

	@DisplayName("삭제하려는 역이 구간내에 존재하지 않을 때 예외처리 테스트")
	@Test
	void validStationInStations() {
		// given
		Station givenUpStation = Station.from("강남역");
		Station givenDownStation = Station.from("양재역");
		Section givenSection = Section.of(신분당선, givenUpStation, givenDownStation, 3);
		신분당선_구간들.addSection(givenSection);

		Station expectStation = Station.from("합정역");

		// when // then
		assertThatThrownBy(() -> {
			신분당선_구간들.validRemoveStation(expectStation);
		}).isInstanceOf(SectionException.class)
			.hasMessageContaining(ErrorCode.VALID_CAN_NOT_REMOVE_NOT_IN_STATIONS.getErrorMessage());
	}

	@DisplayName("삭제하려는 역이 구간의 상, 하행 종점역 사이에 존재하는지 확인하는 메소드 테스트")
	@Test
	void isBetweenStations() throws Exception {
		// given
		Station givenUpStation = Station.from("강남역");
		Station givenDownStation = Station.from("양재역");
		Section givenSection = Section.of(신분당선, givenUpStation, givenDownStation, 3);
		신분당선_구간들.addSection(givenSection);
		Method method = 신분당선_구간들.getClass().getDeclaredMethod("isBetweenStations", Station.class);
		method.setAccessible(true);

		// when
		boolean expect = (boolean)method.invoke(신분당선_구간들, givenDownStation);

		// then
		assertThat(expect).isTrue();
	}
}