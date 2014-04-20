/**
 * 
 */
package com.nms.model;

/**
 * @author Hung
 *
 */
public class ResponseBase
{
	String parameter = "";
	String responseCode = "";
	String responseDesc = "";
	String sessionId = "";
	String transactionId = "";
	public String getParameter()
	{
		return parameter;
	}
	public void setParameter(String parameter)
	{
		this.parameter = parameter;
	}
	public String getResponseCode()
	{
		return responseCode;
	}
	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;
	}
	public String getResponseDesc()
	{
		return responseDesc;
	}
	public void setResponseDesc(String responseDesc)
	{
		this.responseDesc = responseDesc;
	}
	public String getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	public String getTransactionId()
	{
		return transactionId;
	}
	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}
	
	
}
