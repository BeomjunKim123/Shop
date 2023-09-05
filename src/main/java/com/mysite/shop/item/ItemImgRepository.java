package com.mysite.shop.item;

import com.mysite.shop.item.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

	//select * from item_img where item_id = ? order by id asc  
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //select * from item_img where item_id = ? and repimg_yn = ?
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}