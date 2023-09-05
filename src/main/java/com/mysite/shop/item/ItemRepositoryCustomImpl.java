package com.mysite.shop.item;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.mysite.shop.constant.ItemSellStatus;
import com.mysite.shop.item.ItemSearchDto;
import com.mysite.shop.dto.MainItemDto;
import com.mysite.shop.dto.QMainItemDto;

import com.mysite.shop.item.Item;
import com.mysite.shop.item.QItem;
import com.mysite.shop.item.QItemImg;


@Log4j2
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

	// Query DSL 을 사용 
	// 1. JPAQueryFactory  객체를 생성 
	
    private JPAQueryFactory queryFactory;
  
    // 2. 생성자에 매개변수로 EntityManager 를 넣어서 
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    
    
    

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //return searchSellStatus == null ? null : Item.item.itemSellStatus.eq(searchSellStatus);

    }

    
    // BooleanExpression 에 Null 이 리턴이 될경우 : 
    
    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
      
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }
    
    //Query DSL : 동적 쿼리를 사용 가능(where 절에서 Null이면 쿼리를 실행하지 않고, 
    	//쿼리가 들어오면 쿼리가 실행 
    	// JPQL은 쿼리의 자동완성이 안됨. 
    	// Query DSL은 자동완성으로 쿼리를 만들어 낼 수 있다. 

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

    	log.info("=========ItemSearchDto 값 출력 - 시작 ==========================");
    	log.info(itemSearchDto.getSearchDateType());
    	log.info(itemSearchDto.getSearchSellStatus());
    	log.info(itemSearchDto.getSearchBy());
    	log.info(itemSearchDto.getSearchQuery());    	
    	log.info("=========ItemSearchDto 값 출력 - 끝 ==========================");
    	
    	
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())		//가져올 페이지  0
                .limit(pageable.getPageSize())		//가져올 레코드수 3
                .fetch();					// 쿼리를 적용하라. List<Item> 

        //검색한 레코드 총 갯수 
        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    // Query DSL : 엔티티 클래스의 QDomain 의 객체를 생성 
    //		-- 동적 쿼리를 적용 할 수 있다.  null이 반환이되면 처리하지 않고, null 아니면 처리하게된다. 
    // QDomain 의 엔티티 클래스를 생성 해야함. 
    // 사용할 Entity 클래스 이름 앞에 QItem , QItemImg  
    
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))   //null 리턴되면 처리하지 않고, 그렇지 않으면 처리 
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())		//몇 번째 페이지를 가지고 올것인가.(0, 1, 2) 
                .limit(pageable.getPageSize())		//한 페이지에서 출력할 레코드 갯수  (6) 	
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;
        
        //Page인터페이스 : PageImpl 구현체 
        					  //List , Pageable, 레코드 총갯수 ; 
        return new PageImpl<>(content, pageable, total);
       // return new PageImpl<>(content, pageable, total);
    }

}