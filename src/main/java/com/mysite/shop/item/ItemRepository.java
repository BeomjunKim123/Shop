package com.mysite.shop.item;

import com.mysite.shop.dto.MainItemDto;
import com.mysite.shop.item.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
    
   /*
    @Query(value="Select i.item_id, i.item_nm, i.item_detail, i.price, im.img_url "
    		+ "from item i"
    		+ "      join item_img im"
    		+ "          ON i.item_id = im.item_id "
    		+ "where im.repimg_yn = 'Y'", nativeQuery = true)
    Page<MainItemDto> getMainItemPage_T( Pageable pageable);
	*/
}