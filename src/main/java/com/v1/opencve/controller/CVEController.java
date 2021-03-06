package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.CVEDO;
import com.v1.opencve.repository.CVERepository;
import com.v1.opencve.repository.ICVERepository;
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
    private ICVERepository cveRepository = new CVERepository();

    List<String> vendors;
    List<String> products;

    ResourcePatternResolver resourcePatternResolver;
    Resource[] resources;

    public CVEController() throws IOException {
    }

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

    public void listAllFilesFromFolder() throws IOException {
        resourcePatternResolver = new PathMatchingResourcePatternResolver();
        resources = resourcePatternResolver.getResources("cve_jsons/*.json");
    }

    public void insertCVEsToTable() throws IOException, ParseException {
        listAllFilesFromFolder();

        vendors = new ArrayList<>();
        products = new ArrayList<>();
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

                    String cvssv3="", cvssv2="";
                    Date publishedDate, lastModifiedDate;

                    JSONObject impact =  jsonPlant.optJSONObject("impact");
                    if(!impact.isEmpty()){
                        if(!impact.isNull("baseMetricV3") && !impact.optJSONObject("baseMetricV3").isEmpty()){
                            JSONObject baseMetricV3 =  impact.optJSONObject("baseMetricV3");
                            JSONObject cvssV3 =  baseMetricV3.optJSONObject("cvssV3");
                            cvssv3 =  cvssV3.optDouble("baseScore") + " " +  cvssV3.optString("baseSeverity");
                        }
                        if(!impact.isNull("baseMetricV2")){
                            JSONObject baseMetricV2 = impact.optJSONObject("baseMetricV2");
                            JSONObject cvssV2 = baseMetricV2.optJSONObject("cvssV2");
                            cvssv2 = cvssV2.optDouble("baseScore") + " " + baseMetricV2.optString("severity");
                        }
                    }
                    SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

                    publishedDate = jsonPlant.isNull("publishedDate") ? null : obj.parse(jsonPlant.optString("publishedDate"));
                    lastModifiedDate = jsonPlant.isNull("lastModifiedDate") ? null : obj.parse(jsonPlant.optString("lastModifiedDate"));

                    cveDO.setCveid(cve_id);
                    cveDO.setDescription(value);
                    cveDO.setCvssv3(cvssv3);
                    cveDO.setCvssv2(cvssv2);
                    cveDO.setPublishedDate(publishedDate);
                    cveDO.setLastModifiedDate(lastModifiedDate);
                    cveService.createCVE(cveDO);
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

        List<CVEDO> listProducts = page.getContent();

        model.addAttribute("listCVEs", listProducts);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listProducts", listProducts);

        getGravatar(model, 30);
        return "cve";
    }

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        getGravatar(model, 30);
        return viewPage(model, 1);
    }
}