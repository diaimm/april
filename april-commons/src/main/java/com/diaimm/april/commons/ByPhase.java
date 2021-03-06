/*
 * @fileName : PropertiesHolder.java
 * @date : 2013. 6. 13.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons;

import com.diaimm.april.commons.property.PropertyKeyEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author diaimm
 * @version 2013.08.19
 * @see
 */
public class ByPhase implements ApplicationContextAware, InitializingBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private PropertiesHolder property;
    private ApplicationContext applicationContext;
    private Phase phase;
    private Set<String> basePackages;
    @Value("#{phase['profiles.active']}")
    private String phaseProperty;

    /**
     * 현재의 phase를 리턴합니다.
     *
     * @return
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * 현재 phase의 cookie post fix를 반환합니다.
     *
     * @return
     */
    public String getCookiePostFix() {
        return this.getPhase().getCookiePostFix();
    }

    /**
     * 프로퍼티 값을 반환합니다.
     *
     * @param key
     * @return
     */
    public <T extends Enum<T>> String getProperty(T key) {
        return this.property.getProperty(key);
    }

    /**
     * 프로퍼티 값(array)를 반환합니다.
     *
     * @param key
     * @return
     */
    public <T extends Enum<T>> String[] getProperties(T key) {
        return this.property.getProperties(key);
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @param basePackages the basePackages to set
     */
    public void setBasePackages(Set<String> basePackages) {
        this.basePackages = basePackages;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Application phase : {}", phaseProperty);

        this.phase = Phase.byProfile(phaseProperty);
        this.property = new PropertyLoader(this.phase, basePackages).load();
    }

    /**
     * phase 정의
     *
     * @author diaimm
     */
    public static enum Phase {
        LOCAL("LOCAL", "DV"), // 로그인 쿠키 관련
        DEV("DEV", "DV"), //
        STAGE("STAGE", "RL"), // 로그인 쿠키 관련
        REAL("REAL", "RL");
        private final String phaseName;
        private final String cookiePostFix;

        Phase(String phase, String cookiePostFix) {
            this.phaseName = phase;
            this.cookiePostFix = cookiePostFix;
        }

        /**
         * @return
         */
        private static Phase byProfile(String profile) {
            for (Phase phase : Phase.values()) {
                if (StringUtils.equalsIgnoreCase(phase.phaseName, profile)) {
                    return phase;
                }
            }

            // default는 LOCAL
            return LOCAL;
        }

        /**
         * @return the cookiePostFix
         */
        public String getCookiePostFix() {
            return cookiePostFix;
        }

        /**
         * @return the phaseName
         */
        public String getPhaseName() {
            return phaseName;
        }
    }

    /**
     * 프로퍼티 보관
     *
     * @author diaimm
     */
    private static class PropertiesHolder {
        private static final String PROPERTY_VALUE_SEPERATOR = ",";
        private Map<Class<?>, Properties> properties = new HashMap<Class<?>, Properties>();

        /**
         * @param properties
         */
        private void addProperties(Class<?> group, Properties properties) {
            this.properties.put(group, properties);
        }

        public <T extends Enum<T>> String getProperty(T key) {
            Properties properties = this.properties.get(key.getClass());
            if (properties == null) {
                return null;
            }

            String name = ((Enum<?>) key).name();
            if (properties.getProperty(name) != null) {
                return properties.getProperty(name);
            }
            return properties.getProperty(name.toLowerCase());
        }

        public <T extends Enum<T>> String[] getProperties(T key) {
            String property = getProperty(key);
            if (property == null) {
                return null;
            }

            return property.split(PROPERTY_VALUE_SEPERATOR);
        }
    }

    class PropertyLoader extends ClassPathScanningCandidateComponentProvider {
        private final Phase phase;
        private Set<String> basePackages = new HashSet<String>();

        private PropertyLoader(Phase phase, Set<String> basePackages) {
            super(false);
            this.phase = phase;

            if (basePackages != null) {
                this.basePackages.addAll(basePackages);
            }
        }

        private PropertiesHolder load() throws Exception {
            Map<String, BeanDefinition> candidateBeanDefinitions = getCandidateBeanDefinitions();
            return getPropertyHolder(candidateBeanDefinitions);
        }

        /**
         * @param candidateBeanDefinitions
         * @return
         * @throws IOException
         */
        private PropertiesHolder getPropertyHolder(Map<String, BeanDefinition> candidateBeanDefinitions) throws IOException {
            PropertiesHolder ret = new PropertiesHolder();
            for (Entry<String, BeanDefinition> benDefinitionEntry : candidateBeanDefinitions.entrySet()) {
                BeanDefinition beanDefinition = benDefinitionEntry.getValue();
                try {
                    String beanClassName = beanDefinition.getBeanClassName();
                    Class<?> annotatedClass = Class.forName(beanClassName);
                    String propertiesFileName = StringUtils.substringAfterLast(annotatedClass.getName(), ".");
                    // String propertiesFilePath = PropertyKeyEnum.FilePath.get(annotatedClass);

                    if (propertiesFileName != null) {
                        Properties properties = new Properties();

                        // property load, property 는 resource 경로에서 탐색한다. (profile 설정 적용)
                        loadProperties(beanClassName, annotatedClass, properties, null);
                        loadProperties(beanClassName, annotatedClass, properties, this.phase);

                        ret.addProperties(annotatedClass, properties);
                    }
                } catch (ClassNotFoundException e) {
                    // not happens
                }
            }
            return ret;
        }

        private void loadProperties(String beanClassName, Class<?> annotatedClass, Properties properties, Phase phase) throws IOException {
            Resource resource = getPropertyFileResource(properties, annotatedClass, phase);
            if (resource != null) {
                InputStream inputStream = resource.getInputStream();
                try {
                    properties.load(inputStream);
                } finally {
                    if (logger.isDebugEnabled()) {
                        logger.debug("property file is found at " + resource.getURI() + "...... mapping to " + beanClassName);
                    }

                    inputStream.close();
                }
            }
        }

        private Resource getPropertyFileResource(Properties props, Class<?> mappingEnumClass, Phase phase) {
            String filePath = PropertyKeyEnum.FilePath.get(mappingEnumClass);
            if (phase != null) {
                filePath += "-" + phase.name();
            }
            filePath += ".properties";

            Resource resource = applicationContext.getResource("classpath:" + filePath);
            if (resource.isReadable()) {
                return resource;
            }

            return null;
        }

        /**
         * @return
         */
        private Map<String, BeanDefinition> getCandidateBeanDefinitions() {
            PropertyKeyEnumRegistry beanDefinitionRegistry = new PropertyKeyEnumRegistry();
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
            scanner.addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    try {
                        Class<?> targetClass = Class.forName(classMetadata.getClassName());
                        PropertyKeyEnum annotation = targetClass.getAnnotation(PropertyKeyEnum.class);
                        return annotation != null;
                    } catch (ClassNotFoundException e) {
                    }
                    return false;
                }
            });

            String[] basePackages = new String[this.basePackages.size()];
            this.basePackages.toArray(basePackages);
            scanner.scan(basePackages);

            Map<String, BeanDefinition> candidates = beanDefinitionRegistry.candidates;
            return candidates;
        }

        private class PropertyKeyEnumRegistry implements BeanDefinitionRegistry {
            private Map<String, BeanDefinition> candidates = new HashMap<String, BeanDefinition>();

            /**
             * @see org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry#registerBeanDefinition(java.lang.String,
             *      org.springframework.beans.factory.config.BeanDefinition)
             */
            @Override
            public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
                try {
                    Class<?> targetClass = Class.forName(beanDefinition.getBeanClassName());
                    PropertyKeyEnum annotation = targetClass.getAnnotation(PropertyKeyEnum.class);
                    if (annotation != null) {
                        this.candidates.put(beanName, beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    throw new BeanDefinitionStoreException(e.getMessage(), e);
                }
            }

            /**
             * @see org.springframework.core.AliasRegistry#registerAlias(java.lang.String, java.lang.String)
             */
            @Override
            public void registerAlias(String name, String alias) {

            }

            /**
             * @see org.springframework.core.AliasRegistry#removeAlias(java.lang.String)
             */
            @Override
            public void removeAlias(String alias) {

            }

            /**
             * @see org.springframework.core.AliasRegistry#isAlias(java.lang.String)
             */
            @Override
            public boolean isAlias(String beanName) {

                return false;
            }

            /**
             * @see org.springframework.core.AliasRegistry#getAliases(java.lang.String)
             */
            @Override
            public String[] getAliases(String name) {

                return null;
            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#removeBeanDefinition(java.lang.String)
             */
            @Override
            public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {

            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#getBeanDefinition(java.lang.String)
             */
            @Override
            public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {

                return null;
            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#containsBeanDefinition(java.lang.String)
             */
            @Override
            public boolean containsBeanDefinition(String beanName) {

                return false;
            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#getBeanDefinitionNames()
             */
            @Override
            public String[] getBeanDefinitionNames() {

                return null;
            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#getBeanDefinitionCount()
             */
            @Override
            public int getBeanDefinitionCount() {

                return 0;
            }

            /**
             * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#isBeanNameInUse(java.lang.String)
             */
            @Override
            public boolean isBeanNameInUse(String beanName) {

                return false;
            }
        }
    }
}
