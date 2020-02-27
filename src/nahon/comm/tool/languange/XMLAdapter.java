/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.languange;

import java.io.*;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.*;

/**
 *
 * @author chejf
 */
public class XMLAdapter {

    private File xmlfile;
    private Document xmldoc;
    private int dataListNumber = 0;
    private final String dataListNumberName = "DataListNumber";
    private Element dataArea;
    private final String dataAreaName = "DataArea";

    public XMLAdapter(String xmlfilename){
        InitXMLAdapter(xmlfilename);
    }

    private void InitXMLAdapter(String xmlfilename) {
        this.xmlfile = new File(xmlfilename);
        String rootElementName = xmlfilename.substring(xmlfilename.lastIndexOf("/") + 1, xmlfilename.lastIndexOf("."));
        Element root = new Element(rootElementName);

        if (!xmlfile.exists()) {
            Element listnumber = new Element(dataListNumberName);
            listnumber.appendChild(String.valueOf(dataListNumber));
            root.appendChild(listnumber);

            root.appendChild(new Element(dataAreaName));

            xmldoc = new Document(root);
        } else {
            try {
                xmldoc = new Builder().build(xmlfile);
                root = xmldoc.getRootElement();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        }

        dataArea = root.getFirstChildElement(dataAreaName);
        dataListNumber = dataArea.getChildElements().size();
    }

    public int GetNodeNumber() {
        return this.dataListNumber;
    }

    public boolean GetNode(SaveAble datanode){
        Element element = dataArea.getFirstChildElement(datanode.MainKey());
        if (element == null) {
            return false;
        }
        try {
            this.XmlToData(datanode, element);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        } 
        return true;
    }

    public boolean IsContainNode(String Key) {
        return dataArea.getFirstChildElement(Key) == null ? false : true;
    }

    public void AddNode(SaveAble node) {
        Element element = dataArea.getFirstChildElement(node.MainKey());
        if (element != null) {
            return;
        }

        element = new Element(node.MainKey());
        this.DataToXml(node, element);
        dataArea.appendChild(element);
        this.dataListNumber++;
    }

    public void ModifyNode(SaveAble node) {
        Element element = dataArea.getFirstChildElement(node.MainKey());
        if (element == null) {
            return;
        }

        Element newelement = new Element(node.MainKey());
        this.DataToXml(node, newelement);
        dataArea.replaceChild(element, newelement);
    }

    public void AddNodeArray(SaveAble[] lists) {
        for (SaveAble list : lists) {
            this.AddNode(list);
        }
    }

    public void Clean() {
        dataArea.removeChildren();
        this.dataListNumber = 0;
    }

    public void GetNodeByIndex(SaveAble list, int index) throws Exception {
        if (index >= this.dataListNumber || list == null) {
            throw new Exception(String.format("Parameter Error: index = %d, nodeNum = %d, list = %u",
                    index, dataListNumber, list));
        }

        Elements datalist = dataArea.getChildElements();
        this.XmlToData(list, datalist.get(index));
    }

    public void Save() throws FileNotFoundException, IOException {
        Element number = new Element(dataListNumberName);
        number.appendChild(String.valueOf(dataListNumber));
        xmldoc.getRootElement().replaceChild(xmldoc.getRootElement().getFirstChildElement(dataListNumberName), number);

        Serializer serializer = new Serializer(new BufferedOutputStream(new FileOutputStream(xmlfile)), "UTF-8");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(xmldoc);
        serializer.flush();
    }

    public void DelNode(SaveAble node) {
        Element element = dataArea.getFirstChildElement(node.MainKey());
        if (null == element) {
            return;
        }
        dataArea.removeChild(element);
        this.dataListNumber--;
    }

    public boolean DelNode(int listnumber) {
        if (listnumber < GetNodeNumber()) {
            dataArea.removeChild(dataArea.getChildElements().get(listnumber));

            this.dataListNumber--;
            return true;
        } else {
            return false;
        }
    }

    private void XmlToData(SaveAble dataform, Element element) throws IllegalArgumentException, IllegalAccessException {
        Field[] datafields = dataform.getClass().getFields();
        dataform.SetMainKey(element.getLocalName());

        for (Field field : datafields) {
            Element node = element.getFirstChildElement(field.getName());
            if (field.getType() == int.class) {
                field.set(dataform, node == null ? 0 : Integer.valueOf(node.getValue()));
            } else {
                field.set(dataform, node == null ? "" : node.getValue());
            }
        }
    }

    private void DataToXml(SaveAble dataform, Element element) {
        Field[] datafields = dataform.getClass().getFields();

        for (Field field : datafields) {
            Element datanode = new Element(field.getName());
            try {
                datanode.appendChild(field.get(dataform).toString());
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                datanode.appendChild("");
            } finally {
                element.appendChild(datanode);
            }
        }
    }
}
