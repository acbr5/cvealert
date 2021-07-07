package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.ProductsDO;
import com.v1.cvealert.domainobject.SubsProductDO;
import com.v1.cvealert.domainobject.SubsVendorDO;
import com.v1.cvealert.domainobject.VendorDO;
import com.v1.cvealert.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubscriptionsController {

    @Autowired
    IUserService userService = new UserService();

    @Autowired
    IVendorService vendorService = new VendorService();

    @Autowired
    IProductsService productsService = new ProductsService();

    @Autowired
    ISubsVendorService subsService = new SubsVendorService();

    @Autowired
    ISubsProductService subsProductService = new SubsProductService();

    @RequestMapping(value="/account/subscriptions", method= RequestMethod.GET)
    public ModelAndView subscriptions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)){
            // For vendor subscriptions
            List<VendorDO> listVendors = vendorService.getAllVendors();
            List<SubsVendorDO> listSubs = subsService.getAllSubs();
            Long userID = userService.getIDByUsername(auth.getName());

            List<VendorDO> listSubscriptions = new ArrayList<>();

            for(SubsVendorDO subsDOS: listSubs){
                for(VendorDO vendorDOS: listVendors){
                    if(subsDOS.getVendorID().equals(vendorDOS.getId()) && subsDOS.getUserID().equals(userID)){
                        listSubscriptions.add(vendorDOS);
                    }
                }
            }
            model.addAttribute("listSubscriptions", listSubscriptions);

            // For product subscriptions
            List<ProductsDO> listProducts = productsService.getAllProducts();
            List<SubsProductDO> listSubsPro = subsProductService.getAllSubs();

            List<ProductsDO> listProductSubscriptions = new ArrayList<>();

            for(SubsProductDO subsDOS: listSubsPro){
                for(ProductsDO productsDO: listProducts){
                    if(subsDOS.getProductID().equals(productsDO.getId()) && subsDOS.getUserID().equals(userID)){
                        listProductSubscriptions.add(productsDO);
                    }
                }
            }
            model.addAttribute("listProductSubs", listProductSubscriptions);
            ModelAndView mv = new ModelAndView("profiles/subscriptions");
            GetAvatar.getGravatar(model, 30, userService);
            return mv;
        }
        else {
            ModelAndView mv = new ModelAndView("errors/403");
            return mv;
        }
    }
}