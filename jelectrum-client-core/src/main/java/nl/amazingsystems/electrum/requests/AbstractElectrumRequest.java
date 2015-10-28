package nl.amazingsystems.electrum.requests;

import com.google.gson.Gson;

import nl.amazingsystems.electrum.responses.AbstractElectrumResponse;

public abstract class AbstractElectrumRequest {

    private long id;

    private String method;

    private Object[] params;

    public AbstractElectrumRequest(String method) {
	this(method, new Object[0]);
    }

    public AbstractElectrumRequest(String method, Object[] params) {
	this.method = method;
	this.params = params;
    }

    public long getId() {
	return this.id;
    }

    public String getMethod() {
	return this.method;
    }

    public Object[] getParams() {
	return this.params;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public void setParams(Object[] params) {
	this.params = params;
    }

    @Override
    public String toString() {
	Gson gson = new Gson();
	return gson.toJson(this);
    }

    public abstract Class<? extends AbstractElectrumResponse> getResponseClass();

}