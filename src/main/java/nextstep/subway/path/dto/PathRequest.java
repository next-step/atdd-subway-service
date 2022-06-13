package nextstep.subway.path.dto;

public class PathRequest {

    public static class ShortestPath {
        private final Long source;
        private final Long target;

        public ShortestPath(Long source, Long target) {
            this.source = source;
            this.target = target;
        }
    }
}
