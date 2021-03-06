package de.uxnr.amf.v0.type;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.uxnr.amf.AMF_Context;
import de.uxnr.amf.AMF_Type;
import de.uxnr.amf.v0.AMF0_Type;

public abstract class EmptyType extends AMF0_Type {
	@Override
	public void write(AMF_Context context, DataOutputStream output) throws IOException {
		// Nothing to do here
	}

	@Override
	public AMF_Type read(AMF_Context context, DataInputStream input) throws IOException {
		// Nothing to do here
		return this;
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}
}
