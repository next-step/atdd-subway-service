package nextstep.subway.utils;

public class IdTransferObject {
    Long id;

    public IdTransferObject() {
    }

    public IdTransferObject(Long id) {
        this.id = id;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
