package com.mfz.yourbatis.config;

import com.mfz.yourbatis.datasource.MyDataSource;
import com.mfz.yourbatis.datasource.PooledDatasource;
import com.mfz.yourbatis.datasource.UnpooledDatasource;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLConfigrationBuilder {

    private boolean parsed;
    private SAXReader reader;
    private Configuration configuration;

    public XMLConfigrationBuilder() {
        this.parsed = false;
        this.reader = new SAXReader();
        configuration = new Configuration();
    }

    public Configuration build(String XMLPath){
        try {
            if (XMLPath == null || XMLPath.length() == 0) {
                throw new RuntimeException("Configuration Location should not be null");
            }
            if (!parsed) {
                parseConfiguration(reader.read(XMLPath).getRootElement());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return configuration;
    }

    public void parseConfiguration(Element rootElement){
        parsePropertiesElement(rootElement.element("properties"));
        parseDatasourceElement(rootElement.element("datasource"));

    }

    public void parsePropertiesElement(Element propertiesElement){
        String cacheType = propertiesElement.elementText("cache");
        configuration.setFirstCache(Boolean.valueOf(propertiesElement.elementText("cacheFirst")));
        configuration.setSecondCache(Boolean.valueOf(propertiesElement.elementText("cacheSecond")));
        // TODO 2020/08/12 解析未完成
    }

    public void parseDatasourceElement(Element datasourceElement){
        String type = datasourceElement.elementText("type");
        MyDataSource dataSource;
        if (type.equalsIgnoreCase("Pooled")){
            dataSource = new UnpooledDatasource(datasourceElement.elementText("jdbc.url"),datasourceElement.elementText("jdbc.driver"),datasourceElement.elementText("jdbc.userName"),datasourceElement.elementText("jdbc.passWord"));
        }else {
            dataSource = new PooledDatasource(datasourceElement.elementText("jdbc.url"),datasourceElement.elementText("jdbc.driver"),datasourceElement.elementText("jdbc.userName"),datasourceElement.elementText("jdbc.passWord"));
        }
        configuration.setDataSource(dataSource);
    }
}
