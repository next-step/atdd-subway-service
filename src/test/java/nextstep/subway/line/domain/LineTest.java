package nextstep.subway.line.domain;

import nextstep.subway.line.ui.CannotDeleteOnlySectionException;
import nextstep.subway.line.ui.InvalidDistanceException;
import nextstep.subway.line.ui.TwoStationAlreadyExistException;
import nextstep.subway.line.ui.TwoStationNotExistException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    Line 삼호선;
    Station 일원역;
    Station 대청역;
    Station 학여울역;
    Station 대치역;

    @BeforeEach
    void setUp() {

        일원역 = new Station("일원역");
        대청역 = new Station("대청역");
        학여울역 = new Station("학여울역");
        대치역 = new Station("대치역");

        삼호선 = new Line("3호선", "red", 대치역, 학여울역, 10);

    }

    @DisplayName("역 리스트 만들기")
    @Test
    void 역_리스트_만들기() {
        List<Station> stations = 삼호선.stations();

        assertThat(stations).containsExactly(대치역, 학여울역);
    }

    @DisplayName("구간 추가(맨 뒤에)")
    @Test
    void 맨_뒤에_구간_추가() {
        //given
        삼호선.addSection(new Section(학여울역, 대청역, 10));

        //when
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대치역, 학여울역, 대청역);
    }

    @DisplayName("구간 추가(맨 앞에)")
    @Test
    void 맨_앞에_구간_추가() {
        //given
        삼호선.addSection(new Section(대청역, 대치역, 10));

        //when
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대청역, 대치역, 학여울역);
    }

    @DisplayName("구간 추가(중간에)")
    @Test
    void 중간에_구간_추가() {
        //given
        삼호선.addSection(new Section(대치역, 대청역, 6));

        //when
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대치역, 대청역, 학여울역);

        //when
        Section findSection = 삼호선.getSections().stream()
                .filter(section -> section.getUpStation().equals(대청역))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findSection.getDistance()).isEqualTo(4);
    }

    @DisplayName("구간 추가 거리 초과")
    @Test
    void 구간_추가_거리_초과() {
        //when, then
        assertThatThrownBy(() -> 삼호선.addSection(new Section(대치역, 대청역, 15)))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @DisplayName("등록하는 역이 존재하지 않음")
    @Test
    void 등록하는_역이_존재하지_않음() {
        //when, then
        assertThatThrownBy(() -> 삼호선.addSection(new Section(일원역, 대청역, 15)))
                .isInstanceOf(TwoStationNotExistException.class);
    }

    @DisplayName("등록하는 역이 모두 존재함")
    @Test
    void 등록하는_역이_모두_존재함() {
        //when, then
        assertThatThrownBy(() -> 삼호선.addSection(new Section(대치역, 학여울역, 15)))
                .isInstanceOf(TwoStationAlreadyExistException.class);
    }

    @DisplayName("역 삭제(맨 앞)")
    @Test
    void 맨_앞역_삭제() {
        //given
        삼호선.addSection(new Section(대청역, 대치역, 10));

        //when
        삼호선.removeSection(대청역);
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대치역, 학여울역);
    }

    @DisplayName("역 삭제(맨 뒤)")
    @Test
    void 맨_뒤역_삭제() {
        //given
        삼호선.addSection(new Section(대청역, 대치역, 10));

        //when
        삼호선.removeSection(학여울역);
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대청역, 대치역);
    }

    @DisplayName("역 삭제(중간)")
    @Test
    void 중간역_삭제() {
        //given
        삼호선.addSection(new Section(대청역, 대치역, 10));

        //when
        삼호선.removeSection(대치역);
        List<Station> stations = 삼호선.stations();

        //then
        assertThat(stations).containsExactly(대청역, 학여울역);

        //when
        Section findSection = 삼호선.getSections().stream()
                .filter(section -> section.getUpStation().equals(대청역))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findSection.getDistance()).isEqualTo(20);
    }

    @DisplayName("두역 밖에 없을 때 삭제")
    @Test
    void 두역_밖에_없을_때_삭제() {
        //when, then
        assertThatThrownBy(() -> 삼호선.removeSection(대치역)).isInstanceOf(CannotDeleteOnlySectionException.class);
    }

    @DisplayName("삭제하려는 역이 없을 때")
    @Test
    void 삭제하려는_역이_없을_때() {
        //when, then
        assertThatThrownBy(() -> 삼호선.removeSection(일원역)).isInstanceOf(CannotDeleteOnlySectionException.class);
    }
}
