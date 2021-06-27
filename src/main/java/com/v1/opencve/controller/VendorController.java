package com.v1.opencve.controller;

import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.SubsVendorDO;
import com.v1.opencve.domainobject.VendorDO;
import com.v1.opencve.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
public class VendorController {
    @Autowired
    private IVendorService vendorService = new VendorService();

    @Autowired
    private ISubsVendorService subsService = new SubsVendorService();

    @Autowired
    private IUserService userService = new UserService();

    @Autowired
    CustomUserDetailsService userDetailsService;

    List<VendorDO> listVendors;

    ResourcePatternResolver resourcePatternResolver;
    Resource[] resources;

    public void listAllFilesFromFolder() throws IOException {
        resourcePatternResolver = new PathMatchingResourcePatternResolver();
        resources = resourcePatternResolver.getResources("vendor_json/*.json");
    }

    public void insertVendorsToTable() throws IOException, ParseException {
        listAllFilesFromFolder();

        for (int x=0; x<resources.length; x++){
            StringBuilder result = new StringBuilder();
            InputStream inputStream = resources[x].getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
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
    }

    //CONTROLLER
    @RequestMapping("/vendors/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(name = "pageNum") int pageNum) {

        int pageNumber = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        try{
            insertVendorsToTable();
        }catch (Exception e){
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("errors/500");
        }

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
    public String viewHomePage(Model model) {
        GetAvatar.getGravatar(model, 30, userService);
        return viewPage(model, 1);
    }

    @RequestMapping(value = "/vendor-action", method = RequestMethod.POST)
    public String action(@ModelAttribute(value="vendorr") VendorDO vendorr, Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(auth.getName());
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
