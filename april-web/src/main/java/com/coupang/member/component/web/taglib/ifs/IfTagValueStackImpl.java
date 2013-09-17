package com.coupang.member.component.web.taglib.ifs;

/**
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public final class IfTagValueStackImpl implements IfTagValueStack {
	private static final int STACK_SIZE = 1024;
	private int stackSize = STACK_SIZE;
	private int index = 0;
	private Boolean[] valueStack = new Boolean[stackSize];

	public void push(Boolean value) {
		if (getDepth() + 1 > stackSize) {
			stackSize = stackSize * 2;

			Boolean[] newValueStack = new Boolean[stackSize];
			System.arraycopy(valueStack, 0, newValueStack, 0, valueStack.length);

			valueStack = newValueStack;
		}

		valueStack[index++] = value;
	}

	public Boolean pop() {
		if (getDepth() == 0) {
			return null;
		}

		Boolean popped = valueStack[--index];
		valueStack[index] = null;
		return popped;
	}

	public Integer getDepth() {
		return index;
	}
}
