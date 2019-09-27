package com.baison.e3plus.basebiz.order.service.h2;

import org.h2.engine.Constants;
import org.h2.store.fs.FileUtils;
import org.h2.tools.RunScript;
import org.h2.util.IOUtils;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.SQLException;

/**
 *
 * liang.zeng
 *
 */
public class H2Utils {

    /**
     * 初始化数据库表结构
     * @param dataSource
     */
    public static void initSchema(DataSource dataSource){
        InputStream in = null;
        try {
            String schema = "classpath:db/schema.sql";
            in =  FileUtils.newInputStream(schema);
            String path = FileUtils.getParent(schema);
            in = new BufferedInputStream(in, Constants.IO_BUFFER_SIZE);
            Reader reader = new InputStreamReader(in, "UTF-8");
            RunScript.execute(dataSource.getConnection(), reader);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeSilently(in);
        }
    }

    /**
     * 测试H2语法
     * @param args
     * @throws SQLException
     */
    /*public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:mem:at_mw_stock_test";
        String filename = "classpath:db/temp.sql";
        System.out.println("h2 execute start........ ");
        RunScript.execute(url,"sa","sa",filename, Charset.forName("UTF-8"),false);
        System.out.println("h2 execute end........ ");
    }*/
}
