package com.project.normal.xml;

import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;

public interface GenericNamespaceContext extends NamespaceContext {
    void setDocument(Document document);
}
