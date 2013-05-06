package org.openmrs.module.hieproviderhelper.dwr;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.ProviderAttribute;
import org.openmrs.ProviderAttributeType;
import org.openmrs.api.context.Context;

/**
 * 
 * @author magambo A modified org.openmrs.web.drw.ProviderListItem,added the
 *         registry status attribute
 * 
 */

public class ProviderListItemXtra {

	private String identifier;

	private String displayName;

	private Integer providerId;

	private String registryStatus;

	protected final Log log = LogFactory.getLog(getClass());

	private boolean retired = false;

	public ProviderListItemXtra(Provider provider) {
		Person person = provider.getPerson();
		if (person != null) {
			displayName = person.getPersonName().toString();
		} else {
			displayName = provider.getName();
		}
		identifier = provider.getIdentifier();
		providerId = provider.getProviderId();
		retired = provider.isRetired();
		int size = provider.getAttributes().size();
		boolean ifFound = false;

		for (ProviderAttribute pa : provider.getAttributes()) {
			ProviderAttributeType pat = pa.getAttributeType();
			String name = pat.getName();

			if (name.equals("Provider Registry Status")) {
				ifFound = true;
				List<ProviderAttribute> attrs = provider
						.getActiveAttributes(pat);
				setRegistryStatus(attrs.size() > 0 ? attrs.get(0).getValue()
						.toString() : "Not In Registry");
				break;
			}

		}
		if (!ifFound)
			setRegistryStatus("Not In Registry");
		else
			setRegistryStatus("Found");

		// ProviderAttributeType regStatusType =
		// Context.getProviderService().getProviderAttributeType(4);
		// List<ProviderAttribute> attrs =
		// provider.getActiveAttributes(regStatusType);
		// setRegistryStatus(attrs.size() > 0 ?
		// attrs.get(0).getValue().toString() :"Not In Registry");
	}

	/**
	 * @return the identifier of the provider
	 * @should return the identifier that is mentioned for the provider when a
	 *         person is not specified
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the display name for the provider
	 * @should return a display name based on whether provider has a person
	 *         associated
	 * @should return a display name based on provider name when person is not
	 *         associated
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the provider id
	 * @should return the provider id
	 */
	public Integer getProviderId() {
		return providerId;
	}

	/**
	 * @return the retired
	 */
	public boolean isRetired() {
		return retired;
	}

	/**
	 * @param retired
	 *            the retired to set
	 */
	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	public String getRegistryStatus() {
		return registryStatus;
	}

	public void setRegistryStatus(String registryStatus) {
		this.registryStatus = registryStatus;
	}

}
