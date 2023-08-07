package com.mysite.shop.item;

import com.mysite.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;		// 관리자 , 날짜별로 검색 

    private ItemSellStatus searchSellStatus;	//관리자, 상품의 판매상태 (sell, sold out)

    private String searchBy;	//관리자, 상품명으로 검색 , 등록자로 검색  

    private String searchQuery = "";   // main에서 검색어가 주입, 관리자 페이지에서도 사용 

}