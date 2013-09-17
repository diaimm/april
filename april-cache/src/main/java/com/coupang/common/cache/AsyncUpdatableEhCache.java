/*
 * @fileName : AsyncUpdatableEhCache.java
 * @date : 2013. 7. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.common.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.util.CollectionUtils;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.concurrent.CacheLockProvider;
import net.sf.ehcache.concurrent.LockType;
import net.sf.ehcache.concurrent.Sync;
import net.sf.ehcache.constructs.EhcacheDecoratorAdapter;

/**
 * @author diaimm
 * 
 */
class AsyncUpdatableEhCache extends EhcacheDecoratorAdapter {
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static ExecutorCompletionService<Object> executionCompleteService = new ExecutorCompletionService<Object>(executor);
	private final DirtyDataHolder dirtyDataHolder;

	/**
	 * @param underlyingCache
	 */
	AsyncUpdatableEhCache(DirtyDataHolder dirtyDataHolder, Ehcache underlyingCache) {
		super(underlyingCache);
		this.dirtyDataHolder = dirtyDataHolder;
	}

	private Element parentGet(final Object key) throws IllegalStateException, CacheException {
		return super.get(key);
	}

	private Map<Object, Element> parentGetAll(Collection<?> keys) throws IllegalStateException, CacheException {
		return super.getAll(keys);
	}

	/**
	 * @see net.sf.ehcache.constructs.EhcacheDecoratorAdapter#get(java.lang.Object)
	 */
	@Override
	public Element get(final Object key) throws IllegalStateException, CacheException {
		final AsyncUpdatableEhCache callerInstance = this;
		Sync syncForKey = null;
		try {
			syncForKey = ((CacheLockProvider) getInternalContext()).getSyncForKey(key);

			// get내부에서 lock을 획득하지 못하면, dirty 데이터를 삭제하지 못하고 나온다.
			syncForKey.tryLock(LockType.WRITE, 1000);

			Element elementWithData = getDataByThread(key, callerInstance);
			// Element elementWithData = this.parentGet(key);

			return elementWithData;
		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
			if (syncForKey != null) {
				syncForKey.unlock(LockType.WRITE);
			}
		}
	}

	/**
	 * @param key
	 * @param callerInstance
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private Element getDataByThread(final Object key, final AsyncUpdatableEhCache callerInstance) throws InterruptedException, ExecutionException,
			TimeoutException {
		Future<Object> submit = executionCompleteService.submit(new Callable<Object>() {
			private AsyncUpdatableEhCache caller = callerInstance;

			@Override
			public Element call() throws Exception {
				return caller.parentGet(key);
			}
		});

		Element elementWithData = (Element) submit.get(60, TimeUnit.SECONDS);
		return elementWithData;
	}

	/**
	 * @see net.sf.ehcache.constructs.EhcacheDecoratorAdapter#get(java.io.Serializable)
	 */
	@Override
	public Element get(Serializable key) throws IllegalStateException, CacheException {
		return get(key);
	}

	/**
	 * @see net.sf.ehcache.constructs.EhcacheDecoratorAdapter#getAll(java.util.Collection)
	 */
	@Override
	public Map<Object, Element> getAll(final Collection<?> keys) throws IllegalStateException, CacheException {
		final AsyncUpdatableEhCache callerInstance = this;
		List<Sync> syncForKeys = new ArrayList<Sync>();
		try {
			for (Object key : keys) {
				Sync syncForKey = ((CacheLockProvider) getInternalContext()).getSyncForKey(key);
				// get내부에서 lock을 획득하지 못하면, dirty 데이터를 삭제하지 못하고 나온다.
				syncForKey.tryLock(LockType.WRITE, 1000);
				syncForKeys.add(syncForKey);
			}

			Future<Object> submit = executionCompleteService.submit(new Callable<Object>() {
				private AsyncUpdatableEhCache caller = callerInstance;

				@Override
				public Object call() throws Exception {
					return caller.parentGetAll(keys);
				}
			});

			@SuppressWarnings("unchecked")
			Map<Object, Element> elementWithData = (Map<Object, Element>) submit.get(60, TimeUnit.SECONDS);

			return elementWithData;
		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
			if (!CollectionUtils.isEmpty(syncForKeys)) {
				for (Sync syncForKey : syncForKeys) {
					syncForKey.unlock(LockType.WRITE);
				}
			}
		}
	}
}
