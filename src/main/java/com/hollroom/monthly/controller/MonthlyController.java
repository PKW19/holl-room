package com.hollroom.monthly.controller;

import com.hollroom.community.service.FileUploadService;
import com.hollroom.monthly.domain.dto.*;
import com.hollroom.monthly.service.DivisionService;
import com.hollroom.monthly.service.MonthlyProductService;
import com.hollroom.monthly.service.MonthlyTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MonthlyController {
    private final MonthlyProductService productService;
    private final MonthlyTrendService trendService;
    private final DivisionService divisionService;
    private final static int SIDE_PAGE_NUMBER_LIMIT = 2;

    @GetMapping(value = {"/","/monthly"})
    public String showMainPage(@PageableDefault(page = 0, size = 10, sort = "divisionCode", direction = Sort.Direction.ASC) Pageable pageable, Model model){
        Page<MonthlyProductResponseDTO> productPage = productService.readProductAll(pageable);
        Map<Long,MonthlyTrendDTO> trendMap = trendService.readMonthlyTrendsByAddress("서울시");
        addPageNumber(pageable,productPage,model);
        model.addAttribute("productPage", productPage);
        model.addAttribute("trendMap", trendMap);
        return "monthly/monthly";
    }

    @GetMapping("/monthly/product")
    public String showProductPage(@PageableDefault(page = 0, size = 10, sort = "divisionCode", direction = Sort.Direction.ASC) Pageable pageable, Model model){
        Page<MonthlyProductResponseDTO> productPage = productService.readProductAll(pageable);
        addPageNumber(pageable,productPage,model);
        model.addAttribute("productPage", productPage);
        return "monthly/product_list";
    }

    @GetMapping("/monthly/product/register")
    public String showRegisterProductPage(Model model) {
        List<DivisionDTO> divisionList = divisionService.readSubDivisions("서울시");
        model.addAttribute("divisionList", divisionList);
        return "monthly/product_register";
    }



    @PostMapping("/monthly/product/register")
    public String registerProduct(MonthlyProductRequestDTO product){
        productService.insertProduct(product);
        return "redirect:/monthly";
    }

    @GetMapping("/monthly/product/division/{addr}")
    public String readDivisionProduct(@PageableDefault(page = 0, size = 10, sort = "divisionCode", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable String addr, Model model){
        // 시간이 부족하여 첫번째 페이지의 경우, 매물 전체를 기준으로 필터링하는 편법을 사용
        // 개선해야함.
        Page<MonthlyProductResponseDTO> productPage = addr.equals("서울시")?productService.readProductAll(pageable):productService.readDivisionProduct(addr,pageable);
        addPageNumber(pageable,productPage,model);
        model.addAttribute("productPage", productPage);
        return "monthly/product_list";
    }
    @GetMapping("/division/{addr}")
    @ResponseBody
    public DivisionDTO readDivisionCode(@PathVariable String addr){
        return divisionService.readMainDivision(addr);
    }

    @GetMapping("/monthly/product/{id}")
    public String readProduct(@PathVariable Long id, Model model){
        MonthlyProductResponseDTO product = productService.readProduct(id);
        model.addAttribute("product", product);
        return "monthly/product_read";
    }

    private void addPageNumber(Pageable pageable,Page<MonthlyProductResponseDTO> productPage, Model model){
        int startPage = pageable.getPageNumber()-SIDE_PAGE_NUMBER_LIMIT;
        int endPage = pageable.getPageNumber()+SIDE_PAGE_NUMBER_LIMIT;

        if(productPage.getTotalPages() > 1){
            if(startPage < 0){
                endPage+= -startPage;
                startPage = 0;
            }
            if(endPage > productPage.getTotalPages()){
                startPage+= endPage-productPage.getTotalPages();
                endPage = productPage.getTotalPages();
            }
        }else{
            startPage = 0;
            endPage = 0;
        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

}
