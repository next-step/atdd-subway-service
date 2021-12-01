package nextstep.subway.line.domain;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : CalculateType
 * author : haedoang
 * date : 2021-12-01
 * description :
 */
public enum DistanceType {
    PLUS, MINUS;

    public boolean isPlus() {
        return this == PLUS;
    }

    public boolean isMinus() {
        return this == MINUS;
    }

}
