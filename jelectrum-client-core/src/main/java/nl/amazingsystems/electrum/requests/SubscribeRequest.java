package nl.amazingsystems.electrum.requests;

/**
 * Marks an ElectrumRequest as a 'subscribable' request, meaning that the
 * Electrum server will continue to push responses when they become available,
 * long after the request was send
 * 
 * @author dennis
 */
public interface SubscribeRequest extends ElectrumRequest {
}
