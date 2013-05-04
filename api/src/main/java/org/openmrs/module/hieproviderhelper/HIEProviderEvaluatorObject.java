package org.openmrs.module.hieproviderhelper;

import org.openmrs.Provider;

public class HIEProviderEvaluatorObject {
	
	private Provider provider;
	private String NIDNumber;
	private boolean existsInHIE;
	
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public String getNIDNumber() {
		return NIDNumber;
	}
	public void setNIDNumber(String nIDNumber) {
		NIDNumber = nIDNumber;
	}
	public boolean isExistsInHIE() {
		return existsInHIE;
	}
	public void setExistsInHIE(boolean existsInHIE) {
		this.existsInHIE = existsInHIE;
	}

}
