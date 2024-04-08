package com.zosh.service;

import com.zosh.dto.RestaurantDTO;
import com.zosh.model.Address;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.RestaurantRepository;
import com.zosh.repository.UserRepository;
import com.zosh.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService{
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address = addressRepository.save(req.getAddress());

       Restaurant restaurant = new Restaurant();
       restaurant.setAddress(address);;
       restaurant.setContactInformation(req.getContactInformation());
       restaurant.setCuisineType(req.getCuisineType());
       restaurant.setDescription(req.getDescription());
       restaurant.setImages(req.getImages());
       restaurant.setName(req.getName());
       restaurant.setOpeningHours(req.getOpeningHours());
       restaurant.setRegistrationDate(LocalDateTime.now());
       restaurant.setOwner(user);

       return restaurantRepository.save(restaurant);

    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {

       Restaurant restaurant = findRestaurantById(restaurantId);
       if(restaurant.getCuisineType()!=null){
           restaurant.setCuisineType(updatedRestaurant.getCuisineType());
       }
       if(restaurant.getDescription()!=null){
           restaurant.setDescription(updatedRestaurant.getDescription());
       }
       if(restaurant.getName()!=null){
           restaurant.setName(updatedRestaurant.getName());
       }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> opt =restaurantRepository.findById(id);
        if(opt.isEmpty()){
            throw  new Exception(" restaurant not found with id "+id);
        }

        return opt.get();
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if(restaurant==null){
            throw  new Exception("restaurant not found with owner id "+userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDTO addToFavourites(Long restaurantId, User user) throws Exception {
       Restaurant restaurant = findRestaurantById(restaurantId);

       RestaurantDTO dto= new RestaurantDTO();
       dto.setDescription(restaurant.getDescription());
       dto.setImages(restaurant.getImages());
       dto.setTitle(restaurant.getName());
       dto.setId(restaurant.getId());

       boolean isFavourited =false;
       List<RestaurantDTO> favourites = user.getFavourites();
       for(RestaurantDTO favourite : favourites){
           if(favourite.getId().equals(restaurantId)){
               isFavourited = true;
               break;
           }
       }
     if(isFavourited){
         favourites.removeIf(favourite -> favourite.getId().equals(restaurantId));
     }else{
         favourites.add(dto);
     }

       userRepository.save(user);
       return dto;
    }

    @Override
    public Restaurant updateRestuarantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
