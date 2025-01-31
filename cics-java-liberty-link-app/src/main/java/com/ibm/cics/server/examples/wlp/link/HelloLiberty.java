Copyright check
All rights reserved (c)
/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */
package com.ibm.cics.server.examples.wlp.link;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

/**
 * Example enterprise Java program that can be invoked by EXEC CICS LINK.
 */
public class HelloLiberty
{
	/** The name of the enterprise Java CICS program this class provides. */
	private static final String PROGRAM_NAME = "HELLOWLP";

	/** The name of the container that contains the name to print. */
	private static final String NAME_CONTAINER = "NAME";

	/** The name of the JVM server this program is running on. */
	private static final String JVMSERVER_NAME = System.getProperty("com.ibm.cics.jvmserver.wlp.server.name");


	/** The CICS task the program is running on */
	private final Task currentTask;

	/** The user ID of the user running the CICS task */
	private final String taskUserId;


	/** A new instance of the class is created on every link request. */
	public HelloLiberty()
	{
		this.currentTask = Task.getTask();

		String taskUserId = "UNKNOWN";
		try
		{
			taskUserId = this.currentTask.getUSERID();
		}
		catch(CicsException e)
		{
			e.printStackTrace();
		}

		this.taskUserId = taskUserId;
	}

	/**
	 * Prints a greeting message to STDOUT.
	 * <p>
	 * When invoked with a text container called {@value #NAME_CONTAINER} on the 
	 * current channel, that name is used.
	 * <p>
	 * Otherwise, the user ID of the CICS task is used.
	 */
	@CICSProgram(PROGRAM_NAME) // This method can be invoked by EXEC CICS LINK PROGRAM(HELLOWLP)
	public void printMessage()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			final String name = getName();
			sb.append("Hello ")
					.append(name)
					.append(" from Liberty server ")
					.append(JVMSERVER_NAME);

			System.out.println(sb.toString());
		} catch (CicsException e)
		{
			e.printStackTrace();
			Task.getTask().abend("OHNO");
		}
	}

	/**
	 * @return The name to print. This will either be the String stored in the
	 *         container {@value #NAME_CONTAINER}, or the CICS task user ID is that
	 *         container does not exist.
	 * @throws CicsException if a CICS non-zero response is returned for channel and
	 *                       container interaction.
	 */
	private String getName() throws CicsException
	{
		Channel currentChannel = this.currentTask.getCurrentChannel();
		if (currentChannel == null)
		{
			return this.taskUserId;
		}

		Container nameContainer = currentChannel.getContainer(NAME_CONTAINER);
		if (nameContainer == null)
		{
			return this.taskUserId;
		}

		return nameContainer.getString();
	}
}
