package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.*;
import com.v1.cvealert.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@EnableScheduling
public class CVEController {

    @Autowired
    private ICVEService cveService = new CVEService();

    @Autowired
    private IVendorService vendorService = new VendorService();

    @Autowired
    private IProductsService productsService = new ProductsService();

    @Autowired
    private ICveProductService cveProductService = new CveProductService();

    @Autowired
    private IUserService userService = new UserService();

    @Autowired
    private ISubsVendorService subsVendorService = new SubsVendorService();

    @Autowired
    private ISubsProductService subsProductService = new SubsProductService();

    @Autowired
    private IReportsService reportsService = new ReportsService();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resourcePatternResolver.getResources("cve_jsons/*.json");

    public CVEController() throws IOException {
    }

    // 01:00 in every day
    @Scheduled(cron="0 44 21 * * *", zone="Europe/Istanbul")
    public void cve_last_json() throws ParseException, IOException, MessagingException {
        //FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-2021.json")){
                insertCVEsToTable(index, "cve_json");
            }
        }
    }

    // one time in every four hours
    @Scheduled(cron="0 0 */4 * * *", zone="Europe/Istanbul")
    public void modified_json() throws ParseException, IOException, MessagingException {
        FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-modified.json")){
                insertCVEsToTable(index, "modified_json");
            }
        }
    }

    // one time in every four hours
    @Scheduled(cron="0 0 */4 * * *", zone="Europe/Istanbul")
    public void recent_json() throws IOException, ParseException, MessagingException {

        FileDownloader.downloadCVEFiles();
        for (int index=0; index<resources.length; index++) {
            String filename = resources[index].toString().split("/")[8];
            if(filename.substring(0, filename.length()-1).equals("nvdcve-1.1-recent.json")){
                insertCVEsToTable(index, "recent_json");
            }
        }
    }

    public void insertCVEsToTable(Integer index, String json_name) throws IOException, ParseException, MessagingException {
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
                                List<String> allVendors = new ArrayList<>();
                                List<String> allProducts = new ArrayList<>();

                                if (!nodes.isNull("children")) {
                                    JSONArray children = nodes.optJSONArray("children");
                                    for (int j = 0; j < children.length(); j++) {
                                        JSONObject children1 = children.optJSONObject(j);
                                        JSONArray cpe_match_arr = children1.optJSONArray("cpe_match");
                                        cpe_match(cpe_match_arr, cveDO, allVendors, allProducts);
                                    }
                                }
                                if (!nodes.isNull("cpe_match")) {
                                    JSONArray cpe_match_arr = nodes.optJSONArray("cpe_match");
                                    cpe_match(cpe_match_arr, cveDO, allVendors, allProducts);
                                }
                                List<UserDO> allUsers = userService.getAllUsers();
                                List<Long> userIDs = new ArrayList<>();
                                for(UserDO user : allUsers) userIDs.add(user.getId());
                                for(Long userID : userIDs) {
                                    subscribe(cveDO, allVendors, allProducts, userID);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void cpe_match(JSONArray cpe_match_arr, CVEDO cveDO, List<String> allVendors, List<String> allProducts){
        for (int k = 0; k < cpe_match_arr.length(); k++) {
            JSONObject cpe_match = cpe_match_arr.optJSONObject(k);
            if(cpe_match.optString("vulnerable").equals("true")){
                String vendor = cpe_match.optString("cpe23Uri").split(":")[3];
                String product = cpe_match.optString("cpe23Uri").split(":")[4];

                VendorDO vendorDO = new VendorDO();
                if (!vendorService.isExist(vendor)) {
                    vendorDO.setVendorName(vendor);
                    vendorService.createVendor(vendorDO);
                }
                else if(!allVendors.contains(vendor))
                    allVendors.add(vendor);

                ProductsDO productsDO = new ProductsDO();
                if (!productsService.isExist(product)) {
                    productsDO.setProductName(product);
                    productsDO.setVendorID(vendorService.getVendorByName(vendor).getId());
                    productsService.createProduct(productsDO);
                }
                else if(!allProducts.contains(product))
                    allProducts.add(product);

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

    public void subscribe(CVEDO cveDO, List<String> allVendors, List<String> allProducts, Long userID) throws UnsupportedEncodingException, MessagingException, ParseException {
        List<String> subscribes = new ArrayList<>();
        for (String vendor : allVendors) {
            Long vendorID = vendorService.getVendorByName(vendor).getId();
            Optional<SubsVendorDO> subsVendorsOpt = subsVendorService.getSubsByVendorID(vendorID);
            List<SubsVendorDO> subsVendors = toList(subsVendorsOpt);
            for (SubsVendorDO subsVendor : subsVendors) {
                if (subsVendor.getUserID().equals(userID)) {
                    if (!subscribes.contains(vendor)) {
                        subscribes.add(vendor);
                    }
                }
            }
        }
        for (String product : allProducts) {
            Long productID = productsService.getProductByProductName(product).getId();
            Optional<SubsProductDO> subsProductsOpt = subsProductService.getSubsByProductID(productID);
            List<SubsProductDO> subsProducts = toList(subsProductsOpt);
            for (SubsProductDO subsProduct : subsProducts) {
                if (subsProduct.getUserID().equals(userID)) {
                    if (!subscribes.contains(product)) {
                        subscribes.add(product);
                    }
                }
            }
        }
        String email = userService.getUserByUserID(userID).getEmail();
        mailUserForCVEAlert(cveDO, subscribes, email, userID);

        if(subscribes.size() != 0){
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date currentSqlDate = new Date(System.currentTimeMillis());
            Date customUtilDate = dateFormatter.parse(currentSqlDate.toString());

            ReportsDO reportsDO = new ReportsDO();
            reportsDO.setCveID(cveDO.getId());
            reportsDO.setUserID(userID);
            reportsDO.setReportedDate(customUtilDate);
            String deletedFirstAndLastChars = subscribes.toString().substring(1, subscribes.toString().length() - 1);
            reportsDO.setSubscribes(deletedFirstAndLastChars);
            reportsDO.setCvename(cveDO.getCveid());
            reportsDO.setCvedetails(cveDO.getDescription());
            reportsService.createReports(reportsDO);
        }
    }

    public static <T> List<T> toList(Optional<T> opt) {
        return opt.isPresent()
                ? Collections.singletonList(opt.get())
                : Collections.emptyList();
    }

    public void mailUserForCVEAlert(CVEDO cvedo, List<String> subscribes, String email, Long userID) throws MessagingException, UnsupportedEncodingException {
        if(subscribes.size() != 0){
            //String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
            String basepath = "http://localhost:8080/";
            Context context = new Context();
            context.setVariable("cvedo", cvedo);
            context.setVariable("subscribes", subscribes);
            context.setVariable("username", userService.getUserByUserID(userID).getUsername());
            context.setVariable("basepath", basepath);

            String process = templateEngine.process("email_template", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String subject = subscribes.size() + " alert on " + subscribes.toString();
            helper.setFrom("cvealert.v2@gmail.com", "Aisha from CVEAlert.com");
            helper.setTo(email);
            helper.setText(process, true);
            helper.setSubject(subject);
            mailSender.send(message);
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
    public String viewHomePage(Model model) throws IOException, ParseException, MessagingException {
        GetAvatar.getGravatar(model, 30, userService);
        return viewPage(model, 1);
    }
}