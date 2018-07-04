package cn.vpclub.pinganquan.mobile.base.config;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/5/19.
 */

@Component
public class MySQLUpperCaseStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = 1383021413247872460L;

    @Override
    public String classToTableName(String className) {
        return super.classToTableName(className).toUpperCase();
    }
}
