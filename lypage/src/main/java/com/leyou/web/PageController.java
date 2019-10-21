package com.leyou.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.leyou.service.PageService;

import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;
@GetMapping("item/{id}.html")
public String itemPage(@PathVariable("id") Long id, Model model){

    Map<String, Object> attributes = pageService.loadModel(id);
    model.addAllAttributes(attributes);
    return "item";
}

}
