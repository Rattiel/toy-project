package com.demo.auth.web.controller;

import com.demo.auth.authorization.core.mfa.MfaNeedAuthenticationToken;
import com.demo.auth.web.resolver.GetRedirectUrl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Setter
@Controller
public class UserController {
    private SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @GetMapping("/login")
    public String login(
            @AuthenticationPrincipal UserDetails user,
            @GetRedirectUrl String redirectUrl
    ) {
        if (user != null) {
            log.info("redirectUrl: {}", redirectUrl);
            return "redirect:" + redirectUrl;
        }
        return "login";
    }

    @GetMapping("/mfa")
    public String mfa(Model model) {
        Authentication authentication = this.contextHolderStrategy.getContext().getAuthentication();
        if (!(authentication instanceof MfaNeedAuthenticationToken token)) {
            return "redirect:/login";
        }
        model.addAttribute(
                "verificationMethod",
                token.getToken().getVerificationMethod().toLowerCase()
        );
        model.addAttribute(
                "verificationAddress",
                token.getToken().getVerificationAddress()
        );
        model.addAttribute(
                "verificationCode",
                token.getToken().getCode()
        );
        return "mfa";
    }
}
