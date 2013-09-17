/*
 * @fileName : DirtyDataManager.java
 * @date : 2013. 7. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.cache;

/**
 * @author diaimm
 * 
 */
class DirtyDataHolder {
	/**
	 * @param asyncUpdatableEhCache
	 * @param key
	 * @return
	 */
	public boolean hasValue(AsyncUpdatableEhCache asyncUpdatableEhCache, Object key) {
		return false;
	}

	/**
	 * @param asyncUpdatableEhCache
	 * @param key
	 * @return
	 */
	public Object getValue(AsyncUpdatableEhCache asyncUpdatableEhCache, Object key) {
		return null;
	}
}
