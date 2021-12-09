package nextstep.subway.line.domain;

import nextstep.subway.common.exception.AlreadyRegisteredSectionException;
import nextstep.subway.common.exception.MinimumRemovableSectionSizeException;
import nextstep.subway.common.exception.NoRegisteredStationsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("구간 목록 관련 도메인 기능")
class SectionsTest {
    private Sections sections;
    private Station 강남역;
    private Station 신사역;
    private Line 호선2;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        신사역 = new Station("신사역");
        호선2 = new Line("2호선", "green", 강남역, 신사역, 9);
        final Section 강남_신사_구간 = new Section(호선2, 강남역, 신사역, 9);
        sections = new Sections();
        sections.getSections().add(강남_신사_구간);
    }

    @DisplayName("구간을 추가한다")
    @Test
    void addLineStation() {
        //given
        final Station 잠실역 = new Station("잠실역");

        // when
        this.sections.addLineStation(호선2, 잠실역, 신사역, new Distance(5));

        // then
        assertThat(sections.getSections().size()).isEqualTo(2);
    }

    @DisplayName("구간을 추가한 후 역 목록을 확인한다")
    @Test
    void sortSections() {
        // given
        final Station 강남신사사이역 = new Station("강남신사사이역");

        // when
        sections.addLineStation(호선2, 강남역, 강남신사사이역, new Distance(3));

        // then
        assertThat(sections.getStations()).containsExactly(강남역, 강남신사사이역, 신사역);
    }

    @DisplayName("두 역을 모두 포함한 경우 예외를 발생시킨다")
    @Test
    void addSectionBothStationsException() {
        assertThatThrownBy(() -> {
            this.sections.addLineStation(호선2, 강남역, 신사역, new Distance(5));

        }).isInstanceOf(AlreadyRegisteredSectionException.class)
        .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("두 역을 모두 포함하지 않는 경우 예외를 발생시킨다")
    @Test
    void addSectionNoStationsException() {
        assertThatThrownBy(() -> {
            this.sections.addLineStation(호선2, new Station("새로운 역1"), new Station("새로운 역2"), new Distance(5));

        }).isInstanceOf(NoRegisteredStationsException.class)
        .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간 사이에 있는 역을 제거할 수 있다")
    @Test
    void removeSection() {
        //given
        final Station 잠실역 = new Station("잠실역");
        this.sections.addLineStation(호선2, 잠실역, 신사역, new Distance(5));

        // when
        this.sections.removeLineStation(호선2, 잠실역);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("최수 구간 크키보다 작을 때 제거를 시도하면 예외를 발생시킨다")
    @Test
    void removeSectionMinimumSectionSizeException() {
        assertThatThrownBy(() -> {
            this.sections.removeLineStation(호선2, 강남역);

        }).isInstanceOf(MinimumRemovableSectionSizeException.class)
        .hasMessageContaining("최소 삭제 가능한 구간 크기를 확인 해 주세요.");
    }
}