package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;

@DisplayName("노선 테스트")
class LineTest {

	@Test
	@DisplayName("노선 생성")
	void createLineTest() {
		assertThatNoException()
			.isThrownBy(() -> Line.of(Name.from("신분당선"), Color.from("RED"), mock(Sections.class)));
	}

	@Test
	@DisplayName("노선 생성 시 이름이 없을 경우 예외")
	void createLineWithoutNameTest() {
		assertThatThrownBy(() -> Line.of(null, Color.from("RED"), mock(Sections.class)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("노선 생성 시 색상이 없을 경우 예외")
	void createLineWithoutColorTest() {
		assertThatThrownBy(() -> Line.of(Name.from("신분당선"), null, mock(Sections.class)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("노선 생성 시 구간이 없을 경우 예외")
	void createLineWithoutSectionsTest() {
		assertThatThrownBy(() -> Line.of(Name.from("신분당선"), Color.from("RED"), null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("노선 수정")
	void updateLineTest() {
		// given
		Name newName = Name.from("구분당선");
		Color newColor = Color.from("GREEN");
		Line line = Line.of(Name.from("신분당선"), Color.from("RED"), mock(Sections.class));

		// when
		line.update(newName, newColor);

		// then
		Assertions.assertThat(line.getName()).isEqualTo(newName.toString());
		Assertions.assertThat(line.getColor()).isEqualTo(newColor.toString());
	}

	@Test
	@DisplayName("노선 수정 시 이름이 없을 경우 예외")
	void updateLineWithoutNameTest() {
		// given
		Line line = Line.of(Name.from("신분당선"), Color.from("RED"), mock(Sections.class));

		// when & then
		assertThatThrownBy(() -> line.update(null, Color.from("GREEN")))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("노선 수정 시 색상이 없을 경우 예외")
	void updateLineWithoutColorTest() {
		// given
		Line line = Line.of(Name.from("신분당선"), Color.from("RED"), mock(Sections.class));

		// when & then
		assertThatThrownBy(() -> line.update(Name.from("구분당선"), null))
			.isInstanceOf(IllegalArgumentException.class);
	}
}