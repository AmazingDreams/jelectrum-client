package nl.amazingsystems.electrum.responses;

public abstract class AbstractElectrumResponse {

    private long id;

    public long getId() {
	return this.id;
    }

    public void setId(long id) {
	this.id = id;
    }

}
