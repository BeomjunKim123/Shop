package com.mysite.shop.item;

import com.mysite.shop.item.ItemFormDto;
import com.mysite.shop.item.Item;
import com.mysite.shop.item.ItemImg;
import com.mysite.shop.item.ItemImgRepository;
import com.mysite.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.mysite.shop.item.ItemImgDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;

import com.mysite.shop.item.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysite.shop.dto.MainItemDto;

@Log4j2
@Service
@Transactional				// 클래스 내부의 메소드가 하나의 트랜잭션으로 처리하도록 함. 
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록   
    	// client 에서 넘긴 itemFormDto의 값을 getter 로 끄집어내서 
    	// item Entity 클래스의 Setter로 주입후 Repository 의 save 메소드로 장 
        Item item = itemFormDto.createItem();
        /*
        System.out.println("=========서버로 배포후에는 log.info()=================");
        log.info("==> " + item.getItemNm());
        log.info("==> " + item.getPrice());
        log.info("==> " + item.getStockNumber());
        log.info("==> " + item.getItemDetail());
        log.info("==> " + item.getItemSellStatus());
        */ 
        itemRepository.save(item);
        

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
        	
            ItemImg itemImg = new ItemImg();
            
            itemImg.setItem(item);			//Foreign Key 설정 

            if(i == 0)						// 첫 번째 이미지는 Y (대표 이미지 설정) 
                itemImg.setRepimgYn("Y");   
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)    // 메소드가 처리되는 동안 오류가 발생되면 롤백, 오류가 없으면 커밋 
    public ItemFormDto getItemDtl(Long itemId){
    	
    	//item_img 테이블에서 값을 가지고 와서 dto 에 주입 
    	// DB에서 item_img 테이블의 레코드의 값을 가져와서 ItemImg Entity 클래스에 저장후 리스트에 추가 
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        
        for (ItemImg itemImg : itemImgList) {
        	
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        
        
        
        // item 테이블에서 정보를 가지고 와서 dto 에 주입

        //itemID 값이 존재하면 item Entity 클래스에 저장이되고 없으면 예외를 발생 시키도록 함.  
        Item item = itemRepository.findById(itemId)
        		.orElseThrow(EntityNotFoundException::new);
        
        
        // itemFormDTO = item + item_img 테이블의 모두 저장해서 클라이언트로 전송해주는 DTO 
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    	//return itemRepository.getMainItemPage_T( pageable);
    }

}