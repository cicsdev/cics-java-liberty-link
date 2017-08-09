package com.ibm.cics.server.examples.wlp.link;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.IOErrorException;
import com.ibm.cics.server.ISCInvalidRequestException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.InvalidSystemIdException;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NoSpaceException;
import com.ibm.cics.server.NotAuthorisedException;
import com.ibm.cics.server.NotOpenException;
import com.ibm.cics.server.Region;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;
import com.ibm.cics.server.TDQ;

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
			if (currentChannel != null && currentChannel.getContainer("NAME") != null) {
                                Container Output = currentChannel.createContainer("OUTPUT");
                                Output.putString(sb.toString());
                        }           
                                             
                        StringBuilder message = new StringBuilder();
                        Date timestamp = new Date();
                        SimpleDateFormat fm = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                        String datetime = fm.format(timestamp);
                                             
                        message.append("DFHEZRIEL ");
                        message.append(datetime);
                        message.append(" " + Region.getAPPLID());
                        message.append(" " + sb);
                                             
                        TDQ tdq = new TDQ();
                        tdq.setName(TDQName);
                        byte[] record = null;
                        try {
                                record = String.valueOf(message).getBytes(CCSID);
                        } catch (UnsupportedEncodingException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                                Task.getTask().abend("JNCJ");
                        }
                        try {
                            tdq.writeData(record);
                        } catch (IOErrorException | LengthErrorException 
                               | InvalidSystemIdException | ISCInvalidRequestException 
                               | NoSpaceException  | NotAuthorisedException | NotOpenException e) {
                            e.printStackTrace();
                            Task.getTask().abend("TDQJ");
                        }
		} catch (InvalidRequestException | ContainerErrorException
				| ChannelErrorException | CCSIDErrorException
				| CodePageErrorException e) {
			e.printStackTrace();
			Task.getTask().abend("OHNO");
		}
	}
}
