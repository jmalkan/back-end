package com.jiggy.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application that are generic or don't fit in a CRUD type of controller.
 * 
 * @author jmalkan
 */
@Controller
@RequestMapping("system")
public class SystemController {
  private final Logger logger = LoggerFactory.getLogger(SystemController.class);
  
  /**
   * Method to accept a ping to keep session alive.
   * 
   * @return The string value "OK" to signify that session is alive
   */
  @ResponseBody
  @RequestMapping(value = "/keepAlive", method = RequestMethod.GET)
  public String keepAlive() {
    return "OK";
  }
  
  /**
   * Method to accept a ping to keep session alive.
   * 
   * @return The string value "OK" to signify that session is alive
   */
  @ResponseBody
  @RequestMapping(value = "/invalidateSession", method = RequestMethod.POST)
  public void invalidateSession(final HttpSession session) {
    this.logger.warn("Invalidating current http session.");
    session.invalidate();
  }
}