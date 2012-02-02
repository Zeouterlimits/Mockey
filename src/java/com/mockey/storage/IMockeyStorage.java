/*
 * This file is part of Mockey, a tool for testing application 
 * interactions over HTTP, with a focus on testing web services, 
 * specifically web applications that consume XML, JSON, and HTML.
 *  
 * Copyright (C) 2009-2010  Authors:
 * 
 * chad.lafontaine (chad.lafontaine AT gmail DOT com)
 * neil.cronin (neil AT rackle DOT com) 
 * lorin.kobashigawa (lkb AT kgawa DOT com)
 * rob.meyer (rob AT bigdis DOT com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.mockey.storage;

import java.util.Collection;
import java.util.List;

import com.mockey.model.FulfilledClientRequest;
import com.mockey.model.ProxyServerModel;
import com.mockey.model.Scenario;
import com.mockey.model.Service;
import com.mockey.model.ServicePlan;
import com.mockey.model.ServiceRef;
import com.mockey.model.TwistInfo;

/**
 * How Mockey stores itself.
 * 
 * @author chad.lafontaine
 * 
 */
public interface IMockeyStorage {

	public void setReadOnlyMode(Boolean transientState);
	
	public String getFilterTag();
	
	public Boolean getReadOnlyMode();
	
	public void deleteEverything();

	public List<TwistInfo> getTwistInfoList();

	public TwistInfo getTwistInfoById(Long id);

	public TwistInfo getTwistInfoByName(String name);

	public TwistInfo saveOrUpdateTwistInfo(TwistInfo twistInfo);

	public void deleteTwistInfo(TwistInfo twistInfo);

	public Service getServiceById(Long serviceId);

	public Service getServiceByUrl(String urlPath);

	public List<Long> getServiceIds();

	public List<Service> getServices();

	public Service saveOrUpdateService(Service service);

	public void deleteService(Service service);

	public ServiceRef saveOrUpdateServiceRef(ServiceRef serviceRef);

	public List<ServiceRef> getServiceRefs();

	public ServicePlan getServicePlanById(Long servicePlanId);

	public ServicePlan getServicePlanByName(String servicePlanName);

	public List<ServicePlan> getServicePlans();

	public ServicePlan saveOrUpdateServicePlan(ServicePlan servicePlan);

	public void deleteServicePlan(ServicePlan servicePlan);

	public Scenario getUniversalErrorScenario();

	public Long getUniversalTwistInfoId();

	public void setUniversalTwistInfoId(Long twistInfoId);

	public void setUniversalErrorServiceId(Long serviceId);

	public void setUniversalErrorScenarioId(Long scenarioId);

	public Long getUniversalErrorServiceId();

	public Long getUniversalErrorScenarioId();

	public ProxyServerModel getProxy();

	public void setProxy(ProxyServerModel proxy);

	public List<String> uniqueClientIPs();

	public List<String> uniqueClientIPsForService(Long serviceId);

	public List<FulfilledClientRequest> getFulfilledClientRequests();

	public FulfilledClientRequest getFulfilledClientRequestsById(Long fulfilledClientRequestId);

	public List<FulfilledClientRequest> getFulfilledClientRequestsFromIP(String ip);

	public List<FulfilledClientRequest> getFulfilledClientRequestsForService(Long serviceId);

	public List<FulfilledClientRequest> getFulfilledClientRequestsFromIPForService(String ip, Long serviceId);

	public List<FulfilledClientRequest> getFulfilledClientRequest(Collection<String> filterArguments);

	public void saveOrUpdateFulfilledClientRequest(FulfilledClientRequest requestResponseX);

	public void deleteFulfilledClientRequests();

	public void deleteFulfilledClientRequestById(Long fulfilledRequestID);

	public void deleteFulfilledClientRequestsFromIP(Long ip);

	public void deleteFulfilledClientRequestsForService(Long serviceId);

	public void deleteFulfilledClientRequestsFromIPForService(String ip, Long serviceId);

	
	/**
	 * Removes the tag from all Services and Scenarios
	 * 
	 * @param tag - a space delimited argument. 
	 */
	public void deleteTagFromStore(String tag);
	
	public void setFilterTag(String tag);
	public Service getServiceByName(String serviceName);
	
	
}
