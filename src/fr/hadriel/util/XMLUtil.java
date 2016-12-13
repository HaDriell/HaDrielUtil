package fr.hadriel.util;

import org.w3c.dom.Node;

/**
 * Created by glathuiliere setOn 12/12/2016.
 */
public class XMLUtil {

    public static int getAttribute(Node node, String attribute, int defaultValue) {
        Node attribNode = node.getAttributes().getNamedItem(attribute);
        return attribNode == null ? defaultValue : Integer.parseInt(attribNode.getNodeValue());
    }

    public static String getAttribute(Node node, String attribute, String defaultValue) {
        Node attribNode = node.getAttributes().getNamedItem(attribute);
        return attribNode == null ? defaultValue : attribNode.getNodeValue();
    }
}