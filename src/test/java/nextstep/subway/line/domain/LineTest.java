package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.LineFixture.신이호선;
import static nextstep.subway.line.domain.LineFixture.이호선;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineTest {

    @DisplayName("노선 생성 시 구간 정보가 등록된다.")
    @Test
    void 노선_생성_테스트() {
        Line line = 이호선();
        assertThat(line.getStations()).contains(합정역(), 당산역());
    }

    @DisplayName("노선의 정보를 갱신한다.")
    @Test
    void 노선의_정보_수정_테스트() {
        Line line = 이호선();
        line.update(신이호선());

        assertAll(
                () -> assertThat(line.getName()).isEqualTo(신이호선().getName()),
                () -> assertThat(line.getColor()).isEqualTo(신이호선().getColor())
        );
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void 구간_추가_테스트() {
        Line line = 이호선();
        line.addLineStation(영등포구청_당산_구간(line));
        assertThat(line.getStations()).contains(영등포구청역(), 당산역());
    }

    @DisplayName("노선에서 구간을 삭제한다.")
    @Test
    void 구간_삭제_테스트() {
        Line line = 이호선();
        line.addLineStation(영등포구청_당산_구간(line));
        line.removeLineStation(합정역());

        assertThat(line.getStations().contains(합정역())).isFalse();
    }

    @DisplayName("이미 등록된 구간을 등록한다.")
    @Test
    void 이미_등록된_구간_등록_테스트() {
        Line line = 이호선();
        line.addLineStation(영등포구청_당산_구간(line));

        assertThatThrownBy(
                () -> line.addLineStation(당산_합정_구간(line))
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("기존에 존재하지 않는 역들의 구간을 등록한다.")
    @Test
    void 유효하지_않은_구간_등록_테스트() {
        Line line = 이호선();

        assertThatThrownBy(
                () -> line.addLineStation(여의_여의나루_구간(line))
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노건의 하나 뿐인 구간을 삭제한다.")
    @Test
    void 노선_유일_구간_삭제_테스트() {
        Line line = 이호선();

        assertThatThrownBy(
                () -> line.removeLineStation(이호선().getStations().get(0))
        ).isInstanceOf(InvalidDataException.class);
    }
}
