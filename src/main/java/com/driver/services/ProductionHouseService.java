package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
//import com.driver.Exception.ProductionAlreadyExists;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductionHouseService {



@Autowired
    ProductionHouseRepository productionHouseRepository ;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto) {

        String name = productionHouseEntryDto.getName();
       ProductionHouse productionHouse = new ProductionHouse();
       productionHouse.setRatings(0);
       productionHouse.setName(name);


       productionHouseRepository.save(productionHouse);


        return  productionHouse.getId();
    }



}
