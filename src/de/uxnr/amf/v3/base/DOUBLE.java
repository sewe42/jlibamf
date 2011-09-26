package de.uxnr.amf.v3.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.uxnr.amf.AMF_Context;
import de.uxnr.amf.AMF_Type;
import de.uxnr.amf.v3.AMF3_Type;

public class DOUBLE extends AMF3_Type {
	private double value = 0;
	
	public DOUBLE() { }
	
	public DOUBLE(double value) {
		this.set(value);
	}
	
	public DOUBLE(AMF_Context context, DataInputStream input) throws IOException {
		this.read(context, input);
	}

	@Override
	public void write(AMF_Context context, DataOutputStream output) throws IOException {
		output.writeDouble(this.value);
	}

	@Override
	public AMF_Type read(AMF_Context context, DataInputStream input) throws IOException {
		this.value = input.readDouble();
		
		return this;
	}
	
	public void set(double value) {
		this.value = value;
	}
	
	public double get() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
	
	@Override
	public int hashCode() {
		return (int) this.value;
	}
}
