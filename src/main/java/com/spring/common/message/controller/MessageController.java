package com.spring.common.message.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class MessageController {

	@PostMapping("/changeLang")
	@ResponseBody
	public void changeLang(HttpServletRequest request, HttpServletResponse response, @RequestParam("lang") String lang) {
	    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
	    if (localeResolver != null) localeResolver.setLocale(request, response, new Locale(lang));
	}
}
