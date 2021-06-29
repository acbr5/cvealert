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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@EnableScheduling
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

    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resourcePatternResolver.getResources("cve_jsons/*.json");

    public CVEController() throws IOException {
    }

    @Scheduled(cron="0 0 1 * * *", zone="Europe/Istanbul")
    public void cve_last_json() throws ParseException, IOException {
        FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-2021.json")){
                insertCVEsToTable(index, "cve_json");
            }
        }
    }

    @Scheduled(cron="0 0 */4 * * *", zone="Europe/Istanbul")
    public void modified_json() throws ParseException, IOException {
        FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-modified.json")){
                insertCVEsToTable(index, "modified_json");
            }
        }
    }

    @Scheduled(cron="0 0 */4 * * *", zone="Europe/Istanbul")
    public void recent_json() throws IOException, ParseException {
        FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-recent.json")){
                insertCVEsToTable(index, "recent_json");
            }
        }
    }

    public void insertCVEsToTable(Integer index, String json_name) throws IOException, ParseException {
        StringBuilder result = new StringBuilder();
        InputStream inputStream = resources[index].getInputStream();
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
            if(!cveService.isExist(cve_id) || json_name.equals("modified_json")){
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
                if(json_name.equals("modified_json")){
                    cveService.updateCVE(cveDO);
                }
                else {
                    cveService.createCVE(cveDO);

                    JSONObject configurations = jsonPlant.optJSONObject("configurations");
                    if (!configurations.isEmpty()) {
                        if (!configurations.isNull("nodes")) {
                            JSONObject nodes = (configurations.isNull("nodes") ? null : configurations.optJSONArray("nodes")).optJSONObject(0);
                            if (nodes != null) {
                                if (!nodes.isNull("children")) {
                                    JSONArray children = nodes.optJSONArray("children");
                                    for (int j = 0; j < children.length(); j++) {
                                        JSONObject children1 = children.optJSONObject(j);
                                        JSONArray cpe_match_arr = children1.optJSONArray("cpe_match");
                                        for (int k = 0; k < cpe_match_arr.length(); k++) {
                                            JSONObject cpe_match = cpe_match_arr.optJSONObject(k);
                                            String vendor = cpe_match.optString("cpe23Uri").split(":")[3];
                                            String product = cpe_match.optString("cpe23Uri").split(":")[4];

                                            VendorDO vendorDO = new VendorDO();
                                            if (!vendorService.isExist(vendor)) {
                                                vendorDO.setVendorName(vendor);
                                                vendorService.createVendor(vendorDO);
                                            }

                                            ProductsDO productsDO = new ProductsDO();
                                            if (!productsService.isExist(product)) {
                                                productsDO.setProductName(product);
                                                productsDO.setVendorID(vendorService.getVendorByName(vendor).getId());
                                                productsService.createProduct(productsDO);
                                            }

                                            CVEProductDO cveProductDO = new CVEProductDO();
                                            if (!cveProductService.isCVEExist(cveDO.getId(), productsService.getProductByProductName(product).getId())) {
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
                                    for (int k = 0; k < cpe_match_arr.length(); k++) {
                                        JSONObject cpe_match = cpe_match_arr.optJSONObject(k);
                                        String vendor = cpe_match.optString("cpe23Uri").split(":")[3];
                                        String product = cpe_match.optString("cpe23Uri").split(":")[4];

                                        VendorDO vendorDO = new VendorDO();
                                        if (!vendorService.isExist(vendor)) {
                                            vendorDO.setVendorName(vendor);
                                            vendorService.createVendor(vendorDO);
                                        }

                                        ProductsDO productsDO = new ProductsDO();
                                        if (!productsService.isExist(product)) {
                                            productsDO.setProductName(product);
                                            productsDO.setVendorID(vendorService.getVendorByName(vendor).getId());
                                            productsService.createProduct(productsDO);
                                        }

                                        CVEProductDO cveProductDO = new CVEProductDO();
                                        if (!cveProductService.isCVEExist(cveDO.getId(), productsService.getProductByProductName(product).getId())) {
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
                           @PathVariable(name = "pageNum") int pageNum) throws IOException, ParseException {
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
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        GetAvatar.getGravatar(model, 30, userService);
        return "cve";
    }

    @RequestMapping("/")
    public String viewHomePage(Model model) throws IOException, ParseException {
        GetAvatar.getGravatar(model, 30, userService);
        return viewPage(model, 1);
    }
}