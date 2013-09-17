/*
 * @fileName : AsyncUpdatableCacheManager.java
 * @date : 2013. 7. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.common.cache;

import java.util.Collection;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * @author diaimm
 * 
 */
public class AsyncUpdatableSpringEhCacheCacheManager extends EhCacheCacheManager {
	private DirtyDataHolder dirtyDataHolder = new DirtyDataHolder();

	/**
	 * @see org.springframework.cache.ehcache.EhCacheCacheManager#loadCaches()
	 */
	@Override
	protected Collection<Cache> loadCaches() {
		Collection<Cache> loadedCaches = super.loadCaches();

		addOtherCaches(loadedCaches);
		return loadedCaches;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			// check the EhCache cache again
			// (in case the cache was added at runtime)
			Ehcache nativeCache = this.getCacheManager().getEhcache(name);
			if (nativeCache != null) {
				cache = wrapEhcache(nativeCache);
				addCache(cache);
			}
		}
		return cache;
	}

	/**
	 * @see org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager#decorateCache(org.springframework.cache.Cache)
	 */
	@Override
	protected Cache decorateCache(Cache cache) {
		return wrapEhcache((Ehcache) cache.getNativeCache());
	}

	/**
	 * @param ehcache
	 * @return
	 */
	private Cache wrapEhcache(Ehcache ehcache) {
		ehcache.setCacheManager(this.getCacheManager());
		return new EhCacheCache(new AsyncUpdatableEhCache(dirtyDataHolder, ehcache));
	}

	/**
	 * @param loadedCaches
	 */
	private void addOtherCaches(Collection<Cache> loadedCaches) {
		// @EhCacheConfiguration 에 의한 추가 로딩을 수행한다.
	}
}
