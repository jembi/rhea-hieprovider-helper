package org.openmrs.module.hieproviderhelper.dwr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.web.dwr.DWREncounterService;

public class DWRProviderServiceXtra {
	
	
private final Log log = LogFactory.getLog(DWREncounterService.class);
	
	/**
	 * Finds providers
	 * 
	 * @param name to search
	 * @param includeRetired true/false whether or not to included retired providers
	 * @param start starting index for the results to return
	 * @param length the number of results to return
	 * @return a list of matching providers
	 * @should return a message with no matches found when no providers are found
	 * @should return the list of providers matching the search name
	 * @should return the list of providers including retired providers for the matching search name
	 */
	public Vector<Object> findProvider(String name, boolean includeRetired, Integer start, Integer length) {
		Vector<Object> providerListItem = new Vector<Object>();
		List<Provider> providerList = Context.getProviderService().getProviders(name, start, length, null);
		
		if (providerList.size() == 0) {
			MessageSourceService mss = Context.getMessageSourceService();
			providerListItem.add(mss.getMessage("Provider.noMatchesFound", new Object[] { name }, Context.getLocale()));
		} else {
			for (Provider p : providerList) {
				if (!p.isRetired() || (p.isRetired() && includeRetired == true))
					providerListItem.add(new ProviderListItemXtra(p));
			}
		}
		return providerListItem;
	}
	
	/**
	 * Finds providers for the given name along with the total count of providers that matched.
	 * 
	 * @param name to search
	 * @param includeRetired true/false whether or not to included retired providers
	 * @param start starting index for the results to return
	 * @param length the number of results to return
	 * @return a list of matching providers
	 * @throws APIException on any errors while searching providers
	 * @should return the count of all providers matching the searched name along with provider list
	 *         for given length
	 */
	public Map<String, Object> findProviderCountAndProvider(String name, boolean includeRetired, Integer start,
	        Integer length) throws APIException {
		Map<String, Object> providerMap = new HashMap<String, Object>();
		Vector<Object> objectList = findProvider(name, includeRetired, start, length);
		try {
			providerMap.put("count", Context.getProviderService().getCountOfProviders(name));
			providerMap.put("objectList", objectList);
		}
		catch (Exception e) {
			log.error("Error while searching for providers", e);
			objectList.clear();
			objectList.add(Context.getMessageSourceService().getMessage("Provider.search.error") + " - " + e.getMessage());
			providerMap.put("count", 0);
			providerMap.put("objectList", objectList);
		}
		return providerMap;
	}
	
	/**
	 * Get the provider identified by <code>providerId</code>
	 * 
	 * @param providerId
	 * @return
	 */
	public ProviderListItemXtra getProvider(Integer providerId) {
		return new ProviderListItemXtra(Context.getProviderService().getProvider(providerId));
	}
	
	public void validate(){
		System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	}

}
