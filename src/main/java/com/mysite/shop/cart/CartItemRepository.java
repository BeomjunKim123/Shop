package com.mysite.shop.cart;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	//JPQL : 메소드로 쿼리를 처리하 언어 
	//ORM : 메소드를 SQL 쿼리로 매핑 : JPA <=== 하이버네이트 
	//select * from CartItem where cart_id =? and item_id = ? 
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    
    // JPQL : Native Query를 사용 . 
    // Query Dsl 
    
    @Query("select new com.mysite.shop.cart.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
            )
  //  List<CartDetailDto> findCartDetailDtoList(Long cartId);
   List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
    
    /*
     * 	ANSI JOIN 
    
		select ci.count , i.item_nm, i.price , ii.img_url
		from cart_item  ci
		    JOIN item i 
		            ON    ci.item_id = i.item_id 
		    JOIN item_img ii 
		            ON ii.item_id = i.item_id
		where ci.cart_id = 52  and ii.repimg_yn = 'Y' ; 
    
     */
    
    

}