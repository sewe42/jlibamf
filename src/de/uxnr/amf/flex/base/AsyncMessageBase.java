package de.uxnr.amf.flex.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.uxnr.amf.AMF_Context;
import de.uxnr.amf.flex.type.AbstractMessage;
import de.uxnr.amf.v3.base.UTF8;

public abstract class AsyncMessageBase extends AbstractMessage {
	private static final UTF8[][] names = new UTF8[][] {
		{
			new UTF8("correlationId"),
			new UTF8("correlationUuid"),
		}
	};

	@Override
	public void writeExternal(AMF_Context context, DataOutputStream output) throws IOException {
		super.writeExternal(context, output);

		this.writeFields(context, output, AsyncMessageBase.names);
	}

	@Override
	public void readExternal(AMF_Context context, DataInputStream input) throws IOException {
		super.readExternal(context, input);

		this.readFields(context, input, AsyncMessageBase.names);
	}
}
