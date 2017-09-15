package com.ivt.mis.common;

import java.io.IOException;
import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class IbatisUtil {

	private static SqlMapClient sqlMapClient = null;
    static {
        try {
            //加载配置文件
            Reader reader = Resources.getResourceAsReader("config/ibatis_config.xml");
            sqlMapClient=SqlMapClientBuilder.buildSqlMapClient(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IbatisUtil() {
    }

    public static SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }
    
    public static SqlMapClient createNewSqlMapClient() {
    	Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("config/ibatis_config.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return SqlMapClientBuilder.buildSqlMapClient(reader);
    }
}
