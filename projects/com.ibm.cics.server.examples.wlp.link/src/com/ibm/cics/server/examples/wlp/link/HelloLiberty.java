package com.ibm.cics.server.examples.wlp.link;

import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                   */       
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */
/**
 * Sample code to be invoked by EXEC CICS LINK.
 */
public class HelloLiberty {

	/**
	 * Prints a greeting message to STDOUT. When invoked with a text container
	 * called NAME on the current channel, that name is used. Otherwise, the
	 * user ID of the CICS task is used.
	 */
	@CICSProgram("HELLOWLP")  // This method can be invoked by EXEC CICS LINK PROGRAM(HELLOWLP)
	public void printMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hello ");
		try {
			Channel currentChannel = Task.getTask().getCurrentChannel();
			if (currentChannel != null && currentChannel.getContainer("NAME") != null) {
				Container nameContainer = currentChannel.getContainer("NAME");
				sb.append(nameContainer.getString());
			} else {
				sb.append(Task.getTask().getUSERID());
			}
			sb.append(" from Liberty server ");
			sb.append(System.getProperty("com.ibm.cics.jvmserver.wlp.server.name"));
			System.out.println(sb.toString());
		} catch (InvalidRequestException | ContainerErrorException
				| ChannelErrorException | CCSIDErrorException
				| CodePageErrorException e) {
			e.printStackTrace();
			Task.getTask().abend("OHNO");
		}
	}
}
