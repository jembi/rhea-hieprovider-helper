package org.openmrs.module.hieproviderhelper.api.impl;

import java.io.FileOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class SoapRequestGenerator {

	/**
	 * @param args
	 */
	public void generateRequest() {
		try{
			MessageFactory factory = MessageFactory.newInstance();
			SOAPMessage soapMsg = factory.createMessage();
			SOAPPart part = soapMsg.getSOAPPart();

			SOAPEnvelope envelope = part.getEnvelope();
			SOAPHeader header = envelope.getHeader();
			SOAPBody body = envelope.getBody();
			System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
			header.addTextNode("Training Details");

			SOAPBodyElement element = body.addBodyElement(envelope.createName("JAVA", "training", "http://shivasoft.in/blog"));
			element.addChildElement("WS").addTextNode("Training on Web service");

			SOAPBodyElement element1 = body.addBodyElement(envelope.createName("JAVA", "training", "http://shivasoft.in/blog"));
			element1.addChildElement("Spring").addTextNode("Training on Spring 3.0");

			soapMsg.writeTo(System.out);

			FileOutputStream fOut = new FileOutputStream("SoapMessage.xml");
			soapMsg.writeTo(fOut);

			System.out.println();
			System.out.println("SOAP msg created");

		}catch(Exception e){
			e.printStackTrace();
		}

	}

}