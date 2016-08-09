package vision;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import config.VisionConfig;

public class XMLParser {
	VisionStruct vs = new VisionStruct();
	
	public VisionStruct parseString(String xml) {
		
		try {
			Document doc = stringToDoc(xml);
			doc.getDocumentElement().normalize();
	
			NodeList goalList = doc.getElementsByTagName("goal");
	
			for (int i = 0; i < goalList.getLength(); i++) {
	
				Element goalElement = (Element) goalList.item(i);
				
				vs.goals[i].update(goalElement);
	
			}
			
			for(int i = goalList.getLength(); i < VisionConfig.numberOfGoals; i++) {
				vs.goals[i].update();
			}
			
			vs.frameNumber = Integer.parseInt(((Element) doc.getElementsByTagName("Vision").item(0)).getAttribute("frameNumber"));
	
		} catch (Exception e) {
//			e.printStackTrace();
		}

		return vs;
	}

	public static Document stringToDoc(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}
}
