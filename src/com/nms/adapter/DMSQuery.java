/**
 * 
 */
package com.nms.adapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.nms.model.DMSInfo;
import com.nms.model.ResponseBase;

/**
 * @author Hung
 * 
 */
public class DMSQuery
{
	public static ResponseBase login(String username, String password, String tranId, String host, int timeout) throws Exception
	{
		ResponseBase resp = new ResponseBase();
		try
		{
			URL url = new URL(host);
			String hostName = url.getHost();
			String path = url.getPath();
			int port = url.getPort();
			if (port <= 0)
				port = 80;

			String response = doRequestLogin(hostName, port, path,
					username, password, tranId, timeout);

			Document doc = parseResponse(response);

			Element parameterElement = (Element) doc.getElementsByTagName("parameter").item(0);
			// System.out.println(parameterElement.getTextContent());
			Element responseCodeElement = (Element) doc.getElementsByTagName("responseCode").item(0);
			// System.out.println(responseCodeElement.getTextContent());
			Element responseDescElement = (Element) doc.getElementsByTagName("responseDesc").item(0);
			// System.out.println(responseDescElement.getTextContent());
			Element sessionIdElement = (Element) doc.getElementsByTagName("sessionId").item(0);
			// System.out.println(sessionIdElement.getTextContent());
			Element transactionIdElement = (Element) doc.getElementsByTagName("transactionId").item(0);
			// System.out.println(transactionIdElement.getTextContent());

			resp.setParameter(parameterElement.getTextContent());
			resp.setResponseCode(responseCodeElement.getTextContent());
			resp.setResponseDesc(responseDescElement.getTextContent());
			resp.setSessionId(sessionIdElement.getTextContent());
			resp.setTransactionId(transactionIdElement.getTextContent());

		}
		catch (Exception e)
		{
			throw e;
		}

		return resp;
	}

	public static ResponseBase survey(String username, String host, int timeout, DMSInfo info, String sessionId)
	{
		ResponseBase resp = new ResponseBase();
		try
		{
			URL url = new URL(host);
			String hostName = url.getHost();
			String path = url.getPath();
			int port = url.getPort();
			if (port <= 0)
				port = 80;

			String response = doRequestSurvey(hostName, port, path, info, sessionId, username, timeout);

			Document doc = parseResponse(response);

			Element parameterElement = (Element) doc.getElementsByTagName("parameter").item(0);
			// System.out.println(parameterElement.getTextContent());
			Element responseCodeElement = (Element) doc.getElementsByTagName("responseCode").item(0);
			// System.out.println(responseCodeElement.getTextContent());
			Element responseDescElement = (Element) doc.getElementsByTagName("responseDesc").item(0);
			// System.out.println(responseDescElement.getTextContent());
			Element sessionIdElement = (Element) doc.getElementsByTagName("sessionId").item(0);
			// System.out.println(sessionIdElement.getTextContent());
			Element transactionIdElement = (Element) doc.getElementsByTagName("transactionId").item(0);
			// System.out.println(transactionIdElement.getTextContent());

			resp.setParameter(parameterElement.getTextContent());
			resp.setResponseCode(responseCodeElement.getTextContent());
			resp.setResponseDesc(responseDescElement.getTextContent());
			resp.setSessionId(sessionIdElement.getTextContent());
			resp.setTransactionId(transactionIdElement.getTextContent());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resp;
	}

	private static String doRequestSurvey(String host, int port, String path, DMSInfo info, String sessionId,  String username,
			int timeout) throws IOException
	{
		StringBuilder strXml = new StringBuilder();

		/**
		 * Append xmlData
		 */
		strXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strXml.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dms=\"http://dms.webservice.crm.com\">");
		strXml.append("<soapenv:Header/>");
		strXml.append("<soapenv:Body>");
		strXml.append("<dms:survey>");
		strXml.append("<dms:in0>");
		strXml.append("<dms:callerId>" + info.getCid() + "</dms:callerId>");
		strXml.append("<dms:checkStocksAndItems>" + info.getCheckStocks() + "</dms:checkStocksAndItems>");
		strXml.append("<dms:coverageIssue>" + info.getCoverageIssue() + "</dms:coverageIssue>");
		strXml.append("<dms:createOutletIssue>" + info.getCreateOutletIssue() + "</dms:createOutletIssue>");
		strXml.append("<dms:dataSpeedIssue>" + info.getDataSpeedIssue() + "</dms:dataSpeedIssue>");
		strXml.append("<dms:errorDevice>" + info.getErrorDevice() + "</dms:errorDevice>");
		strXml.append("<dms:imageData>" + info.getImage() + "</dms:imageData>");
		strXml.append("<dms:latitude>" + info.getLat() + "</dms:latitude>");
		strXml.append("<dms:locationAreaCode>" + info.getLac() + "</dms:locationAreaCode>");
		strXml.append("<dms:lockHandsetIssue>" + info.getLockHandsetIssue() + "</dms:lockHandsetIssue>");
		strXml.append("<dms:loginStatus>" + info.getLoginStatus() + "</dms:loginStatus>");
		strXml.append("<dms:longitude>" + info.getLon() + "</dms:longitude>");
		strXml.append("<dms:operator>" + info.getOperator() + "</dms:operator>");
		strXml.append("<dms:publicServiceCommission>" + info.getPsc() + "</dms:publicServiceCommission>");
		strXml.append("<dms:radioNetworkControl>" + info.getRnc() + "</dms:radioNetworkControl>");
		strXml.append("<dms:routePlanIssue>" + info.getRoutePlanIssue() + "</dms:routePlanIssue>");
		strXml.append("<dms:sessionId>" + sessionId + "</dms:sessionId>");
		strXml.append("<dms:signal>" + info.getSignal() + "</dms:signal>");
		strXml.append("<dms:smsIssue>" + info.getSmsIssue() + "</dms:smsIssue>");
		strXml.append("<dms:surveyCreateDate>" + info.getCreateDate() + "</dms:surveyCreateDate>");
		strXml.append("<dms:syncIssue>" + info.getSyncIssue() + "</dms:syncIssue>");
		strXml.append("<dms:transactionId>" + info.getTranId() + "</dms:transactionId>");
		strXml.append("<dms:typeOfNetwork>" + info.getType() + "</dms:typeOfNetwork>");
		strXml.append("<dms:username>" + username + "</dms:username>");
		strXml.append("<dms:voiceIssue>" + info.getVoiceIssue() + "</dms:voiceIssue>");
		strXml.append("</dms:in0>");
		strXml.append("</dms:survey>");
		strXml.append("</soapenv:Body>");
		strXml.append("</soapenv:Envelope>");

		int length = strXml.length();
		/**
		 * Add Header
		 */
		StringBuilder sbContent = new StringBuilder();
		sbContent.append("POST " + path + " HTTP/1.1\r\n");
		sbContent.append("Accept-Encoding: gzip,deflate\r\n");
		sbContent.append("Content-Type: text/xml;charset=UTF-8\r\n");
		// sbContent.append("Accept: application/soap+xml, application/dime, multipart/related, text/*\r\n");
		// sbContent.append("Cache-Control: no-cache\r\n");
		// sbContent.append("Pragma: no-cache\r\n");
		sbContent.append("SOAPAction: \"\"\r\n");
		sbContent.append("Content-Length: " + length + "\r\n");
		sbContent.append("Host: " + host + ":" + port + "\r\n");
		sbContent.append("Connection: Keep-Alive\r\n");
		sbContent.append("User-Agent: Apache-HttpClient/4.1.1 (java 1.5)\r\n");

		sbContent.append("\r\n");
		sbContent.append(strXml);

		return doRequest(host, port, sbContent.toString(), timeout);
	}

	private static String doRequestLogin(String host, int port,
				String path, String username, String password, String tranId, int timeout) throws IOException
	{
		StringBuilder strXml = new StringBuilder();

		/**
		 * Append xmlData
		 */
		strXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strXml.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dms=\"http://dms.webservice.crm.com\">");
		strXml.append("<soapenv:Header/>");
		strXml.append("<soapenv:Body>");
		strXml.append("<dms:login><dms:in0>");
		strXml.append("<dms:password>" + password + "</dms:password>");
		strXml.append("<dms:transactionId>" + tranId + "</dms:transactionId>");
		strXml.append("<dms:username>" + username + "</dms:username>");
		strXml.append("</dms:in0></dms:login>");
		strXml.append("</soapenv:Body>");
		strXml.append("</soapenv:Envelope>");

		int length = strXml.length();
		/**
		 * Add Header
		 */
		StringBuilder sbContent = new StringBuilder();
		sbContent.append("POST " + path + " HTTP/1.1\r\n");
		sbContent.append("Accept-Encoding: gzip,deflate\r\n");
		sbContent.append("Content-Type: text/xml;charset=UTF-8\r\n");
		// sbContent.append("Accept: application/soap+xml, application/dime, multipart/related, text/*\r\n");
		// sbContent.append("Cache-Control: no-cache\r\n");
		// sbContent.append("Pragma: no-cache\r\n");
		sbContent.append("SOAPAction: \"\"\r\n");
		sbContent.append("Content-Length: " + length + "\r\n");
		sbContent.append("Host: " + host + ":" + port + "\r\n");
		sbContent.append("Connection: Keep-Alive\r\n");
		sbContent.append("User-Agent: Apache-HttpClient/4.1.1 (java 1.5)\r\n");

		sbContent.append("\r\n");
		sbContent.append(strXml);

		return doRequest(host, port, sbContent.toString(), timeout);
	}

	private static String doRequest(String host, int port, String content,
				int timeout) throws IOException
	{
		Socket sock = null;
		try
		{
			InetAddress addrIP = InetAddress.getByName(host);
			sock = new Socket(addrIP, port);
			if (timeout > 0)
			{
				sock.setSoTimeout(timeout);
			}

			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream(), "UTF-8"));

			wr.write(content);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			String response = "";
			String line;

			while ((line = rd.readLine()) != null)
			{
				response += line + "\r\n";
				if (line.equalsIgnoreCase("0"))
				{
					sock.close();
					break;
				}
			}

			/**
			 * remove last \r\n
			 */
			response = response.substring(0, response.length() - 2);
			return response;
		}
		finally
		{
			if (sock != null)
			{
				try
				{
					sock.close();
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	private static Document parseResponse(String response)
			throws ParserConfigurationException, SAXException, IOException
	{
		String xmlContent = response;
		Document document = null;

		// System.out.println("<soap:Envelope: "
		// + xmlContent.indexOf("<soap:Envelope"));

		xmlContent = xmlContent.substring(response.indexOf("<soap:Envelope"));

		xmlContent = xmlContent.replace("\n0", "");

		xmlContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xmlContent;

		// System.out.println(xmlContent);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();

		document = builder.parse(new InputSource(new StringReader(xmlContent)));

		NodeList nodeList = document.getElementsByTagName("soap:Fault");

		if (nodeList != null)
		{
			if (nodeList.getLength() > 0)
			{
				AxisFault fault = null;

				try
				{
					fault = new AxisFault();
					Element element = (Element) nodeList.item(0);
					String faultString = getTextValue(element, "faultstring");
					String detail = element.getElementsByTagName("detail")
							.item(0).getTextContent();

					fault.setFaultString(faultString);
					fault.setFaultDetailString(detail);

				}
				catch (Exception e)
				{
					fault = new AxisFault();
					fault.setFaultString("CGW Internal Server Error");
					fault.setFaultDetailString(nodeList.item(0)
							.getTextContent());
				}

				throw fault;
			}
		}

		return document;
	}

	private static String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0)
		{
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
}
