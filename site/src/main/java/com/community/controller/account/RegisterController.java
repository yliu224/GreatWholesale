/*-
 * #%L
 * Community Demo Site
 * %%
 * Copyright (C) 2009 - 2023 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */

package com.community.controller.account;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.controller.account.BroadleafRegisterController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerAttribute;
import org.broadleafcommerce.profile.core.domain.CustomerAttributeImpl;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.community.HCRegisterCustomerForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller responsible for registering a customer
 */
@Controller
@RequestMapping("/register")
public class RegisterController extends BroadleafRegisterController {

    @RequestMapping(method = RequestMethod.GET)
    public String register(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm) {
        return super.register(registerCustomerForm, request, response, model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processRegister(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("registrationForm") HCRegisterCustomerForm registerCustomerForm, BindingResult errors)
            throws ServiceException, PricingException {
        String url = super.processRegister(registerCustomerForm, errors, request, response, model);
        if (url.equals(getRegisterSuccessView())) {
            // Grab the current customer from the request
            Customer newCustomer = CustomerState.getCustomer();

            // Create the referralCode CustomerAttribute
            CustomerAttribute referralCodeAttr = new CustomerAttributeImpl();
            referralCodeAttr.setName("referralCode");
            referralCodeAttr.setValue(registerCustomerForm.getReferralCode());
            referralCodeAttr.setCustomer(newCustomer);

            // Update our customer object
            newCustomer.getCustomerAttributes().put("referralCode", referralCodeAttr);

            newCustomer = customerService.saveCustomer(newCustomer);

            // Place the new customer onto the request
            CustomerState.setCustomer(newCustomer);
        }
        return url;
    }

    @ModelAttribute("registrationForm")
    public HCRegisterCustomerForm initCustomerRegistrationForm() {
        RegisterCustomerForm superForm = super.initCustomerRegistrationForm();

        HCRegisterCustomerForm form = new HCRegisterCustomerForm();
        form.setCustomer(superForm.getCustomer());
        return form;
    }

    @Override
    public String getRegisterView() {
        return "authentication/login";
    }
}
