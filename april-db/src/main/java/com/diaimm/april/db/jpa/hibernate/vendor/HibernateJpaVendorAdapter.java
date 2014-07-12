package com.diaimm.april.db.jpa.hibernate.vendor;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: coupang
 * Date: 2014. 7. 13.
 * Time: 오전 12:28
 * To change this template use File | Settings | File Templates.
 */
public class HibernateJpaVendorAdapter extends org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter {
    private Map<String, Object> hibernateProperties = Maps.newHashMap();

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> jpaPropertyMap = super.getJpaPropertyMap();
        jpaPropertyMap.putAll(this.hibernateProperties);
        return jpaPropertyMap;
    }

    public Map<String, Object> getHibernateProperties() {
        return hibernateProperties;
    }

    public void setHibernateProperties(Map<String, Object> hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }
}
