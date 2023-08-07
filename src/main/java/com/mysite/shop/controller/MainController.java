package com.mysite.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.shop.item.ItemSearchDto;
import com.mysite.shop.dto.MainItemDto;
import com.mysite.shop.item.ItemService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

    	// pageable : (현재 페이지 , 한페이지에서 출력할 갯수 ) 
    	// PageRequest.of( page , 6)    //
    	// PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

}