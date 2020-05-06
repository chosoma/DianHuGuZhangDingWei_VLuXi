package com.thingtek.config;

import com.thingtek.beanServiceDao.base.service.BaseService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.*;

/*
    端口参数
 */
@Component
public class PortConfig extends BaseService {
    private String mcip;
    private int port;
    private int portank;

    public int getPortank() {
        return portank;
    }

    public String getMcip() {
        return mcip;
    }

    public void setMcip(String mcip) {
        this.mcip = mcip;
    }

    public int getPort() {
        return port;
    }


    private String filename = "NetConfig.xml";

    public PortConfig() {
        init();
    }

    private void init() {
        File configfile = new File(filename);
        if (!configfile.exists()) {
            initDefault();
            createConfigXML();
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(configfile);
                Element root = document.getRootElement();

                port = Integer.parseInt(root.element("serverport").getText());
                portank = Integer.parseInt(root.element("serverportank").getText());
                mcip = root.element("mcip").getText();
            } catch (Exception e) {
                initDefault();
                createConfigXML();
            }
        }
    }

    /**
     * 创建配置XML
     */
    private void createConfigXML() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("property");
        Element serverport = root.addElement("serverport");
        serverport.addText(String.valueOf(port));
        Element serverankport = root.addElement("serverankport");
        serverankport.addText(String.valueOf(portank));
        Element mcipelement = root.addElement("mcip");
        mcipelement.addText(mcip);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
            writer.write(document);
            writer.close();

        } catch (IOException e) {
            log(e);
        }
    }

    /**
     * 更新配置XML
     */
    public void refreshConfigXml() {

        File configfile = new File(filename);
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(configfile);
            Element root = document.getRootElement();
            Element mcipelement = root.element("mcip");
            mcipelement.setText(mcip);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
            writer.write(document);
            writer.close();

        } catch (DocumentException | IOException e) {
            log(e);
        }
    }

    /**
     * 设置默认值
     */
    private void initDefault() {
        port = 1024;
        portank = 3456;
        mcip = "192.168.1.250";
    }


}
