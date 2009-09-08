/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mockey.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockey.model.PlanItem;
import com.mockey.model.ServicePlan;
import com.mockey.model.Service;
import com.mockey.model.Url;
import com.mockey.storage.IMockeyStorage;
import com.mockey.storage.StorageRegistry;

public class ServicePlanSetupServlet extends HttpServlet {

    private static final long serialVersionUID = -2964632050151431391L;

    private Log log = LogFactory.getLog(ServicePlanSetupServlet.class);

    private IMockeyStorage store = StorageRegistry.MockeyStorage;

    /**
     * 
     * 
     * @param req
     *            basic request
     * @param resp
     *            basic resp
     * @throws ServletException
     *             basic
     * @throws IOException
     *             basic
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Service Plan setup/delete");
        ServicePlan servicePlan = null;
        Long servicePlanId = null;
        List<Service> allServices = store.getServices();
        try {
            servicePlanId = new Long(req.getParameter("plan_id"));
            servicePlan = store.getServicePlanById(servicePlanId);
        } catch (Exception e) {
            // Do nothing
        }
        String action = req.getParameter("action");
        if ("delete_plan".equals(action) && servicePlan != null) {

            Util.saveErrorMessage("Service plan <b>" + servicePlan.getName() + "</b> deleted.", req);
            store.deleteServicePlan(servicePlan);
            String contextRoot = req.getContextPath();
            resp.sendRedirect(Url.getContextAwarePath("home", contextRoot));
            return;
        } else if ("set_plan".equals(action) && servicePlan != null) {

            setPlan(servicePlan);
            Util.saveSuccessMessage("Service plan " + servicePlan.getName() + " is set.", req);
            String contextRoot = req.getContextPath();
            resp.sendRedirect(Url.getContextAwarePath("home", contextRoot));
            return;
        } else if ("edit_plan".equals(action)) {
            req.setAttribute("mode", "edit_plan");

            if (servicePlan != null) {
                allServices = new ArrayList<Service>();
                Iterator<PlanItem> iter = servicePlan.getPlanItemList().iterator();
                while (iter.hasNext()) {
                    PlanItem pi = (PlanItem) iter.next();
                    Service msb = store.getServiceById(pi.getServiceId());
                    if (msb != null) {
                        msb.setHangTime(pi.getHangTime());
                        msb.setDefaultScenarioId(pi.getScenarioId());
                        msb.setServiceResponseType(pi.getServiceResponseType());
                        allServices.add(msb);
                    }
                }
            }

        }
        if (servicePlan == null) {
            servicePlan = new ServicePlan();
        }
        req.setAttribute("services", allServices);
        req.setAttribute("plans", store.getServicePlans());
        req.setAttribute("plan", servicePlan);
        req.setAttribute("universalError", store.getUniversalErrorScenario());
        RequestDispatcher dispatch = req.getRequestDispatcher("/home.jsp");
        dispatch.forward(req, resp);
    }

    /**
     * 
     * 
     * @param req
     *            basic request
     * @param resp
     *            basic resp
     * @throws ServletException
     *             basic
     * @throws IOException
     *             basic
     */    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServicePlan servicePlan = new ServicePlan();
        Long servicePlanId = null;

        try {
            servicePlanId = new Long(req.getParameter("plan_id"));
        } catch (Exception e) {
            // Do nothing
        }
        if (servicePlanId != null) {
            servicePlan = this.store.getServicePlanById(servicePlanId);
        }
        servicePlan.setName(req.getParameter("plan_name"));
        servicePlan.setDescription(req.getParameter("plan_description"));
        String[] planItems = req.getParameterValues("plan_item");
        boolean createPlan = true;
        if (planItems != null) {
            for (int i = 0; i < planItems.length; i++) {

                String serviceId = planItems[i];
                String ssIdKey = "scenario_" + serviceId;
                String serviceResponseTypeKey = "serviceResponseType_" + serviceId;
                String hangTime = "hangTime_" + serviceId;
                int hangTimeInt = 500;
                String scenarioId = req.getParameter(ssIdKey);
                String serviceOnlyButtonKey = req.getParameter("update_service_" + serviceId);
                int serviceResponseTypeKeyInt = 0;
                try {
                    serviceResponseTypeKeyInt = Integer.parseInt(req.getParameter(serviceResponseTypeKey));
                } catch (Exception e) {

                }
                try {
                    hangTimeInt = Integer.parseInt(req.getParameter(hangTime));
                } catch (Exception e) {

                }

                if (serviceOnlyButtonKey != null) {
                    createPlan = false;
                    Service msb = store.getServiceById(Long.parseLong(serviceId));
                    if (scenarioId != null) {
                        msb.setDefaultScenarioId(Long.parseLong(scenarioId));
                    }
                    msb.setServiceResponseType(serviceResponseTypeKeyInt);
                    store.saveOrUpdateService(msb);
                    Util.saveSuccessMessage("Service \"" + msb.getServiceName() + "\" updated.", req);
                    break;
                }
                PlanItem planItem = new PlanItem();
                if (scenarioId != null) {
                    planItem.setScenarioId(new Long(scenarioId));
                }
                planItem.setHangTime(hangTimeInt);
                planItem.setServiceId(new Long(serviceId));
                planItem.setServiceResponseType(serviceResponseTypeKeyInt);
                servicePlan.addPlanItem(planItem);
            }
        }

        if (createPlan) {

            Util.saveSuccessMessage("Service plan updated.", req);
            store.saveOrUpdateServicePlan(servicePlan);

        }

        req.setAttribute("services", store.getServices());
        req.setAttribute("plans", store.getServicePlans());
        req.setAttribute("plan", servicePlan);
        req.setAttribute("universalError", store.getUniversalErrorScenario());
        RequestDispatcher dispatch = req.getRequestDispatcher("/home.jsp");
        dispatch.forward(req, resp);
    }

    private void setPlan(ServicePlan servicePlan) {    	
    	for (PlanItem planItem : servicePlan.getPlanItemList()) {
            Service service = store.getServiceById(planItem.getServiceId());
            if (service != null) {
                service.setHangTime(planItem.getHangTime());
                service.setDefaultScenarioId(planItem.getScenarioId());
                service.setServiceResponseType(planItem.getServiceResponseType());
                store.saveOrUpdateService(service);
            }
        }
    }
}