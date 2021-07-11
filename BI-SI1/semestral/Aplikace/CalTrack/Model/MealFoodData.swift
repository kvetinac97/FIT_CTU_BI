//
//  MealFoodData.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct MealFoodData : Codable {
    let mealId: Int
    let foodId: Int
    let date: String // Swift is not able to parse Date from JSON string
    let amount: Int
}
