package com.mysite.shop.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mysite.shop.dto.MainItemDto;

@SpringBootTest
class ItemServiceTest {
	
	@Autowired
	ItemService itemService; 

	
	@Test
	void testGetMainItemPage() {
		
		ItemSearchDto itemSearchDto = new ItemSearchDto(); 
		itemSearchDto.setSearchQuery("가방");
		
		Pageable pageable = PageRequest.of( 0, 6);

		Page<MainItemDto>  main = 
				itemService.getMainItemPage(itemSearchDto, pageable); 
		
		System.out.println("================================");
		System.out.println(main.getTotalElements());
		System.out.println("================================");
		
	}

}
