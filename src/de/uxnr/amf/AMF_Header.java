package de.uxnr.amf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.uxnr.amf.v0.AMF0_Type;
import de.uxnr.amf.v0.base.U32;
import de.uxnr.amf.v0.base.U8;
import de.uxnr.amf.v0.base.UTF8;

public class AMF_Header {
	private UTF8 headerName;
	private U8 mustUnderstand;
	private U32 headerLength;
	private AMF0_Type body;
	
	public AMF_Header(String headerName, boolean mustUnderstand, long headerLength) {
		this.headerName = new UTF8(headerName);
		this.mustUnderstand = new U8(mustUnderstand ? 1 : 0);
		this.headerLength = new U32(headerLength);
	}
	
	public AMF_Header(AMF_Context context, DataInputStream input) throws IOException {
		this.read(context, input);
	}
	
	public void write(AMF_Context context, DataOutputStream output) throws IOException {
		this.headerName.write(context, output);
		this.mustUnderstand.write(context, output);
		this.headerLength.write(context, output);
		AMF0_Type.writeType(context, output, this.body);
	}
	
	public void read(AMF_Context context, DataInputStream input) throws IOException {
		this.headerName = new UTF8(context, input);
		this.mustUnderstand = new U8(context, input);
		this.headerLength = new U32(context, input);
		this.body = AMF0_Type.readType(context, input);
	}
	
	public String getHeaderName() {
		return this.headerName.get();
	}
	
	public boolean getMustUnderstand() {
		return this.mustUnderstand.get() != 0;
	}
	
	public long getHeaderLength() {
		return this.headerLength.get();
	}
	
	public AMF0_Type getBody() {
		return this.body;
	}
	
	@Override
	public String toString() {
		String str = "AMF_Header\n";
		str += "\tHeaderName: "+this.headerName+"\n";
		str += "\tMustUnderstand: "+this.mustUnderstand+"\n";
		str += "\tHeaderLength: "+this.headerLength+"\n";
		str += this.body;
		return str;
	}
}
