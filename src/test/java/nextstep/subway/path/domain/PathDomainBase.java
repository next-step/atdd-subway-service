package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.Mockito.when;

public class PathDomainBase {
    protected static final int DEFAULT_DISTANCE = 10;

    protected static Line 이호선 = new Line("2호선", "green");
    @Mock
    protected static Station 신도림;
    @Mock
    protected static Station 문래;
    @Mock
    protected static Station 영등포구청;
    @Mock
    protected static Station 당산;

    protected static Line 구호선 = new Line("9호선", "brown");
    @Mock
    protected static Station 국회의사당;
    @Mock
    protected static Station 여의도;
    @Mock
    protected static Station 샛강;
    @Mock
    protected static Station 노량진;

    protected static Line 일호선 = new Line("1호선", "blue");
    @Mock
    protected static Station 영등포;
    @Mock
    protected static Station 신길;
    @Mock
    protected static Station 대방;

    public PathDomainBase() {
        MockitoAnnotations.openMocks(this);
        when(신도림.getId()).thenReturn(1L);
        when(문래.getId()).thenReturn(2L);
        when(영등포구청.getId()).thenReturn(3L);
        when(당산.getId()).thenReturn(4L);

        when(국회의사당.getId()).thenReturn(5L);
        when(여의도.getId()).thenReturn(6L);
        when(샛강.getId()).thenReturn(7L);
        when(노량진.getId()).thenReturn(8L);

        when(영등포.getId()).thenReturn(9L);
        when(신길.getId()).thenReturn(10L);
        when(대방.getId()).thenReturn(11L);
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
