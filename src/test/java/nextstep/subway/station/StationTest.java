package nextstep.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    public static final String STATION_NAME1 = "강남역";
    public static final String STATION_NAME2 = "역삼역";
    public static final String STATION_NAME3 = "선릉역";
    public static final String STATION_NAME4 = "삼성역";
    public static final Station STATION1 = new Station(STATION_NAME1);
    public static final Station STATION2 = new Station(STATION_NAME2);

    @Test
    @DisplayName("Station 생성 후 name 검증")
    void create() {
        // given
        // when
        Station station = new Station(STATION_NAME1);

        // when
        assertThat(station.getName()).isEqualTo(STATION_NAME1);
    }

    @Test
    @DisplayName("Station 생성 시 name 는 빈값일 경우 에러 발생")
    void validEmpty() {
        assertThrows(InvalidParameterException.class, () -> new Station(""));
    }

}
