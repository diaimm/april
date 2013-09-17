/*
 * @fileName : Cachable.java
 * @date : 2013. 7. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.ehcache.store.MemoryStoreEvictionPolicy.MemoryStoreEvictionPolicyEnum;

/**
 * @author diaimm
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EhCacheConfiguration {
	String name() default "";

	long maxElementsInMemory() default 0L;

	boolean eternal() default false;

	long timeToIdleSeconds() default 0;

	long timeToLiveSeconds() default 0;

	boolean overflowToDisk() default false;

	boolean diskPersistent() default false;

	long diskExpiryThreadIntervalSeconds() default 120;

	MemoryStoreEvictionPolicyEnum memoryStoreEvictionPolicy = MemoryStoreEvictionPolicyEnum.LRU;
}
