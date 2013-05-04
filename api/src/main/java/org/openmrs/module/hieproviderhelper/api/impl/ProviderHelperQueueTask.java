
package org.openmrs.module.hieproviderhelper.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Implementation of a task that process all form entry queues.
 * 
 * NOTE: This class does not need to be StatefulTask as we create the context in
 * the constructor.
 * 
 * @author Justin Miranda
 * @version 1.0
 */
public class ProviderHelperQueueTask extends AbstractTask {

	// Logger
	private static Log log = LogFactory.getLog(ProviderHelperQueueTask.class);
	
	// Instance of form processor
	//private SoapRequestGenerator soapRequestGenerator = null;
	
	/**
	 * Default Constructor (Uses SchedulerConstants.username and
	 * SchedulerConstants.password
	 * 
	 */
	public ProviderHelperQueueTask() {
		System.out.println(" --------------------public ProviderHelperQueueTask()---------------------");
	}

	/**
	 * Process the next form entry in the database and then remove the form
	 * entry from the database.
	 */
	public void execute() {
		Context.openSession();
		log.debug("Processing form entry queue ... ");
		try {
			if (Context.isAuthenticated() == false)
				authenticate();
			SoapRequestGenerator soapRequestGenerator = new SoapRequestGenerator();
			soapRequestGenerator.generateRequest();
			
			System.out.println("------------------------processor.processFormEntryQueue();------------------------");
		} catch (APIException e) {
			log.error("Error running form entry queue task", e);
			throw e;
		} finally {
			Context.closeSession();
		}
	}
	
	/**
	 * Clean up any resources here
	 *
	 */
	public void shutdown() {
		System.out.println("Shutting down ProcessFormEntryQueue task ...");
		
	}

}