package ca.mcgill.music.ddmal.meisearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ca.mcgill.music.ddmal.mei.MeiDocument;

public class XPathDocSearcher implements DocSearcher {

    private String expression;

    public XPathDocSearcher(String query) {
        // Make from query
        // expression =
        // "//note[@pname=\"a\"]/following::note[@pname=\"b\"]/following::note[@pname=\"c\"]";
        expression = "//mei:note";
    }

    // Can't use because it takes an MeiDocument
    public List<Response> find(MeiDocument doc, String query) {
        return null;
    }

    public List<Response> findXpath(InputStream stream) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
        xmlFact.setNamespaceAware(true);
        DocumentBuilder builder = xmlFact.newDocumentBuilder();
        Document doc = builder.parse(stream);

        NamespaceContext ctx = new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals("mei")) {
                    uri = "http://www.music-encoding.org/ns/mei";
                } else {
                    uri = null;
                }
                return uri;
            }

            // Dummy implemenation - not used!
            public String getPrefix(String uri) {
                return null;
            }

            public Iterator getPrefixes(String arg0) {
                return null;
            }
        };

        InputSource inputSource = new InputSource(stream);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(ctx);
        List<Response> res = new ArrayList<Response>();
        try {
            String x = xpath.evaluate(expression, doc);
            System.out.println(x);
            // nodes.item(0).getLocalName();
            // res.add(makeResponse(nodes));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Response makeResponse(NodeList nodes) {
        return null;
    }

}
