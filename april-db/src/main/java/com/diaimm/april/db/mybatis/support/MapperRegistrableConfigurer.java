/*
 * @fileName : MapperRegistrableAdapter.java
 * @date : 2013. 6. 18.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.CollectionUtils;

import com.diaimm.april.db.mybatis.MapperScannerInitializer.BeanNamePostfixes;
import com.diaimm.april.db.mybatis.datasource.RoutingSqlSessionTemplate;

/**
 * Mapper를 등록해야하는 configurer를 위한 abstract class 입니다.
 * <p/>
 * 이 class를 상속한 bean은 mapper의 xml파일 경로를 지정하여 해당 mapper를 bean으로 등록할 수 있습니다.
 *
 * @author diaimm
 */
public abstract class MapperRegistrableConfigurer implements BeanFactoryPostProcessor {
	private List<String> dbQualifierIds;

	/**
	 * @param dbQualifierIds the dbQualifierIds to set
	 */
	public void setDbQualifierIds(List<String> dbQualifierIds) {
		this.dbQualifierIds = dbQualifierIds;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Map<Class<?>, MapperPosition> mapperPositions = new HashMap<Class<?>, MapperPosition>();
		if (CollectionUtils.isEmpty(dbQualifierIds)) {
			throw new BeanCreationException("dbQualifierIds must be set!!!!");
		}

		for (String dbQualifierId : dbQualifierIds) {
			List<Class<?>> mapperTypes = this.getMapperTypes(dbQualifierId);
			for (Class<?> mapperType : mapperTypes) {
				RoutingSqlSessionTemplate sqlSessionTemplate = beanFactory.getBean(BeanNamePostfixes.SESSION_TEMPLATE.fullName(dbQualifierId),
						RoutingSqlSessionTemplate.class);

				if (!sqlSessionTemplate.getConfiguration().hasMapper(mapperType)) {
					sqlSessionTemplate.getConfiguration().addMapper(mapperType);
				}
				mapperPositions.put(mapperType, new MapperPosition(dbQualifierId, mapperType, sqlSessionTemplate.getMapper(mapperType)));
			}
		}

		afterMapperIntialized(beanFactory, mapperPositions);
	}

	/**
	 * @param beanFactory
	 */
	public abstract void afterMapperIntialized(ConfigurableListableBeanFactory beanFactory, Map<Class<?>, MapperPosition> mapperPositions);

	/**
	 * mapper class들의 정보를 리턴합니다.
	 *
	 * @param configedDBQualifierId
	 * @return
	 */
	public abstract List<Class<?>> getMapperTypes(String configedDBQualifierId);

	public static class MapperPosition {
		private final Class<?> mapperClass;
		private final String datasourceId;
		private final Object instance;

		/**
		 * @param datasourceId : properties 설정에 사용한 qualifierId
		 * @param mapper       : repository(혹은 dao) 의 class, interface이며, mapper file(xml)은 동일 패키지에 있어야 합니다.
		 */
		private MapperPosition(String datasourceId, Class<?> mapper, Object instance) {
			this.datasourceId = datasourceId;
			this.mapperClass = mapper;
			this.instance = instance;
		}

		/**
		 * @return the mapper
		 */
		public Class<?> getMapperClass() {
			return mapperClass;
		}

		/**
		 * @return the datasourceId
		 */
		public String getDatasourceId() {
			return datasourceId;
		}

		/**
		 * @return the instance
		 */
		public Object getInstance() {
			return instance;
		}
	}
}