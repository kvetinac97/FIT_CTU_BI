//
//  CaloryService.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 22.04.2021.
//

import Foundation

/**
   Interface indentifying a service used to calculate relations between macronutrients, calories, food and activities
   Use of interface allows for change of way how calories are computed
 */
protocol CaloryServicing {
    
    var authService: AuthServicing { get }
    
    /**
        Function to compute number of accepted calories
        @param food list of food to calculate calories and macronutrients from
        @return calculated number of calories, protein, carbohydrates, fat
     */
    func caloriesInFoodMealList (foodMealList: [UserMealFood]) -> CaloryData
    
    /**
        Function to compute number of calories burned
        @param activity list of activities (which define how many calories are burned)
        @return calculated number of calories burned
     */
    func caloriesFromActivityList (activityList: [UserActivity]) -> Int
    
    /**
        Function to compute number of calories from given macronutrients
        @param protein amount of protein in food
        @param carbohydrates amount of carbohydrates in food
        @param fat amount of fat in food
        @return calculated number of calories
     */
    func caloriesFromMacronutrients (protein: Int, carbohydrates: Int, fat: Int) -> Int
    
}

struct CaloryService : CaloryServicing {
    
    var authService: AuthServicing = AuthService()
    
    func caloriesInFoodMealList (foodMealList: [UserMealFood]) -> CaloryData {
        var protein = 0, carbohydrates = 0, fat = 0
        foodMealList.forEach { userMealFood in
            protein += Int(Double(userMealFood.amount) * userMealFood.food.unitToGrams * (Double(userMealFood.food.protein) / Double(100)))
            carbohydrates += Int(Double(userMealFood.amount) * userMealFood.food.unitToGrams * (Double(userMealFood.food.carbohydrates) / Double(100)))
            fat += Int(Double(userMealFood.amount) * userMealFood.food.unitToGrams * (Double(userMealFood.food.fat) / Double(100)))
        }
        let calories = caloriesFromMacronutrients(protein: protein, carbohydrates: carbohydrates, fat: fat)
        return CaloryData(calories: calories, protein: protein, carbohydrates: carbohydrates, fat: fat)
    }

    func caloriesFromActivityList (activityList: [UserActivity]) -> Int {
        var calories = 0
        if let user = authService.getUser() {
            activityList.forEach { userActivity in
                calories += Int(Double(userActivity.duration) * userActivity.activity.caloriesPerMinute * Double(user.weight))
            }
        }
        return calories
    }

    func caloriesFromMacronutrients (protein: Int, carbohydrates: Int, fat: Int) -> Int {
        return 4 * protein + 4 * carbohydrates + 9 * fat
    }
    
}
