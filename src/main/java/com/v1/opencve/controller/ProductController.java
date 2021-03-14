package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.ProductsDO;
import com.v1.opencve.domainobject.SubsProductDO;
import com.v1.opencve.domainobject.VendorDO;
import com.v1.opencve.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    IUserService userService = new UserService();

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    IVendorService vendorService = new VendorService();

    @Autowired
    IProductsService productsService = new ProductsService();

    @Autowired
    ISubsProductService subsService = new SubsProductService();

    public String vendorName;
    public Long vendorID;

    // For user image of user's email
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

    public void insertProductsToTable(String vendorName, Long vendorID) throws IOException, ParseException {
        String urlString = "https://cve.circl.lu/api/browse/" + vendorName;
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

        JSONArray products = jsonObject.optJSONArray("product");
        for(int i = 0; i < products.length(); i++) {
            ProductsDO productsDO = new ProductsDO();
            String product_name =  products.optString(i);
            if(!productsService.isExist(product_name)){
                productsDO.setProductName(product_name);
                productsDO.setVendorID(vendorID);
                productsService.createProduct(productsDO);
            }
        }
    }

    @RequestMapping("/products/{vendorName}/{pageNum}/")
    public String viewPage(@PathVariable(name = "vendorName") String vendorName,
                           @PathVariable(name = "pageNum") int pageNum, Long vendorID,  Model model) {

        int pageNumber = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        try{
            insertProductsToTable(vendorName, vendorID);
        }catch (Exception e){
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("errors/500");
        }

        Long vendor_ID = vendorService.getVendorByName(vendorName).getId();
        List<ProductsDO> allProducts = productsService.getAllProducts();
        List<Long> list = new ArrayList<>();

        for(int i=0; i<allProducts.size(); i++){
            if((allProducts.get(i).getVendorID()) == vendor_ID){
                list.add(allProducts.get(i).getId());
            }
        }
        Page<ProductsDO> page = productsService.listAll(pageNum, list);
        List<ProductsDO> listProducts = page.getContent();

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("getVendorName", vendorName);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        getGravatar(model, 30);
        return "products.html";
    }

    @RequestMapping(value = "/product-action", method = RequestMethod.POST)
    public String viewHomePage(@ModelAttribute(value="vendorForProduct") VendorDO vendorDO, Model model, RedirectAttributes redirectAttributes) {
        getGravatar(model, 30);
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
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(auth.getName());
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