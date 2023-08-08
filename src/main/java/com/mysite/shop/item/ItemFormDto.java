package com.mysite.shop.item;

import com.mysite.shop.constant.ItemSellStatus;
import com.mysite.shop.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    // Dto의 값을 Entity 클래스와 연결해서 자동으로 값이 주입 되도록 
    // //modelmapper 라이브러리 반드시 등록 되어 있어야 사용 가능 
    
    private static ModelMapper modelMapper = new ModelMapper();  
    
    //Client form 에서 넘어오는 값을 DTO에 담아서 Item Entity 클래스에 적용후 DB에 저장  
    	//ModelMapper  : dto의 값을 Entity 클래스에 자동으로 주입하는 라이브러리 
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    
    //DB에서 가져온 item Entity 클래스를 DTO 에 주입해서 client 로 전송 하기 위한 매핑 
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

}