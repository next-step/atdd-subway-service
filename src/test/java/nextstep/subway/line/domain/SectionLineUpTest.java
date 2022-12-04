package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 목록 클래스 테스트")
class SectionLineUpTest {

    private final Line 분당선 = new Line("분당선", "color");
    private final Station 삼전역 = new Station("삼전역");
    private final Station 석촌고분역 = new Station("석촌고분역");
    private final Station 잠실새내역 = new Station("잠실새내역");
    private final Station 선릉역 = new Station("선릉역");
    private final Distance 거리 = new Distance(10);
    private final Section 삼전역_석촌고분역_구간 = new Section(분당선, 삼전역, 석촌고분역, 거리);
    private final Section 석촌고분역_잠실새내역_구간 = new Section(분당선, 석촌고분역, 잠실새내역, 거리);

    private final Section 잠실새내역_선릉역_구간 = new Section(분당선, 잠실새내역, 선릉역, 거리);

    @DisplayName("생성 성공")
    @Test
    void create_sectionLineUp_success() {

        assertThatNoException().isThrownBy(() -> new SectionLineUp(Arrays.asList(삼전역_석촌고분역_구간, 석촌고분역_잠실새내역_구간)));
    }

    @DisplayName("구간 추가 실패 - 이미 등록된 구간")
    @Test
    void addDuplicates_section_RuntimeException() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp(Arrays.asList(삼전역_석촌고분역_구간, 석촌고분역_잠실새내역_구간));
        //when, then:
        assertThatThrownBy(() -> 분당선_구간.add(삼전역_석촌고분역_구간)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 내 포함 된 지하철 역 목록 조회")
    @Test
    void getStation_sectionLineUp_success() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp(Arrays.asList(삼전역_석촌고분역_구간, 석촌고분역_잠실새내역_구간));
        //when:
        List<Station> 지하철역_목록 = 분당선_구간.getStations();
        //then:
        assertThat(지하철역_목록).containsSequence(삼전역, 석촌고분역, 잠실새내역);
    }

    @DisplayName("구간 추가 성공")
    @Test
    void add_section_success() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        //when:
        분당선_구간.add(삼전역_석촌고분역_구간);
        분당선_구간.add(석촌고분역_잠실새내역_구간);
        분당선_구간.add(잠실새내역_선릉역_구간);
        //then:
        assertThat(분당선_구간.sectionSize()).isEqualTo(3);
    }

    @DisplayName("예외 - 이미 존재하는 구간 추가")
    @Test
    void addDuplicates_section_IllegalArgumentException() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        분당선_구간.add(삼전역_석촌고분역_구간);
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> 분당선_구간.add(삼전역_석촌고분역_구간));
    }

    @DisplayName("예외 - 겹치는 지하철역이 없는 구간 추가")
    @Test
    void addUnknownStation_section_IllegalArgumentException() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        분당선_구간.add(삼전역_석촌고분역_구간);
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> 분당선_구간.add(잠실새내역_선릉역_구간));
    }

    @DisplayName("종점 구간 삭제 성공")
    @Test
    void removeUpStation_section_success() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        분당선_구간.add(삼전역_석촌고분역_구간);
        분당선_구간.add(석촌고분역_잠실새내역_구간);
        //when:
        분당선_구간.remove(삼전역);
        //then:
        assertThat(분당선_구간.getStations()).containsSequence(석촌고분역, 잠실새내역);
    }

    @DisplayName("중간 구간 삭제 성공")
    @Test
    void removeInternalStation_section_success() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        분당선_구간.add(삼전역_석촌고분역_구간);
        분당선_구간.add(석촌고분역_잠실새내역_구간);
        //when:
        분당선_구간.remove(석촌고분역);
        //then:
        assertThat(분당선_구간.getStations()).containsSequence(삼전역, 잠실새내역);
    }

    @DisplayName("예외 - 구간이 하나인 노선의 구간을 제거하는 경우")
    @Test
    void removeOnlyOneSection_section_IllegalArgumentException() {
        //given:
        SectionLineUp 분당선_구간 = new SectionLineUp();
        분당선_구간.add(삼전역_석촌고분역_구간);
        //when, then:
        assertThatThrownBy(() -> 분당선_구간.remove(삼전역)).isInstanceOf(IllegalStateException.class);
    }
}
