package org.openmrs.module.hieproviderhelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.hieproviderhelper.hpdmessage.HPDClient;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ProviderValidationTask extends AbstractTask {
	
	// Logger 
	private Log log = LogFactory.getLog(ProviderValidationTask.class);
	
	public void execute() {
		try {
			// Authenticate
			if (!Context.isAuthenticated()) {
				authenticate();
			}
			
			List<Provider> providerList = Context.getProviderService().getAllProviders();
			List<String> providerIds = new ArrayList<String>();
			
			for(Provider provider : providerList){
				String providerId = provider.getPerson().getAttribute("EPID").getValue();
				
				if(providerId == null){
					providerId = provider.getIdentifier();
				}
				
				providerIds.add(providerId);
			}
			
			// Send alert notifications to users who have unread alerts
			HPDClient hpdClient = new HPDClient();
			hpdClient.createHPDRequest(providerIds);
		}
		catch (Exception e) {
			log.error(e);
		}
	}

}
