<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="View Providers" otherwise="/login.htm"
	redirect="/index.htm" />
	
<openmrs:htmlInclude file="/dwr/interface/DWRProviderServiceXtra.js"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables_jui.css"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js"/>
<openmrs:htmlInclude file="/scripts/jquery-ui/js/openmrsSearch.js" />

<script type="text/javascript">
	var lastSearch;
	
	$j(document).ready(
	function() {
		new OpenmrsSearch("findProvider", true, doProviderSearch, doSelectionHandler, 
				[	{fieldName:"displayName", header:omsgs.providerName},
					{fieldName:"identifier", header:omsgs.providerIdentifier},
				{fieldName:"registryStatus", header:"Provider Registry Status"}
				],	
                {
                    searchLabel: '<spring:message code="Provider.search" javaScriptEscape="true"/> ',
                    searchPlaceholder:'<spring:message code="Provider.search.placeholder" javaScriptEscape="true"/>',
                    doSearchWhenEmpty: true,
                    includeVoidedLabel: '<spring:message code="SearchResults.includeRetired" javaScriptEscape="true"/>'
                });
	});
	
	
	function doSelectionHandler(index, data) {
		document.location = "provider.form?providerId=" + data.providerId;
	}
	
	//searchHandler for the Search widget
	function doProviderSearch(text, resultHandler, getMatchCount, opts) {
		lastSearch = text;
		DWRProviderServiceXtra.findProviderCountAndProvider(text,opts.includeVoided,opts.start, opts.length,resultHandler);
	}

	function validate(){
		DWRProviderServiceXtra.validate();
	}
	
</script>
<br/>
<a href="provider.form"><spring:message code="Provider.add"/></a><p align="left">
<input type="submit" id="validateProvider" value='<spring:message code="hieproviderhelper.validate"/>' onclick="validate()"></p>
<div>
	<b class="boxHeader"><spring:message code="Provider.find"/></b>
	<div class="box">
		<div class="searchWidgetContainer" id="findProvider"> </div>
	</div>
</div>



<%@ include file="/WEB-INF/template/footer.jsp" %>