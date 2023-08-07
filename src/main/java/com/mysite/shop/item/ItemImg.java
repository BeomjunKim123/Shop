package com.mysite.shop.item;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import com.mysite.shop.entity.BaseEntity;

@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    
    // Foreign Key  : 지연 로딩 설정  :  기본적으로 서버의 부하를 줄이기 위해서 많은 테이블을 조인시 기본 설정이다. 
    // ItemImg (Many) , Item (one) 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    
    

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}