package nextstep.subway.path.domain;

import lombok.Getter;
import java.util.List;

@Getter
public class Path<T> {
    private final List<T> paths;
    private final double distance;

    public Path(final List<T> paths, final double distance) {
        this.paths = paths;
        this.distance = distance;
    }

    public int size() {
        return paths.size();
    }

    public T getSource() {
        return paths.get(0);
    }

    public T getTrget() {
        return paths.get(size() - 1);
    }
}
