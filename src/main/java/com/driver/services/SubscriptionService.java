package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;
@Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription newSubscription= new Subscription();
        newSubscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        newSubscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        int subscriptionAmount= getSubscriptionTypeAmount(newSubscription.getSubscriptionType());

        int totalAmount= 0;

        if(subscriptionAmount==500){
          totalAmount = subscriptionAmount+(200*newSubscription.getNoOfScreensSubscribed());
        }
        if(subscriptionAmount==800){
            totalAmount = subscriptionAmount+(250*newSubscription.getNoOfScreensSubscribed());
        }
        if(subscriptionAmount==1000) {
            totalAmount = subscriptionAmount + (350 * newSubscription.getNoOfScreensSubscribed());
        }

        newSubscription.setTotalAmountPaid(totalAmount);
        user.setSubscription(newSubscription);
        userRepository.save(user);

        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        Subscription currentSubscription = userRepository.findById(userId).get().getSubscription();

        if(currentSubscription.getSubscriptionType() == SubscriptionType.ELITE) {
            throw new Exception("Already the best Subscription");
        }

        SubscriptionType newSubscriptionType;
        if (currentSubscription.getSubscriptionType() == SubscriptionType.BASIC) {
            newSubscriptionType = SubscriptionType.PRO;
        }
        else {
            newSubscriptionType = SubscriptionType.ELITE;
        }

        int currentPrice = currentSubscription.getTotalAmountPaid();
        int newPrice = 0;
        int subscriptionAmount = getSubscriptionTypeAmount(newSubscriptionType);


        if (subscriptionAmount == 500) newPrice = subscriptionAmount + (200 * currentSubscription.getNoOfScreensSubscribed());
        else if (subscriptionAmount == 800) newPrice = subscriptionAmount + (250 * currentSubscription.getNoOfScreensSubscribed());
        else if (subscriptionAmount == 1000) newPrice = subscriptionAmount + (350 * currentSubscription.getNoOfScreensSubscribed());

        int priceDifference = newPrice - currentPrice;

        currentSubscription.setSubscriptionType(newSubscriptionType);
        subscriptionRepository.save(currentSubscription);

        return priceDifference;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int revenue =0;

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList= subscriptionRepository.findAll();
        for(Subscription s : subscriptionList){
            revenue += s.getTotalAmountPaid();
        }




        return revenue;
    }
    public int getSubscriptionTypeAmount(SubscriptionType subscriptionType) {
        if (subscriptionType == SubscriptionType.BASIC) return 500;
        else if (subscriptionType == SubscriptionType.PRO) return 800;
        else return 1000;
    }

}
