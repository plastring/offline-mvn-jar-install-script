package org.plastring;

import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String pomFilePath = System.getProperty("user.dir") + File.separator + args[0];
        System.out.println(pomFilePath);

        File pomFile = new File(pomFilePath);
        Document xmlDoc = null;
        try {
            xmlDoc = new SAXReader().read(pomFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element xmlRoot = xmlDoc.getRootElement();

        List<Element> childElements = xmlRoot.elements();

        List<Element> dependencies = null;
        for (Element ele : childElements) {
//            System.out.println(ele.getName());
            if("dependencies".equals(ele.getName())) {
                dependencies = ele.elements();
                break;
            }
        }

        for (Element ele: dependencies) {
            for (Object obj : ele.elements()) {
                Element element = (Element) obj;
                System.out.println(element.getName());
            }
        }
    }
}
