/*
 * Copyright (C) 2021 denkbares GmbH, Germany
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */

package org.potassco.clingcon.api;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import org.potassco.clingo.symbol.Symbol;

import java.util.Arrays;
import java.util.List;

@Structure.FieldOrder({"type", "value"})
public class Value extends Structure {

	public int type;
	public ValueUnion value;

	public Value() {
		super();
	}

	public Value(int type, ValueUnion field1) {
		super();
		this.type = type;
		this.value = field1;
	}

	public Value(Pointer peer) {
		super(peer);
	}

	@Override
	public String toString() {
		switch (type) {
			case 0:
				return String.valueOf(value.int_number);
			case 1:
				return String.valueOf(value.double_number);
			case 2:
				return Symbol.fromLong(value.symbol).toString();
			default:
				throw new IllegalStateException("Unknown value type");
		}
	}

	public ValueType getType() {
		return ValueType.fromValue(type);
	}

	public ValueUnion getValue() {
		return value;
	}

	public static class ByReference extends Value implements Structure.ByReference { }

	public static class ByValue extends Value implements Structure.ByValue { }

	public static class ValueUnion extends Union {

		public int int_number;
		public double double_number;
		public long symbol;

		public ValueUnion() {
			super();
		}

		public ValueUnion(int int_number) {
			super();
			this.int_number = int_number;
			setType(Integer.TYPE);
		}

		public ValueUnion(double double_number) {
			super();
			this.double_number = double_number;
			setType(Double.TYPE);
		}

		public ValueUnion(long symbol) {
			super();
			this.symbol = symbol;
			setType(Long.TYPE);
		}

		public ValueUnion(Pointer peer) {
			super(peer);
		}

		public static class ByReference extends ValueUnion implements Structure.ByReference { }

		public static class ByValue extends ValueUnion implements Structure.ByValue { }
	}
}
