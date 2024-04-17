package com.zosh.service;

import com.zosh.model.Category;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.repository.FoodRepositroy;
import com.zosh.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService{

    @Autowired
    private FoodRepositroy foodRepositroy;

    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {

        Food food= new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredients(req.getIngredients());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegeterian());
        food.setCreationDate(new Date());

       Food savedFood= foodRepositroy.save(food);
       restaurant.getFoods().add(savedFood);
       return  savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setRestaurant(null);
        foodRepositroy.save(food);

    }

    @Override
    public List<Food> getRestaurantsFood(Long resaturantId,
                                         boolean isVegeterian,
                                         boolean isNonveg,
                                         boolean isSeasonal,
                                         String foodCategory) {

        List<Food> foods= foodRepositroy.findByRestaurantId(resaturantId);

        if(isVegeterian){
            foods = filterByVegeteriant(foods,isVegeterian);
        }
        if(isNonveg){
            foods = filterByNonVeg(foods,isNonveg);
        }
        if(isSeasonal){
            foods = filterBySeasonal(foods,isSeasonal);
        }
        if(foodCategory!=null && !foodCategory.equals("")){
            foods = filterByFoodCategory(foods, foodCategory);
        }
        return foods;
    }

    private List<Food> filterByFoodCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if(food.getFoodCategory()!=null){
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food -> food.isSeasonal()==isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean isNonveg) {

        return foods.stream().filter(food -> food.isVegetarian()==false).collect(Collectors.toList());
    }

    private List<Food> filterByVegeteriant(List<Food> foods, boolean isVegeterian) {

        return foods.stream().filter(food-> food.isVegetarian()==isVegeterian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String Keyword) {
        return foodRepositroy.searchFood(Keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepositroy.findById(foodId);
        if(optionalFood.isEmpty()){
            throw new Exception("Food not exist");
        }

        return optionalFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepositroy.save(food);
    }
}
