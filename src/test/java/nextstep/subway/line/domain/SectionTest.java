package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    private static final Line 신분당선 = new Line("신분당선", "bg-red-600");
    private static final Station 강남역 = new Station("강남역");
    private static final Station 서초역 = new Station("서초역");
    private static final Station 반포역 = new Station("반포역");

    @DisplayName("앞역이 같을 경우, 구간을 수정한다.")
    @Test
    void update_sameUpStation() {
        //given
        Section section = new Section(신분당선, 강남역, 반포역, 4);

        //when
        section.update(new Section(신분당선, 강남역, 서초역, 2));

        //then
        assertAll(
                () -> assertThat(section.getUpDownStation()).containsExactly(서초역, 반포역),
                () -> assertEquals(2, section.getDistance())
        );
    }

    @DisplayName("길이가 같거나 큰 구간으로 수정하려고하면, exception")
    @Test
    void update_fail_invalidDistance() {
        //given
        Section section = new Section(신분당선, 강남역, 반포역, 4);

        //when//then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> section.update(new Section(신분당선, 강남역, 서초역, 4)))
                .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("뒷역이 같을 경우, 구간을 수정한다.")
    @Test
    void update_sameDownStation() {
        //given
        Section section = new Section(신분당선, 강남역, 반포역, 4);

        //when
        section.update(new Section(신분당선, 서초역, 반포역, 2));

        //then
        assertAll(
                () -> assertThat(section.getUpDownStation()).containsExactly(강남역, 서초역),
                () -> assertEquals(2, section.getDistance())
        );
    }

    @DisplayName("뒤에 구간인지 확인한다.")
    @Test
    void isNext() {
        //given
        Section before = new Section(신분당선, 강남역, 서초역, 2);
        Section after = new Section(신분당선, 서초역, 반포역, 3);

        //when
        boolean isNext = after.isNext(before);

        //then
        assertTrue(isNext);
    }

    @DisplayName("앞에 구간인지 확인한다.")
    @Test
    void isBefore() {
        //given
        Section before = new Section(신분당선, 강남역, 서초역, 2);
        Section after = new Section(신분당선, 서초역, 반포역, 3);

        //when
        boolean isBefore = before.isBefore(after);

        //then
        assertTrue(isBefore);
    }

    @DisplayName("구간의 앞역인지 확인한다.")
    @Test
    void isUpStation() {
        //given
        Section section = new Section(신분당선, 강남역, 서초역, 2);

        //when
        boolean isUpStation = section.isUpStation(강남역);

        //then
        assertTrue(isUpStation);
    }

    @DisplayName("구간의 뒷역인지 확인한다.")
    @Test
    void isDownStation() {
        //given
        Section section = new Section(신분당선, 강남역, 서초역, 2);

        //when
        boolean isDownStation = section.isDownStation(서초역);

        //then
        assertTrue(isDownStation);
    }

    @DisplayName("뒷구간과 합쳐서 한 구간으로 만든다.")
    @Test
    void combine() {
        //given
        Section section = new Section(신분당선, 강남역, 서초역, 2);
        Section after = new Section(신분당선, 서초역, 반포역, 2);

        //when
        Section combine = section.combine(after);

        //then
        assertAll(
                () -> assertThat(combine.getUpDownStation()).containsExactly(강남역, 반포역),
                () -> assertEquals(4, combine.getDistance())
        );
    }
}