package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.UserDO;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    // For user image of user's email
    @Autowired
    CustomUserDetailsService userDetailsService;

    private String getGravatar(Model model, Integer size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)){
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(auth.getName());
            String mail = userDetails.getEmail();
            Gravatar.setURL(mail, size);
            String userGravatar = Gravatar.getURL();
            model.addAttribute("gravatar", userGravatar);
            return userGravatar;
        }
        return null;
    }
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
            ModelAndView mv = new ModelAndView("/errors/500");
        }

        Page<VendorDO> page = vendorService.listAll(pageNum);

        List<VendorDO> listVendors = page.getContent();

        model.addAttribute("listVendors", listVendors);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listVendors", listVendors);

        getGravatar(model, 30);
        return "vendors";
    }

    @RequestMapping("/vendors")
    public String viewHomePage(Model model) {
        getGravatar(model, 30);
        return viewPage(model, 1);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public String product(@ModelAttribute(value="product") VendorDO vendorDO, Map<String, Object> model) {
       // vendorService.makeAdmin(vendorDO);
        return "redirect:vendors";
    }
}
