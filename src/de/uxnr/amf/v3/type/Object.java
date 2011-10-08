package de.uxnr.amf.v3.type;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uxnr.amf.AMF_Context;
import de.uxnr.amf.AMF_Type;
import de.uxnr.amf.flex.Flex;
import de.uxnr.amf.v3.AMF3_Trait;
import de.uxnr.amf.v3.AMF3_Type;
import de.uxnr.amf.v3.base.U29;
import de.uxnr.amf.v3.base.UTF8;

public class Object extends AMF3_Type {
	private static final UTF8 EMPTY_KEY = new UTF8();

	private final Map<UTF8, AMF3_Type> value = new LinkedHashMap<UTF8, AMF3_Type>();

	private AMF3_Trait trait = null;
	private AMF3_Type external = null;

	private java.lang.Integer hashCode = null;

	public Object() { }

	public Object(AMF3_Type external) {
		this.external = external;
	}

	@Override
	public void write(AMF_Context context, DataOutputStream output) throws IOException {
		int flags = this.trait.getCount() << 4;
		flags |= 1 | 2 | (this.trait.isExternalizable() ? 4 : 0) | (this.trait.isDynamic() ? 8 : 0);

		U29 flag = new U29(flags);
		flag.write(context, output);

		UTF8 className = this.trait.getClassName();
		className.write(context, output);

		List<UTF8> names = this.trait.getNames();
		for (UTF8 name : names) {
			name.write(context, output);
		}

		if (this.trait.isExternalizable()) {
			Flex.writeMessage(context, output, className, this.external);
		} else {
			for (UTF8 name : names) {
				AMF3_Type.writeType(context, output, this.value.get(name));
			}

			if (this.trait.isDynamic()) {
				for (Entry<UTF8, AMF3_Type> entry : this.value.entrySet()) {
					if (!names.contains(entry.getKey())) {
						entry.getKey().write(context, output);
						AMF3_Type.writeType(context, output, entry.getValue());
					}
				}

				Object.EMPTY_KEY.write(context, output);
			}
		}
	}

	@Override
	public AMF_Type read(AMF_Context context, DataInputStream input) throws IOException {
		U29 flag = new U29(context, input);
		int flags = flag.get();

		if ((flags & 1) == 0)
			return context.getAMF3Object(flags >> 1);

		if ((flags & 2) == 0)
			this.trait = context.getAMF3Trait(flags >> 2);

		if (this.trait == null) {
			this.trait = new AMF3_Trait();
			this.trait.setExternalizable((flags & 4) == 4);
			this.trait.setDynamic((flags & 8) == 8);

			UTF8 className = new UTF8();
			className = (UTF8) className.read(context, input);
			this.trait.setClassName(className);

			int count = flags >> 4;
			for (int index = 0; index < count; index++) {
				UTF8 name = new UTF8();
				name = (UTF8) name.read(context, input);
				this.trait.addName(name);
			}

			context.addAMF3Trait(this.trait);
		}

		if (this.trait.isExternalizable()) {
			this.external = Flex.readMessage(context, input, this.trait.getClassName());
		} else {
			for (UTF8 name : this.trait.getNames()) {
				AMF3_Type value = AMF3_Type.readType(context, input);

				this.value.put(name, value);
			}

			if (this.trait.isDynamic()) {
				UTF8 key = null;
				AMF3_Type value = null;
				do {
					key = new UTF8();
					key = (UTF8) key.read(context, input);

					if (key.equals(Object.EMPTY_KEY)) {
						break;
					} else {
						value = AMF3_Type.readType(context, input);

						this.value.put(key, value);
					}
				} while (key != null && value != null);
			}
		}

		context.addAMF3Object(this);

		return this;
	}

	public Set<UTF8> keySet() {
		return this.value.keySet();
	}

	public Set<Entry<UTF8, AMF3_Type>> entrySet() {
		return this.value.entrySet();
	}

	public void put(UTF8 key, AMF3_Type value) {
		this.hashCode = null;
		this.value.put(key, value);
	}

	public void set(UTF8 key, AMF3_Type value) {
		this.put(key, value);
	}

	public void set(UTF8 key, AMF3_Type value, boolean dynamic) {
		if (!dynamic && !this.trait.hasName(key))
			this.trait.addName(key);

		this.put(key, value);
	}

	public AMF3_Type get(UTF8 key) {
		return this.value.get(key);
	}

	public java.lang.String getClassName() {
		return this.trait.getClassName().get();
	}

	public AMF3_Type getExternal() {
		return this.external;
	}

	@Override
	public java.lang.String toString() {
		if (this.external != null)
			return "External '" + this.getClassName() + "' " + this.external;
		return "Object '" + this.getClassName() + "' " + this.value;
	}

	@Override
	public int hashCode() {
		if (this.hashCode != null)
			return this.hashCode;
		this.hashCode = this.value.hashCode();
		if (this.trait != null)
			this.hashCode ^= this.trait.hashCode();
		if (this.external != null)
			this.hashCode ^= this.external.hashCode();
		return this.hashCode;
	}
}
