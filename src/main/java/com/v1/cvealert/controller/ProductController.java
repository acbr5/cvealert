package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.ProductsDO;
import com.v1.cvealert.domainobject.SubsProductDO;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableScheduling
public class ProductController {
    @Autowired
    IUserService userService = new UserService();

    @Autowired
    IVendorService vendorService = new VendorService();

    @Autowired
    IProductsService productsService = new ProductsService();

    @Autowired
    ISubsProductService subsService = new SubsProductService();

    public String vendorName;
    public Long vendorID;

    @Scheduled(cron="0 40 13 * * *", zone="Europe/Istanbul")
    public void insertProductsToTable() throws IOException {
        List<VendorDO> vendors = vendorService.getAllVendors();
        for(int i=0; i<vendors.size(); i++){
            String vendor = vendors.get(i).getVendorName();
            Long id = vendorService.getVendorByName(vendor).getId();
            String urlString = "https://cve.circl.lu/api/browse/" + vendor;
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

            try{
                JSONArray products = jsonObject.optJSONArray("product");
                for(int j = 0; j < products.length(); j++) {
                    ProductsDO productsDO = new ProductsDO();
                    String product_name = products.optString(j);
                    if (!productsService.isExist(product_name)) {
                        productsDO.setProductName(product_name);
                        productsDO.setVendorID(id);
                        productsService.createProduct(productsDO);
                    }
                }

            }catch (NullPointerException ex){
                continue;
            }
        }
    }

    @RequestMapping("/products/{vendorName}/{pageNum}/")
    public String viewPage(@PathVariable(name = "vendorName") String vendorName,
                           @PathVariable(name = "pageNum") int pageNum, Long vendorID,  Model model) {
        Long vendor_ID = vendorService.getVendorByName(vendorName).getId();
        List<ProductsDO> allProducts = productsService.getAllProducts();
        List<Long> list = new ArrayList<>();
        List<Long> allProductID = new ArrayList<>();

        for(int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).getVendorID().equals(vendor_ID)){
                list.add(allProducts.get(i).getId());
            }
            allProductID.add(allProducts.get(i).getId());
        }
        Page<ProductsDO> page;
        if(vendorName.equals("*"))
            page = productsService.listAll(pageNum, allProductID);
        else
            page = productsService.listAll(pageNum, list);
        List<ProductsDO> listProducts = page.getContent();

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("getVendorName", vendorName);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        GetAvatar.getGravatar(model, 30, userService);
        return "products.html";
    }

    @RequestMapping("/products/{vendorName}")
    public ModelAndView viewPage2(@PathVariable(name = "vendorName") String vendorName) {
        return new ModelAndView("redirect:/products/"+vendorName+"/1/");
    }

    @RequestMapping(value = "/product-action", method = RequestMethod.POST)
    public String viewHomePage(@ModelAttribute(value="vendorForProduct") VendorDO vendorDO, Model model, RedirectAttributes redirectAttributes) {
        GetAvatar.getGravatar(model, 30, userService);
        vendorName =  vendorDO.getVendorName();
        vendorID = vendorDO.getId();
        redirectAttributes.addAttribute("vendorName", vendorName);
        model.addAttribute("getVendorName", vendorName);
        redirectAttributes.addAttribute("pageNum", 1);
        viewPage(vendorName,1, vendorID, model);
        return "redirect:products/{vendorName}/{pageNum}/";
    }

    @RequestMapping(value = "/subsProduct", method = RequestMethod.POST)
    public String subsProduct(@ModelAttribute(value="$getProduct") ProductsDO productsDO, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            Long userID = userService.getIDByUsername(auth.getName());
            Long productID =  productsDO.getId();
            SubsProductDO subsDO = new SubsProductDO();
            subsDO.setProductID(productID);
            subsDO.setUserID(userID);
            subsService.createSubs(subsDO);
        }
        return "redirect:vendors";
    }
}