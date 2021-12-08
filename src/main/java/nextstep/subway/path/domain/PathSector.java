package nextstep.subway.path.domain;

public enum PathSector {
    SECOND(50, 8, 100),
    FIRST(10, 5, 100);

    private final int startDistance;
    private final int partDistance;
    private final int partPrice;

    PathSector(int startDistance, int partDistance, int partPrice) {
        this.startDistance = startDistance;
        this.partDistance = partDistance;
        this.partPrice = partPrice;
    }

    public static int getOverPrice(int distance) {
        int result = 0;

        for (PathSector pathSector : values()) {
            int overDistance = Math.max(distance - pathSector.startDistance, 0);
            distance -= overDistance;

            result += calculateSectorPrice(pathSector, overDistance);
        }

        return result;
    }

    private static int calculateSectorPrice(PathSector pathSector, int overDistance) {
        if (overDistance == 0) {
            return 0;
        }

        return (((overDistance - 1) / pathSector.partDistance) + 1) * pathSector.partPrice;
    }
}
