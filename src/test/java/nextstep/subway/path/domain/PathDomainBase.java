package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PathDomainBase {
    protected static final int DEFAULT_DISTANCE = 10;
    protected static final int LINE_CHARGE_900 = 900;
    protected static final int LINE_CHARGE_500 = 500;

    protected static Line 이호선 = new Line("2호선", "green", 0);
    @Mock
    protected static Station 신도림;
    @Mock
    protected static Station 문래;
    @Mock
    protected static Station 영등포구청;
    @Mock
    protected static Station 당산;

    protected static Line 구호선 = new Line("9호선", "brown", LINE_CHARGE_900);
    @Mock
    protected static Station 국회의사당;
    @Mock
    protected static Station 여의도;
    @Mock
    protected static Station 샛강;
    @Mock
    protected static Station 노량진;

    protected static Line 일호선 = new Line("1호선", "blue", LINE_CHARGE_500);
    @Mock
    protected static Station 영등포;
    @Mock
    protected static Station 신길;
    @Mock
    protected static Station 대방;

    public PathDomainBase() {
        MockitoAnnotations.openMocks(this);
        mockStation(신도림, 1L);
        mockStation(문래, 2L);
        mockStation(영등포구청, 3L);
        mockStation(당산, 4L);

        mockStation(국회의사당, 5L);
        mockStation(여의도, 6L);
        mockStation(샛강, 7L);
        mockStation(노량진, 8L);

        mockStation(영등포, 9L);
        mockStation(신길, 10L);
        mockStation(대방, 11L);
    }

    private void mockStation(Station station, long id) {
        when(station.getId()).thenReturn(id);
        when(station.equalsId(any())).thenCallRealMethod();
    }

    protected static <T> ArgumentSupplier<T> supply(Supplier<T> supplier) {
        return new ArgumentSupplier<>(supplier);
    }

    protected static class ArgumentSupplier<T> {
        private Supplier<T> supplier;

        public ArgumentSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public T get() {
            return supplier.get();
        }
    }
}
