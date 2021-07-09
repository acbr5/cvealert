package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.SubsVendorDO;
import com.v1.cvealert.domainobject.VendorDO;
import com.v1.cvealert.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Controller
@EnableScheduling
public class VendorController {
    @Autowired
    private IVendorService vendorService = new VendorService();

    @Autowired
    private ISubsVendorService subsService = new SubsVendorService();

    @Autowired
    private IUserService userService = new UserService();

    List<VendorDO> listVendors;

    // one time in every four hours
    @Scheduled(cron="0 0 */4 * * *")
    public void insertVendorsToTable() throws IOException{
        String urlString = "https://cve.circl.lu/api/browse/";
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        rd.close();

        JSONObject jsonObject = new JSONObject(result.toString());

        JSONArray vendors = jsonObject.optJSONArray("vendor");
        for(int i = 0; i < vendors.length(); i++) {
            VendorDO vendorDO = new VendorDO();
            String vendor =  vendors.optString(i);
            if(!vendorService.isExist(vendor)){
                vendorDO.setVendorName(vendor);
                vendorService.createVendor(vendorDO);
            }
        }
    }

    @RequestMapping("/vendors/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(name = "pageNum") int pageNum) {
        Page<VendorDO> page = vendorService.listAll(pageNum);

        listVendors = page.getContent();

        model.addAttribute("listVendors", listVendors);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listVendors", listVendors);

        GetAvatar.getGravatar(model, 30, userService);
        return "vendors";
    }

    @RequestMapping("/vendors")
    public String viewHomePage(Model model) throws IOException {
        GetAvatar.getGravatar(model, 30, userService);
        return viewPage(model, 1);
    }

    @RequestMapping(value = "/vendor-action", method = RequestMethod.POST)
    public String action(@ModelAttribute(value="vendorr") VendorDO vendorr) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            Long userID = userService.getIDByUsername(auth.getName());
            Long vendorID =  vendorr.getId();
            SubsVendorDO subsDO = new SubsVendorDO();
            subsDO.setVendorID(vendorID);
            subsDO.setUserID(userID);
            subsService.createSubs(subsDO);
        }
        return "redirect:vendors";
    }
}
