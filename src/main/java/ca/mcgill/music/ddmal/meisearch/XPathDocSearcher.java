package ca.mcgill.music.ddmal.meisearch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.mcgill.music.ddmal.mei.MeiDocument;

public class XPathDocSearcher implements DocSearcher {

    private String expression;

    public XPathDocSearcher(String query) {
        // Make from query
        //expression = "//note[@pname=\"a\"]/following::note[@pname=\"b\"]/following::note[@pname=\"c\"]";
        expression = "//note[@pname=a]";
    }

    // Can't use because it takes an MeiDocument
    public List<Response> find(MeiDocument doc, String query) {
        return null;
    }

    public List<Response> findXpath(InputStream stream) {
        InputSource inputSource = new InputSource(stream);
        XPath xpath = XPathFactory.newInstance().newXPath();
        List<Response> res = new ArrayList<Response>();
        try {
            NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
            nodes.item(0).getLocalName();
            res.add(makeResponse(nodes));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Response makeResponse(NodeList nodes) {
        return null;
    }

}
