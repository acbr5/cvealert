package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.BlogDO;
import com.v1.opencve.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    IUserService userService = new UserService();

    @Autowired
    IBlogService blogService = new BlogService();

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

    @RequestMapping(value="/blog", method= RequestMethod.GET)
    public ModelAndView blog(Model model) {
        getGravatar(model, 30);
        ModelAndView mv = new ModelAndView("blog");
        Optional<BlogDO> blogDO;
        blogDO = blogService.getBlogByID(2L);
        BlogDO blog2 = new BlogDO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(auth.getName());
            Long userID = userService.getIDByUsername(auth.getName());
            String title = "What is CVE?";
            if(!blogService.isExist(title)){
                String content = "Common Vulnerabilities and Exposures (CVE) is a database of publicly disclosed information security issues. Stick around cybersecurity long enough, and you’ll hear the term CVE used on a near daily basis. In these cases, the reference is usually to the CVE number, each of which uniquely identifies one vulnerability from the list. CVE provides a convenient, reliable way for vendors, enterprises, academics, and all other interested parties to exchange information about cybersecurity issues. Enterprises typically use CVE, and corresponding CVSS scores, for planning and prioritization in their vulnerability management programs. Understanding CVE Identifiers\\n" +
                        "\n" +
                        "Every CVE is assigned a number known as a CVE Identifier. CVE identifiers are assigned by one of around 100 CVE Numbering Authorities (CNAs). CNAs include IT vendors, research organizations like universities, security companies, and even MITRE themselves.\n" +
                        "\n" +
                        "A CVE identifier takes the form of CVE-[Year]-[Number]. Year represents the year in which the vulnerability was reported. The number is a sequential number assigned by the CNA.\n" +
                        "\n" +
                        "For example, CVE-2019-0708, corresponds to a flaw in Microsoft’s Remote Desktop Protocol (RDP) implementation. While CVE-2019-0709 might not sound familiar, you might recognize the common name given to this CVE, BlueKeep. Infamous CVEs, like BlueKeep, that get a lot of enterprise (and press) attention commonly get an informal nickname as an easy way to remember the vulnerability in question. A select few CVEs even get their own cool custom logo or graphic (often designed by the marketing teams at the vendor or organization looking to publicize information on the vulnerability to attract journalist interest):How Many CVEs are There?\n" +
                        "\n" +
                        "There are thousands of new CVEs every year. Since the CVE program was started in 1999, over 130,000 CVE Identifiers have been issued. Over the last few years, there have been 12,000-15,000 new CVEs annually.\n" +
                        "\n" +
                        "Large software vendors with many products represent a large portion of the reported CVEs. Microsoft and Oracle, for example, each have over 6000 reported CVEs across their many product lines. In fact, the top 50 software vendors represent more than half of all CVEs issued since the inception of the CVE program."+

                        "\n Source: https://www.balbix.com/insights/what-is-a-cve/";
                blog2.setUserID(userID);
                blog2.setTitle(title);
                blog2.setContent(content);
                blogService.createBlog(blog2);
            }
        }
        model.addAttribute("blogDO", blogDO);
        mv = new ModelAndView("blog.html");
        return mv;
    }
}
