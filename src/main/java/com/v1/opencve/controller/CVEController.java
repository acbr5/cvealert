package com.v1.opencve.controller;

import com.v1.opencve.domainobject.CVEDO;
import com.v1.opencve.domainobject.CVEProductDO;
import com.v1.opencve.domainobject.ProductsDO;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CVEController {

    @Autowired
    private ICVEService cveService = new CVEService();

    @Autowired
    private IVendorService vendorService = new VendorService();

    @Autowired
    IProductsService productsService = new ProductsService();

    @Autowired
    ICveProductService cveProductService = new CveProductService();

    @Autowired
    IUserService userService = new UserService();

    ResourcePatternResolver resourcePatternResolver;
    Resource[] resources;

    public CVEController() throws IOException {
    }

    public void listAllFilesFromFolder() throws IOException {
        resourcePatternResolver = new PathMatchingResourcePatternResolver();
        resources = resourcePatternResolver.getResources("cve_jsons/*.json");
    }

    public void insertCVEsToTable() throws IOException, ParseException {
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

            JSONArray cve_items = jsonObject.optJSONArray("CVE_Items");
            for(int i = 0; i < cve_items.length(); i++) {
                CVEDO cveDO = new CVEDO();
                JSONObject jsonPlant =  cve_items.optJSONObject(i);
                JSONObject cve =  jsonPlant.optJSONObject("cve");
                JSONObject cve_data_meta = cve.optJSONObject("CVE_data_meta");
                String cve_id = cve_data_meta.optString("ID");
                if(!cveService.isExist(cve_id)){
                    JSONObject description = cve.isNull("description") ? null : cve.optJSONObject("description");
                    JSONArray description_data = description.isNull("description_data") ? null : description.optJSONArray("description_data");
                    String value="";
                    JSONObject jsonobject = description_data.optJSONObject(0);
                    value = jsonobject.optString("value");

                    Double cvssv2BaseScore=0., cvssv3BaseScore=0.;
                    String cvssv2Severity = "", cvssv3Severity="";

                    Date publishedDate, lastModifiedDate;

                    JSONObject impact =  jsonPlant.optJSONObject("impact");
                    if(!impact.isEmpty()){
                        if(!impact.isNull("baseMetricV3") && !impact.optJSONObject("baseMetricV3").isEmpty()){
                            JSONObject baseMetricV3 =  impact.optJSONObject("baseMetricV3");
                            JSONObject cvssV3 =  baseMetricV3.optJSONObject("cvssV3");
                            cvssv3BaseScore = cvssV3.optDouble("baseScore");
                            cvssv3Severity = cvssV3.optString("baseSeverity");
                        }
                        if(!impact.isNull("baseMetricV2")){
                            JSONObject baseMetricV2 = impact.optJSONObject("baseMetricV2");
                            JSONObject cvssV2 = baseMetricV2.optJSONObject("cvssV2");
                            cvssv2BaseScore = cvssV2.optDouble("baseScore");
                            cvssv2Severity = baseMetricV2.optString("severity");
                        }
                    }

                    SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

                    publishedDate = jsonPlant.isNull("publishedDate") ? null : obj.parse(jsonPlant.optString("publishedDate"));
                    lastModifiedDate = jsonPlant.isNull("lastModifiedDate") ? null : obj.parse(jsonPlant.optString("lastModifiedDate"));

                    cveDO.setCveid(cve_id);
                    cveDO.setDescription(value);
                    cveDO.setCvssv2BaseScore(cvssv2BaseScore);
                    cveDO.setCvssv2Severity(cvssv2Severity);
                    cveDO.setCvssv3BaseScore(cvssv3BaseScore);
                    cveDO.setCvssv3Severity(cvssv3Severity);
                    cveDO.setPublishedDate(publishedDate);
                    cveDO.setLastModifiedDate(lastModifiedDate);
                    cveService.createCVE(cveDO);

                    JSONObject configurations =  jsonPlant.optJSONObject("configurations");
                    if(!configurations.isEmpty()){
                        if(!configurations.isNull("nodes")) {
                            JSONObject nodes = (configurations.isNull("nodes") ? null : configurations.optJSONArray("nodes")).optJSONObject(0);
                            if(nodes!=null){
                                if (!nodes.isNull("children")) {
                                    JSONArray children = nodes.optJSONArray("children");
                                    for (int j = 0; j < children.length(); j++) {
                                        JSONObject children1 = children.optJSONObject(j);
                                        JSONArray cpe_match_arr = children1.optJSONArray("cpe_match");
                                        for(int k=0; k<cpe_match_arr.length(); k++){
                                            JSONObject cpe_match = cpe_match_arr.optJSONObject(k);
                                            String vendor = cpe_match.optString("cpe23Uri").split(":")[3];
                                            String product = cpe_match.optString("cpe23Uri").split(":")[4];

                                            VendorDO vendorDO = new VendorDO();
                                            if(!vendorService.isExist(vendor)){
                                                vendorDO.setVendorName(vendor);
                                                vendorService.createVendor(vendorDO);
                                            }

                                            ProductsDO productsDO = new ProductsDO();
                                            if(!productsService.isExist(product)){
                                                productsDO.setProductName(product);
                                                productsDO.setVendorID(vendorService.getVendorByName(vendor).getId());
                                                productsService.createProduct(productsDO);
                                            }

                                            CVEProductDO cveProductDO = new CVEProductDO();
                                            if(!cveProductService.isCVEExist(cveDO.getId(), productsService.getProductByProductName(product).getId())){
                                                cveProductDO.setCveID(cveDO.getId());
                                                cveProductDO.setCveName(cveDO.getCveid());
                                                cveProductDO.setProductID(productsService.getProductByProductName(product).getId());
                                                cveProductDO.setProductName(productsService.getProductByProductName(product).getProductName());
                                                cveProductService.createRow(cveProductDO);
                                            }
                                        }
                                    }
                                } else {
                                    JSONArray cpe_match_arr = nodes.optJSONArray("cpe_match");
                                    for(int k=0; k<cpe_match_arr.length(); k++){
                                        JSONObject cpe_match = cpe_match_arr.optJSONObject(k);
                                        String vendor = cpe_match.optString("cpe23Uri").split(":")[3];
                                        String product = cpe_match.optString("cpe23Uri").split(":")[4];

                                        VendorDO vendorDO = new VendorDO();
                                        if(!vendorService.isExist(vendor)){
                                            vendorDO.setVendorName(vendor);
                                            vendorService.createVendor(vendorDO);
                                        }

                                        ProductsDO productsDO = new ProductsDO();
                                        if(!productsService.isExist(product)){
                                            productsDO.setProductName(product);
                                            productsDO.setVendorID(vendorService.getVendorByName(vendor).getId());
                                            productsService.createProduct(productsDO);
                                        }

                                        CVEProductDO cveProductDO = new CVEProductDO();
                                        if(!cveProductService.isCVEExist(cveDO.getId(), productsService.getProductByProductName(product).getId())){
                                            cveProductDO.setCveID(cveDO.getId());
                                            cveProductDO.setCveName(cveDO.getCveid());
                                            cveProductDO.setProductID(productsService.getProductByProductName(product).getId());
                                            cveProductDO.setProductName(productsService.getProductByProductName(product).getProductName());
                                            cveProductService.createRow(cveProductDO);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @RequestMapping("/cve/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(name = "pageNum") int pageNum) {

        int pageNumber = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        try{
            insertCVEsToTable();
        }catch (Exception e){
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("/errors/500");
        }

        Page<CVEDO> page = cveService.listAll(pageNum);

        List<CVEDO> listCVEs = page.getContent();

        List<CVEProductDO> allCVEProducts = cveProductService.getAllRows();
        List<Long> list = new ArrayList<>();
        for(CVEDO cvedo: listCVEs){

            Long cveID = cvedo.getId();
            for(int i=0; i<allCVEProducts.size(); i++){
                if((allCVEProducts.get(i).getCveID()) == cveID){
                    list.add(allCVEProducts.get(i).getId());
                }
            }
        }
        List<CVEProductDO> liste = cveProductService.getAllById(list);
        model.addAttribute("listProducts", liste);
        model.addAttribute("listCVEs", listCVEs);
      /*  model.addAttribute("listProducts", listProducts);*/
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        GetAvatar.getGravatar(model, 30, userService);
        return "cve";
    }

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        GetAvatar.getGravatar(model, 30, userService);
        return viewPage(model, 1);
    }
}